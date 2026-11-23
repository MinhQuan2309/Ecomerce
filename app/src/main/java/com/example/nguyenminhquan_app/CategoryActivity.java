package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private static final int[] PRICE_LABELS = {
            R.string.price_all,
            R.string.price_under_200,
            R.string.price_200_500,
            R.string.price_over_500
    };

    private static final double[] PRICE_MIN = {0, 0, 200000, 500000};
    private static final double[] PRICE_MAX = {
            Double.MAX_VALUE, 200000, 500000, Double.MAX_VALUE
    };

    private LinearLayout layoutRail;
    private LinearLayout layoutPrice;
    private LinearLayout layoutProducts;
    private TextView tvCategoryName;
    private TextView tvEmptyCategory;

    private int railIndex = 0;
    private int priceIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        layoutRail = findViewById(R.id.layoutRail);
        layoutPrice = findViewById(R.id.layoutPrice);
        layoutProducts = findViewById(R.id.layoutProducts);
        tvCategoryName = findViewById(R.id.tvCategoryName);
        tvEmptyCategory = findViewById(R.id.tvEmptyCategory);

        findViewById(R.id.cardSearch).setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));

        findViewById(R.id.tvSeeAll).setOnClickListener(v -> openHome());

        setupRail();
        setupPrices();
        selectRail(0);

        BottomNavigationHelper.setup(this, BottomNavigationHelper.TAB_CATEGORY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationHelper.updateCartBadge(this);
    }

    private void setupRail() {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < Categories.BROWSE.length; i++) {
            int category = Categories.BROWSE[i];

            View row = inflater.inflate(R.layout.item_category_rail, layoutRail, false);

            TextView tvRail = row.findViewById(R.id.tvRail);
            ImageView imgRail = row.findViewById(R.id.imgRail);
            ImageView icRail = row.findViewById(R.id.icRail);

            tvRail.setText(Categories.LABELS[category]);

            if (Categories.IMAGES[category] == null) {
                imgRail.setVisibility(View.GONE);
                icRail.setVisibility(View.VISIBLE);
                icRail.setImageResource(Categories.ICONS[category]);
            } else {
                ImageLoader.applyAsset(imgRail, Categories.IMAGES[category]);
            }

            int index = i;
            row.setOnClickListener(v -> selectRail(index));

            layoutRail.addView(row);
        }
    }

    private void selectRail(int index) {
        railIndex = index;

        for (int i = 0; i < layoutRail.getChildCount(); i++) {
            View row = layoutRail.getChildAt(i);
            boolean selected = i == index;

            row.setBackgroundColor(ContextCompat.getColor(this,
                    selected ? R.color.white : android.R.color.transparent));

            TextView tvRail = row.findViewById(R.id.tvRail);
            tvRail.setTextColor(ContextCompat.getColor(this,
                    selected ? R.color.text_primary : R.color.text_secondary));
            tvRail.setTypeface(null, selected ? android.graphics.Typeface.BOLD
                    : android.graphics.Typeface.NORMAL);
        }

        tvCategoryName.setText(Categories.LABELS[Categories.BROWSE[index]]);

        showProducts();
    }

    private void setupPrices() {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < PRICE_LABELS.length; i++) {
            View chip = inflater.inflate(R.layout.item_category, layoutPrice, false);

            TextView tvCategory = chip.findViewById(R.id.tvCategory);
            tvCategory.setText(PRICE_LABELS[i]);

            int index = i;
            chip.setOnClickListener(v -> selectPrice(index));

            layoutPrice.addView(chip);
        }

        selectPrice(0);
    }

    private void selectPrice(int index) {
        priceIndex = index;

        for (int i = 0; i < layoutPrice.getChildCount(); i++) {
            MaterialCardView chip = (MaterialCardView) layoutPrice.getChildAt(i);
            boolean selected = i == index;

            chip.setCardBackgroundColor(ContextCompat.getColor(this,
                    selected ? R.color.brand_primary : R.color.white));
            chip.setStrokeColor(ContextCompat.getColor(this,
                    selected ? R.color.brand_primary : R.color.divider));

            TextView tvCategory = chip.findViewById(R.id.tvCategory);
            tvCategory.setTextColor(ContextCompat.getColor(this,
                    selected ? R.color.white : R.color.text_primary));
        }

        showProducts();
    }

    private void showProducts() {
        layoutProducts.removeAllViews();

        String tag = Categories.TAGS[Categories.BROWSE[railIndex]];
        List<Product> products = AppDatabase.getProducts(this, tag);

        LayoutInflater inflater = LayoutInflater.from(this);
        int shown = 0;

        for (Product product : products) {
            if (product.getPrice() < PRICE_MIN[priceIndex]
                    || product.getPrice() >= PRICE_MAX[priceIndex]) {
                continue;
            }

            View row = inflater.inflate(R.layout.item_category_product, layoutProducts, false);

            ImageLoader.apply(row.findViewById(R.id.imgRowProduct), product);
            ((TextView) row.findViewById(R.id.tvRowName)).setText(product.getName());
            ((TextView) row.findViewById(R.id.tvRowPrice))
                    .setText(Money.format(product.getPrice()));

            row.setOnClickListener(v -> {
                Intent intent = new Intent(CategoryActivity.this, DetailActivity.class);
                intent.putExtra(Constants.EXTRA_PRODUCT, product);
                startActivity(intent);
            });

            layoutProducts.addView(row);
            shown++;
        }

        tvEmptyCategory.setVisibility(shown == 0 ? View.VISIBLE : View.GONE);
    }

    private void openHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.EXTRA_CATEGORY,
                Categories.TAGS[Categories.BROWSE[railIndex]]);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
