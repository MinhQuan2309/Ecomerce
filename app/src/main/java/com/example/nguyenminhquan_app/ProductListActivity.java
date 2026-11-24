package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private GridView gridProducts;
    private View layoutEmpty;
    private TextView tvProductCount;
    private TextView tvFilterHint;
    private TextView tvToolbarTitle;

    private String categoryId;
    private String keyword;
    private double minPrice;
    private double maxPrice;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        ToolbarHelper.setupBack(this);

        title = getIntent().getStringExtra(Constants.EXTRA_TITLE);
        categoryId = getIntent().getStringExtra(Constants.EXTRA_CATEGORY);
        keyword = getIntent().getStringExtra(Constants.EXTRA_KEYWORD);
        minPrice = getIntent().getDoubleExtra(Constants.EXTRA_MIN_PRICE, 0);
        maxPrice = getIntent().getDoubleExtra(Constants.EXTRA_MAX_PRICE, Double.MAX_VALUE);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvProductCount = findViewById(R.id.tvProductCount);
        tvFilterHint = findViewById(R.id.tvFilterHint);
        gridProducts = findViewById(R.id.gridProducts);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        if (title != null && !title.isEmpty()) {
            tvToolbarTitle.setText(title);
        } else {
            tvToolbarTitle.setText(R.string.category_title);
        }

        loadProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        List<Product> products = AppDatabase.getProductsByFilter(this, categoryId, keyword, minPrice, maxPrice);

        // Fallback: If strict keyword didn't match anything, try category alone
        if (products.isEmpty() && categoryId != null && !categoryId.isEmpty() && !"ALL".equalsIgnoreCase(categoryId)) {
            products = AppDatabase.getProductsByFilter(this, categoryId, null, 0, Double.MAX_VALUE);
        }

        int count = products.size();
        tvProductCount.setText(count + " sản phẩm");

        if (title != null) {
            tvFilterHint.setText(title);
            tvFilterHint.setVisibility(View.VISIBLE);
        } else {
            tvFilterHint.setVisibility(View.GONE);
        }

        if (count == 0) {
            gridProducts.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            gridProducts.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);

            ProductAdapter adapter = new ProductAdapter(this, products, R.layout.item_product_grid);
            adapter.setFavorites(AppDatabase.getFavoriteIds(this), this::loadProducts);
            gridProducts.setAdapter(adapter);

            gridProducts.setOnItemClickListener((parent, view, position, id) -> {
                Product selected = (Product) adapter.getItem(position);
                Intent intent = new Intent(ProductListActivity.this, DetailActivity.class);
                intent.putExtra(Constants.EXTRA_PRODUCT, selected);
                startActivity(intent);
            });
        }
    }
}
