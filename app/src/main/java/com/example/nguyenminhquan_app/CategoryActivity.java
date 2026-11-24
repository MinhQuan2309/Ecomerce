package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class CategoryActivity extends AppCompatActivity {

    private LinearLayout layoutRail;
    private TextView tvBannerTitle;
    private TextView tvBannerSubtitle;
    private MaterialButton btnViewAllCategory;
    private LinearLayout containerSubCategories;
    private LinearLayout containerPurposes;
    private LinearLayout containerMaterials;
    private LinearLayout containerPriceRanges;

    private int currentRailIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        layoutRail = findViewById(R.id.layoutRail);
        tvBannerTitle = findViewById(R.id.tvBannerTitle);
        tvBannerSubtitle = findViewById(R.id.tvBannerSubtitle);
        btnViewAllCategory = findViewById(R.id.btnViewAllCategory);
        containerSubCategories = findViewById(R.id.containerSubCategories);
        containerPurposes = findViewById(R.id.containerPurposes);
        containerMaterials = findViewById(R.id.containerMaterials);
        containerPriceRanges = findViewById(R.id.containerPriceRanges);

        findViewById(R.id.cardSearch).setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));

        setupRail();
        selectRail(0);

        BottomNavigationHelper.setup(this, BottomNavigationHelper.TAB_CATEGORY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationHelper.updateCartBadge(this);
    }

    private void setupRail() {
        layoutRail.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < CategoryTaxonomy.CATEGORIES.length; i++) {
            CategoryTaxonomy.MainCategory cat = CategoryTaxonomy.CATEGORIES[i];
            View row = inflater.inflate(R.layout.item_category_rail, layoutRail, false);

            TextView tvRail = row.findViewById(R.id.tvRail);
            ImageView imgRail = row.findViewById(R.id.imgRail);
            ImageView icRail = row.findViewById(R.id.icRail);

            tvRail.setText(cat.getName());

            if (cat.getImageAsset() == null) {
                imgRail.setVisibility(View.GONE);
                icRail.setVisibility(View.VISIBLE);
                icRail.setImageResource(cat.getIconRes());
            } else {
                imgRail.setVisibility(View.VISIBLE);
                icRail.setVisibility(View.GONE);
                ImageLoader.applyAsset(imgRail, cat.getImageAsset());
            }

            int index = i;
            row.setOnClickListener(v -> selectRail(index));

            layoutRail.addView(row);
        }
    }

    private void selectRail(int index) {
        currentRailIndex = index;

        for (int i = 0; i < layoutRail.getChildCount(); i++) {
            View row = layoutRail.getChildAt(i);
            boolean selected = (i == index);

            row.setBackgroundColor(ContextCompat.getColor(this,
                    selected ? R.color.white : android.R.color.transparent));

            TextView tvRail = row.findViewById(R.id.tvRail);
            tvRail.setTextColor(ContextCompat.getColor(this,
                    selected ? R.color.text_primary : R.color.text_secondary));
            tvRail.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
        }

        CategoryTaxonomy.MainCategory cat = CategoryTaxonomy.findCategory(index);
        tvBannerTitle.setText(cat.getTitle());
        tvBannerSubtitle.setText(cat.getSubtitle());

        btnViewAllCategory.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
            intent.putExtra(Constants.EXTRA_TITLE, cat.getName());
            intent.putExtra(Constants.EXTRA_CATEGORY, cat.getId());
            startActivity(intent);
        });

        populateSubCategories(cat);
        populatePurposes(cat);
        populateMaterials(cat);
        populatePriceRanges(cat);
    }

    private void populateSubCategories(CategoryTaxonomy.MainCategory cat) {
        containerSubCategories.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        CategoryTaxonomy.SubCategoryItem[] items = cat.getSubCategories();
        if (items == null || items.length == 0) return;

        // Render in 2 columns grid
        int columns = 2;
        for (int i = 0; i < items.length; i += columns) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);

            for (int col = 0; col < columns; col++) {
                int itemIndex = i + col;
                if (itemIndex < items.length) {
                    CategoryTaxonomy.SubCategoryItem sub = items[itemIndex];
                    View tile = inflater.inflate(R.layout.item_subcategory_tile, row, false);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    tile.setLayoutParams(lp);

                    ImageView img = tile.findViewById(R.id.imgSubCategory);
                    ImageView ic = tile.findViewById(R.id.icSubCategory);
                    TextView tvName = tile.findViewById(R.id.tvSubCategoryName);

                    tvName.setText(sub.getName());

                    if (sub.getImageAsset() != null) {
                        img.setVisibility(View.VISIBLE);
                        ic.setVisibility(View.GONE);
                        ImageLoader.applyAsset(img, sub.getImageAsset());
                    } else if (sub.getIconRes() != 0) {
                        img.setVisibility(View.GONE);
                        ic.setVisibility(View.VISIBLE);
                        ic.setImageResource(sub.getIconRes());
                    } else {
                        img.setVisibility(View.VISIBLE);
                        ic.setVisibility(View.GONE);
                        img.setImageResource(R.drawable.ic_image_placeholder);
                    }

                    tile.setOnClickListener(v -> {
                        Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
                        intent.putExtra(Constants.EXTRA_TITLE, sub.getName());
                        intent.putExtra(Constants.EXTRA_CATEGORY, cat.getId());
                        intent.putExtra(Constants.EXTRA_KEYWORD, sub.getFilterKeyword());
                        startActivity(intent);
                    });

                    row.addView(tile);
                } else {
                    View placeholder = new View(this);
                    placeholder.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
                    row.addView(placeholder);
                }
            }

            containerSubCategories.addView(row);
        }
    }

    private void populatePurposes(CategoryTaxonomy.MainCategory cat) {
        containerPurposes.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        CategoryTaxonomy.FilterItem[] items = cat.getPurposes();
        if (items == null || items.length == 0) return;

        int columns = 2;
        for (int i = 0; i < items.length; i += columns) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);

            for (int col = 0; col < columns; col++) {
                int itemIndex = i + col;
                if (itemIndex < items.length) {
                    CategoryTaxonomy.FilterItem filter = items[itemIndex];
                    View tile = inflater.inflate(R.layout.item_criteria_tile, row, false);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp.setMarginEnd(col == 0 ? dp(3) : 0);
                    lp.setMarginStart(col == 1 ? dp(3) : 0);
                    tile.setLayoutParams(lp);

                    TextView tvName = tile.findViewById(R.id.tvCriteriaName);
                    tvName.setText(filter.getLabel());

                    tile.setOnClickListener(v -> {
                        Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
                        intent.putExtra(Constants.EXTRA_TITLE, filter.getLabel());
                        intent.putExtra(Constants.EXTRA_CATEGORY, cat.getId());
                        intent.putExtra(Constants.EXTRA_KEYWORD, filter.getKeyword());
                        startActivity(intent);
                    });

                    row.addView(tile);
                } else {
                    View placeholder = new View(this);
                    placeholder.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
                    row.addView(placeholder);
                }
            }

            containerPurposes.addView(row);
        }
    }

    private void populateMaterials(CategoryTaxonomy.MainCategory cat) {
        containerMaterials.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        CategoryTaxonomy.FilterItem[] items = cat.getMaterials();
        if (items == null || items.length == 0) return;

        int columns = 2;
        for (int i = 0; i < items.length; i += columns) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);

            for (int col = 0; col < columns; col++) {
                int itemIndex = i + col;
                if (itemIndex < items.length) {
                    CategoryTaxonomy.FilterItem filter = items[itemIndex];
                    View tile = inflater.inflate(R.layout.item_criteria_tile, row, false);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp.setMarginEnd(col == 0 ? dp(3) : 0);
                    lp.setMarginStart(col == 1 ? dp(3) : 0);
                    tile.setLayoutParams(lp);

                    TextView tvName = tile.findViewById(R.id.tvCriteriaName);
                    tvName.setText(filter.getLabel());

                    tile.setOnClickListener(v -> {
                        Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
                        intent.putExtra(Constants.EXTRA_TITLE, filter.getLabel());
                        intent.putExtra(Constants.EXTRA_CATEGORY, cat.getId());
                        intent.putExtra(Constants.EXTRA_KEYWORD, filter.getKeyword());
                        startActivity(intent);
                    });

                    row.addView(tile);
                } else {
                    View placeholder = new View(this);
                    placeholder.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
                    row.addView(placeholder);
                }
            }

            containerMaterials.addView(row);
        }
    }

    private void populatePriceRanges(CategoryTaxonomy.MainCategory cat) {
        containerPriceRanges.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        CategoryTaxonomy.FilterItem[] items = cat.getPriceRanges();
        if (items == null || items.length == 0) return;

        int columns = 2;
        for (int i = 0; i < items.length; i += columns) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setOrientation(LinearLayout.HORIZONTAL);

            for (int col = 0; col < columns; col++) {
                int itemIndex = i + col;
                if (itemIndex < items.length) {
                    CategoryTaxonomy.FilterItem filter = items[itemIndex];
                    View tile = inflater.inflate(R.layout.item_criteria_tile, row, false);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                    lp.setMarginEnd(col == 0 ? dp(3) : 0);
                    lp.setMarginStart(col == 1 ? dp(3) : 0);
                    tile.setLayoutParams(lp);

                    TextView tvName = tile.findViewById(R.id.tvCriteriaName);
                    tvName.setText(filter.getLabel());

                    tile.setOnClickListener(v -> {
                        Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
                        intent.putExtra(Constants.EXTRA_TITLE, filter.getLabel());
                        intent.putExtra(Constants.EXTRA_CATEGORY, cat.getId());
                        intent.putExtra(Constants.EXTRA_MIN_PRICE, filter.getMinPrice());
                        intent.putExtra(Constants.EXTRA_MAX_PRICE, filter.getMaxPrice());
                        startActivity(intent);
                    });

                    row.addView(tile);
                } else {
                    View placeholder = new View(this);
                    placeholder.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
                    row.addView(placeholder);
                }
            }

            containerPriceRanges.addView(row);
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
