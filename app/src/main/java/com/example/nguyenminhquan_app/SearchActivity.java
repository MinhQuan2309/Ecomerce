package com.example.nguyenminhquan_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText edtSearch;
    private ListView listRecent;
    private ListView listResult;
    private View layoutRecentHeader;
    private View layoutNoResult;

    private List<Product> allProducts;
    private List<Product> results;

    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ToolbarHelper.setupBack(this);

        edtSearch = findViewById(R.id.edtSearch);
        listRecent = findViewById(R.id.listRecent);
        listResult = findViewById(R.id.listResult);
        layoutRecentHeader = findViewById(R.id.layoutRecentHeader);
        layoutNoResult = findViewById(R.id.layoutNoResult);

        allProducts = AppDatabase.getProducts(this, null);

        setupSearchField();
        setupResultList();
        setupClearAll();

        showRecent();
        openKeyboard();

        BottomNavigationHelper.setup(this, BottomNavigationHelper.TAB_SEARCH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationHelper.updateCartBadge(this);
    }

    private void setupSearchField() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = SearchFilter.removeAccent(s.toString().trim());
                showResults();
            }
        });

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            SearchHistory.add(this, edtSearch.getText().toString());
            return false;
        });
    }

    private void setupResultList() {
        listResult.setOnItemClickListener((parent, view, position, id) -> {
            Product product = results.get(position);

            SearchHistory.add(this, product.getName());

            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
            intent.putExtra(Constants.EXTRA_PRODUCT, product);
            startActivity(intent);
        });
    }

    private void setupClearAll() {
        TextView tvClearAll = findViewById(R.id.tvClearAll);

        tvClearAll.setOnClickListener(v -> {
            SearchHistory.clear(this);
            showRecent();
        });
    }

    private void showRecent() {
        List<String> history = SearchHistory.get(this);

        listRecent.setAdapter(new SearchHistoryAdapter(this, history, keywordToRemove -> {
            SearchHistory.remove(this, keywordToRemove);
            showRecent();
        }));

        listRecent.setOnItemClickListener((parent, view, position, id) -> {
            edtSearch.setText(history.get(position));
            edtSearch.setSelection(edtSearch.getText().length());
        });

        boolean hasHistory = !history.isEmpty();
        layoutRecentHeader.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
        listRecent.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
        listResult.setVisibility(View.GONE);
        layoutNoResult.setVisibility(View.GONE);
    }

    private void showResults() {
        if (keyword.isEmpty()) {
            showRecent();
            return;
        }

        results = SearchFilter.filter(allProducts, keyword);

        layoutRecentHeader.setVisibility(View.GONE);
        listRecent.setVisibility(View.GONE);

        if (results.isEmpty()) {
            listResult.setVisibility(View.GONE);
            layoutNoResult.setVisibility(View.VISIBLE);
            return;
        }

        layoutNoResult.setVisibility(View.GONE);
        listResult.setVisibility(View.VISIBLE);
        listResult.setAdapter(new ProductAdapter(this, results, R.layout.item_search_result));
    }

    private void openKeyboard() {
        edtSearch.requestFocus();

        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtSearch, InputMethodManager.SHOW_IMPLICIT);
    }
}
