package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    private static final String[] SIZES = {"S", "M", "L", "XL"};

    private Product product;

    private ImageView imgFavorite;
    private LinearLayout layoutSize;

    private boolean favorite;
    private int selectedSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ToolbarHelper.setupBack(this);

        product = (Product) getIntent().getSerializableExtra(Constants.EXTRA_PRODUCT);

        imgFavorite = findViewById(R.id.imgFavorite);
        layoutSize = findViewById(R.id.layoutSize);

        showProduct();
        setupFavorite();
        setupRating();
        setupSizes();

        MaterialButton btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(v -> addToCart());

        MaterialButton btnGoCart = findViewById(R.id.btnGoCart);
        btnGoCart.setOnClickListener(v ->
                startActivity(new Intent(DetailActivity.this, CartActivity.class)));
    }

    private void setupRating() {
        findViewById(R.id.rowRating).setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, ReviewActivity.class);
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.getId());
            startActivity(intent);
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        showRating();
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
        for (int i = 0; i < SIZES.length; i++) {
            MaterialCardView card = buildSizeCard(SIZES[i]);

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

        Toast.makeText(this, getString(R.string.detail_size_selected, SIZES[index]),
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
