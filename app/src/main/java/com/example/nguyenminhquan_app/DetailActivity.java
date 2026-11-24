package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailActivity extends AppCompatActivity {
    private static final String[] SIZES_CLOTHES = {"S", "M", "L", "XL"};
    private static final String[] SIZES_SHOES = {"39", "40", "41", "42"};

    private Product product;

    private ImageView imgFavorite;
    private View sectionSize;
    private LinearLayout layoutSize;

    private LinearLayout layoutInlineReviews;
    private View layoutReviewEmpty;
    private MaterialButton btnSeeAllReviews;

    private LinearLayout layoutSimilarProducts;
    private LinearLayout layoutAccessories;

    private boolean favorite;
    private int selectedSize = 1;
    private String[] activeSizes = SIZES_CLOTHES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ToolbarHelper.setupBack(this);

        product = (Product) getIntent().getSerializableExtra(Constants.EXTRA_PRODUCT);
        if (product == null) {
            finish();
            return;
        }

        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            product.setCategory(AppDatabase.getProductCategory(this, product.getId()));
        }

        imgFavorite = findViewById(R.id.imgFavorite);
        sectionSize = findViewById(R.id.sectionSize);
        layoutSize = findViewById(R.id.layoutSize);

        layoutInlineReviews = findViewById(R.id.layoutInlineReviews);
        layoutReviewEmpty = findViewById(R.id.layoutReviewEmpty);
        btnSeeAllReviews = findViewById(R.id.btnSeeAllReviews);

        layoutSimilarProducts = findViewById(R.id.layoutSimilarProducts);
        layoutAccessories = findViewById(R.id.layoutAccessories);

        showProduct();
        setupFavorite();
        setupRating();
        setupSizes();
        showProductSpecifications();
        showInlineReviews();
        showSimilarProducts();
        showRecommendedAccessories();

        MaterialButton btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(v -> addToCart());

        MaterialButton btnGoCart = findViewById(R.id.btnGoCart);
        btnGoCart.setOnClickListener(v ->
                startActivity(new Intent(DetailActivity.this, CartActivity.class)));

        MaterialButton btnWriteReviewDetail = findViewById(R.id.btnWriteReviewDetail);
        if (btnWriteReviewDetail != null) {
            btnWriteReviewDetail.setOnClickListener(v -> showWriteReviewDialog());
        }

        if (btnSeeAllReviews != null) {
            btnSeeAllReviews.setOnClickListener(v -> openAllReviews());
        }
    }

    private void openAllReviews() {
        Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
        intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    private void setupRating() {
        findViewById(R.id.rowRating).setOnClickListener(v -> openAllReviews());
    }

    private void showRating() {
        RatingSummary summary = AppDatabase.getRatingSummary(this, product.getId());

        LinearLayout layoutRatingStars = findViewById(R.id.layoutRatingStars);
        TextView tvRatingValue = findViewById(R.id.tvRatingValue);

        StarsHelper.show(layoutRatingStars, summary.getAverage(), 16);

        if (summary.isEmpty()) {
            tvRatingValue.setText(R.string.review_none);
        } else {
            tvRatingValue.setText(String.format(Locale.US, "%.1f  •  ", summary.getAverage())
                    + getString(R.string.review_count, summary.getTotal()));
        }
    }

    private void showInlineReviews() {
        List<Review> reviews = AppDatabase.getReviews(this, product.getId());
        RatingSummary summary = AppDatabase.getRatingSummary(this, product.getId());

        TextView tvAverageInline = findViewById(R.id.tvAverageInline);
        LinearLayout layoutAverageStarsInline = findViewById(R.id.layoutAverageStarsInline);
        TextView tvTotalInline = findViewById(R.id.tvTotalInline);

        if (tvAverageInline != null) {
            tvAverageInline.setText(String.format(Locale.US, "%.1f", summary.getAverage()));
        }
        if (layoutAverageStarsInline != null) {
            StarsHelper.show(layoutAverageStarsInline, summary.getAverage(), 18);
        }
        if (tvTotalInline != null) {
            tvTotalInline.setText(summary.getTotal() == 0 ?
                    getString(R.string.review_none) : getString(R.string.review_count, summary.getTotal()));
        }

        if (layoutInlineReviews == null) return;
        layoutInlineReviews.removeAllViews();

        if (reviews.isEmpty()) {
            if (layoutReviewEmpty != null) layoutReviewEmpty.setVisibility(View.VISIBLE);
            if (btnSeeAllReviews != null) btnSeeAllReviews.setVisibility(View.GONE);
        } else {
            if (layoutReviewEmpty != null) layoutReviewEmpty.setVisibility(View.GONE);

            int displayCount = Math.min(reviews.size(), 3);
            for (int i = 0; i < displayCount; i++) {
                Review r = reviews.get(i);
                View card = LayoutInflater.from(this)
                        .inflate(R.layout.item_review_card, layoutInlineReviews, false);

                TextView tvAvatar = card.findViewById(R.id.tvAvatarInitial);
                TextView tvAuthorName = card.findViewById(R.id.tvAuthorName);
                TextView tvReviewDate = card.findViewById(R.id.tvReviewDate);
                TextView tvCardComment = card.findViewById(R.id.tvCardComment);
                LinearLayout layoutCardStars = card.findViewById(R.id.layoutCardStars);

                String name = r.getUsername();
                if (name == null || name.isEmpty()) name = "Khách hàng";
                tvAuthorName.setText(name);

                String initial = name.substring(0, 1).toUpperCase(Locale.getDefault());
                tvAvatar.setText(initial);

                tvReviewDate.setText(timeAgo(r.getCreatedAt()));
                tvCardComment.setText(r.getComment());
                StarsHelper.show(layoutCardStars, r.getRating(), 14);

                layoutInlineReviews.addView(card);
            }

            if (btnSeeAllReviews != null) {
                btnSeeAllReviews.setVisibility(reviews.size() > 2 ? View.VISIBLE : View.GONE);
                btnSeeAllReviews.setText(getString(R.string.detail_see_all_reviews) + " (" + reviews.size() + ")");
            }
        }
    }

    private void showWriteReviewDialog() {
        View form = LayoutInflater.from(this).inflate(R.layout.dialog_write_review, null);

        RatingBar ratingBar = form.findViewById(R.id.ratingBar);
        EditText edtComment = form.findViewById(R.id.edtComment);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.review_write)
                .setView(form)
                .setPositiveButton(R.string.review_send, null)
                .setNegativeButton(R.string.review_cancel, null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            int rating = Math.round(ratingBar.getRating());
            String comment = edtComment.getText().toString().trim();

            if (rating < 1) {
                Toast.makeText(this, R.string.review_need_rating, Toast.LENGTH_SHORT).show();
                return;
            }

            if (comment.isEmpty()) {
                Toast.makeText(this, R.string.review_need_comment, Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.addReview(this, product.getId(), Session.getUsername(this), rating, comment);
            dialog.dismiss();

            Toast.makeText(this, R.string.review_thanks, Toast.LENGTH_SHORT).show();
            showRating();
            showInlineReviews();
        });
    }

    private String timeAgo(long createdAt) {
        long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - createdAt);
        if (days <= 0) return getString(R.string.review_time_today);
        return getString(R.string.review_time_days, days);
    }

    private void showProductSpecifications() {
        TextView tvDetailSku = findViewById(R.id.tvDetailSku);
        TextView tvDetailCategory = findViewById(R.id.tvDetailCategory);
        TextView tvDetailMaterial = findViewById(R.id.tvDetailMaterial);

        if (tvDetailSku != null) {
            tvDetailSku.setText(String.format(Locale.US, "ECOMM-%04d", product.getId()));
        }

        String category = product.getCategory();
        if (category == null || category.isEmpty()) {
            category = "Thời trang";
        }
        if (tvDetailCategory != null) {
            tvDetailCategory.setText(category);
        }

        if (tvDetailMaterial != null) {
            String nameLower = product.getName() != null ? product.getName().toLowerCase(Locale.getDefault()) : "";
            if (nameLower.contains("sơ mi")) {
                tvDetailMaterial.setText("Lụa pha cotton cao cấp, mềm rủ ít nhăn");
            } else if (nameLower.contains("khoác")) {
                tvDetailMaterial.setText("Vải gió 2 lớp chống thấm nước nhẹ, lót lưới");
            } else if (nameLower.contains("jean")) {
                tvDetailMaterial.setText("Denim cotton dệt chéo, co giãn nhẹ");
            } else if (nameLower.contains("kaki") || nameLower.contains("short")) {
                tvDetailMaterial.setText("Kaki 100% sợi dệt tự nhiên, dày dặn");
            } else if (nameLower.contains("giày") || nameLower.contains("sneaker")) {
                tvDetailMaterial.setText("Lưới dệt Mesh thoáng khí, đế cao su non êm ái");
            } else if (nameLower.contains("đồng hồ")) {
                tvDetailMaterial.setText("Thép không gỉ 316L, kính cứng chống xước, dây silicon");
            } else if (nameLower.contains("balo")) {
                tvDetailMaterial.setText("Vải Canvas trượt nước cao cấp, đệm lưng êm");
            } else if (nameLower.contains("kính")) {
                tvDetailMaterial.setText("Gọng nhựa dẻo TR90, tròng phân cực UV400");
            } else if (nameLower.contains("tai nghe")) {
                tvDetailMaterial.setText("Nhựa ABS hoàn thiện nhám cao cấp, đệm tai mút bọt khí");
            } else if (nameLower.contains("bình")) {
                tvDetailMaterial.setText("Inox 304 tiêu chuẩn thực phẩm, giữ nhiệt 12h");
            } else if (nameLower.contains("sổ")) {
                tvDetailMaterial.setText("Bìa da PU mềm, giấy Fort định lượng 100gsm");
            } else {
                tvDetailMaterial.setText("Cotton Compact 100% tự nhiên, co giãn 4 chiều");
            }
        }
    }

    private void showSimilarProducts() {
        if (layoutSimilarProducts == null) return;
        layoutSimilarProducts.removeAllViews();

        List<Product> similarList = AppDatabase.getSimilarProducts(this, product.getId(), product.getCategory());
        for (Product item : similarList) {
            View card = LayoutInflater.from(this)
                    .inflate(R.layout.item_product_recommend, layoutSimilarProducts, false);

            ImageView img = card.findViewById(R.id.imgProduct);
            TextView tvName = card.findViewById(R.id.tvName);
            TextView tvPrice = card.findViewById(R.id.tvPrice);

            ImageLoader.apply(img, item);
            tvName.setText(item.getName());
            tvPrice.setText(Money.format(item.getPrice()));

            card.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                intent.putExtra(Constants.EXTRA_PRODUCT, item);
                startActivity(intent);
            });

            layoutSimilarProducts.addView(card);
        }
    }

    private void showRecommendedAccessories() {
        if (layoutAccessories == null) return;
        layoutAccessories.removeAllViews();

        List<Product> accessories = AppDatabase.getRecommendedAccessories(this, product.getId());
        for (Product item : accessories) {
            View card = LayoutInflater.from(this)
                    .inflate(R.layout.item_product_recommend, layoutAccessories, false);

            ImageView img = card.findViewById(R.id.imgProduct);
            TextView tvName = card.findViewById(R.id.tvName);
            TextView tvPrice = card.findViewById(R.id.tvPrice);

            ImageLoader.apply(img, item);
            tvName.setText(item.getName());
            tvPrice.setText(Money.format(item.getPrice()));

            card.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, DetailActivity.class);
                intent.putExtra(Constants.EXTRA_PRODUCT, item);
                startActivity(intent);
            });

            layoutAccessories.addView(card);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showRating();
        showInlineReviews();
    }

    private void setupFavorite() {
        favorite = AppDatabase.isFavorite(this, product.getId());
        showFavoriteIcon();

        imgFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void toggleFavorite() {
        favorite = AppDatabase.toggleFavorite(this, product.getId());
        showFavoriteIcon();

        int message = favorite ? R.string.detail_favorite_added : R.string.detail_favorite_removed;
        Toast.makeText(this, getString(message, product.getName()), Toast.LENGTH_SHORT).show();
    }

    private void showFavoriteIcon() {
        if (favorite) {
            imgFavorite.setImageResource(R.drawable.ic_favorite);
            imgFavorite.clearColorFilter();
        } else {
            imgFavorite.setImageResource(R.drawable.ic_favorite_border);
            imgFavorite.setColorFilter(ContextCompat.getColor(this, R.color.text_primary));
        }
    }

    private void setupSizes() {
        String category = product.getCategory();
        if (Constants.CATEGORY_ACCESSORY.equals(category) || Constants.CATEGORY_OTHER.equals(category)) {
            if (sectionSize != null) {
                sectionSize.setVisibility(View.GONE);
            }
            return;
        }

        if (sectionSize != null) {
            sectionSize.setVisibility(View.VISIBLE);
        }

        if (Constants.CATEGORY_SHOES.equals(category)) {
            activeSizes = SIZES_SHOES;
        } else {
            activeSizes = SIZES_CLOTHES;
        }

        layoutSize.removeAllViews();
        for (int i = 0; i < activeSizes.length; i++) {
            MaterialCardView card = buildSizeCard(activeSizes[i]);

            int index = i;
            card.setOnClickListener(v -> selectSize(index));

            layoutSize.addView(card);
        }

        showSelectedSize();
    }

    private MaterialCardView buildSizeCard(String label) {
        MaterialCardView card = new MaterialCardView(this);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(dp(54), dp(54));
        params.setMarginEnd(dp(10));
        card.setLayoutParams(params);

        card.setRadius(dp(14));
        card.setCardElevation(0);
        card.setStrokeWidth(dp(1));
        card.setClickable(true);
        card.setFocusable(true);

        TextView tvLabel = new TextView(this);
        tvLabel.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvLabel.setGravity(Gravity.CENTER);
        tvLabel.setText(label);
        tvLabel.setTextSize(15);

        card.addView(tvLabel);
        return card;
    }

    private void selectSize(int index) {
        selectedSize = index;
        showSelectedSize();

        Toast.makeText(this, getString(R.string.detail_size_selected, activeSizes[index]),
                Toast.LENGTH_SHORT).show();
    }

    private void showSelectedSize() {
        for (int i = 0; i < layoutSize.getChildCount(); i++) {
            MaterialCardView card = (MaterialCardView) layoutSize.getChildAt(i);
            boolean selected = i == selectedSize;

            card.setCardBackgroundColor(ContextCompat.getColor(this,
                    selected ? R.color.brand_primary : R.color.white));
            card.setStrokeColor(ContextCompat.getColor(this,
                    selected ? R.color.brand_primary : R.color.divider));

            TextView tvLabel = (TextView) card.getChildAt(0);
            tvLabel.setTextColor(ContextCompat.getColor(this,
                    selected ? R.color.white : R.color.text_primary));
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void showProduct() {
        ImageView imgDetail = findViewById(R.id.imgDetail);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvDescription = findViewById(R.id.tvDetailDescription);

        ImageLoader.apply(imgDetail, product);

        tvName.setText(product.getName());
        tvPrice.setText(Money.format(product.getPrice()));
        tvDescription.setText(product.getDescription());
    }

    private void addToCart() {
        AppDatabase.addToCart(this, product.getId());

        Toast.makeText(this, getString(R.string.detail_added_to_cart, product.getName()),
                Toast.LENGTH_SHORT).show();
    }
}
