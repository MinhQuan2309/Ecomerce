package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long BANNER_DELAY = 4000;
    private static final int DOT_SIZE = 6;
    private static final int DOT_WIDE = 18;

    private BottomNavigationView bottomNav;
    private GridView gridProducts;
    private View layoutProfile;
    private View scrollHome;
    private View tilSearch;
    private LinearLayout layoutCategory;
    private LinearLayout layoutDots;
    private ViewPager pagerBanner;
    private EditText edtSearch;
    private TextView tvCount;
    private TextView tvEmpty;

    private List<Product> allProducts;
    private List<Product> products;
    private String category = null;
    private String keyword = "";
    private boolean onlyFavorite = false;
    private int sortMode = 0;

    private final Handler bannerHandler = new Handler(Looper.getMainLooper());

    private final Runnable bannerRunnable = new Runnable() {
        @Override
        public void run() {
            int count = pagerBanner.getAdapter().getCount();
            pagerBanner.setCurrentItem((pagerBanner.getCurrentItem() + 1) % count, true);
            bannerHandler.postDelayed(this, BANNER_DELAY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottomNav);
        gridProducts = findViewById(R.id.gridProducts);
        layoutProfile = findViewById(R.id.layoutProfile);
        scrollHome = findViewById(R.id.scrollHome);
        tilSearch = findViewById(R.id.tilSearch);
        layoutCategory = findViewById(R.id.layoutCategory);
        layoutDots = findViewById(R.id.layoutDots);
        pagerBanner = findViewById(R.id.pagerBanner);
        tvCount = findViewById(R.id.tvCount);
        tvEmpty = findViewById(R.id.tvEmpty);

        setupToolbar();
        setupBanner();
        setupSort();
        setupSearch();
        setupList();
        setupProfile();
        setupCategories();
        setupBottomNav();

        showRequestedTab(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        showRequestedTab(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(
                layoutProfile.getVisibility() == View.VISIBLE ? R.id.nav_info : R.id.nav_list);

        BottomNavigationHelper.updateCartBadge(this);

        if (onlyFavorite) {
            loadProducts();
        }

        bannerHandler.postDelayed(bannerRunnable, BANNER_DELAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bannerHandler.removeCallbacks(bannerRunnable);
    }

    private void showRequestedTab(Intent intent) {
        if (intent.getBooleanExtra(Constants.EXTRA_SHOW_PROFILE, false)) {
            showProfile();
            return;
        }

        if (intent.hasExtra(Constants.EXTRA_CATEGORY)) {
            selectCategory(Categories.indexOf(intent.getStringExtra(Constants.EXTRA_CATEGORY)));
        }

        showList();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupBottomNav() {
        BottomNavigationHelper.setup(this, BottomNavigationHelper.TAB_LIST,
                this::showList, this::showProfile);
    }

    private void setupBanner() {
        pagerBanner.setAdapter(new BannerAdapter(this));

        for (int i = 0; i < pagerBanner.getAdapter().getCount(); i++) {
            View dot = new View(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dp(DOT_SIZE), dp(DOT_SIZE));
            params.setMarginEnd(dp(5));
            dot.setLayoutParams(params);

            layoutDots.addView(dot);
        }

        pagerBanner.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                showDots(position);
            }
        });

        showDots(0);
    }

    private void showDots(int position) {
        for (int i = 0; i < layoutDots.getChildCount(); i++) {
            View dot = layoutDots.getChildAt(i);
            boolean selected = i == position;

            ViewGroup.LayoutParams params = dot.getLayoutParams();
            params.width = dp(selected ? DOT_WIDE : DOT_SIZE);
            dot.setLayoutParams(params);

            dot.setBackgroundResource(selected
                    ? R.drawable.bg_dot_active : R.drawable.bg_dot_inactive);
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void setupCategories() {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < Categories.TAGS.length; i++) {
            View item = inflater.inflate(R.layout.item_category_image, layoutCategory, false);

            TextView tvCategory = item.findViewById(R.id.tvCategory);
            ImageView imgCategory = item.findViewById(R.id.imgCategory);
            ImageView icCategory = item.findViewById(R.id.icCategory);

            tvCategory.setText(Categories.LABELS[i]);

            if (Categories.IMAGES[i] == null) {
                imgCategory.setVisibility(View.GONE);
                icCategory.setVisibility(View.VISIBLE);
                icCategory.setImageResource(Categories.ICONS[i]);
            } else {
                ImageLoader.applyAsset(imgCategory, Categories.IMAGES[i]);
            }

            int index = i;
            item.setOnClickListener(v -> selectCategory(index));

            layoutCategory.addView(item);
        }

        selectCategory(0);
    }

    private void selectCategory(int index) {
        for (int i = 0; i < layoutCategory.getChildCount(); i++) {
            View item = layoutCategory.getChildAt(i);
            boolean selected = i == index;

            MaterialCardView card = item.findViewById(R.id.cardCategory);
            card.setStrokeWidth(selected ? dp(2) : 0);

            TextView tvCategory = item.findViewById(R.id.tvCategory);
            tvCategory.setTextColor(ContextCompat.getColor(this,
                    selected ? R.color.text_primary : R.color.text_secondary));
            tvCategory.setTypeface(null, selected ? Typeface.BOLD : Typeface.NORMAL);
        }

        onlyFavorite = Constants.FILTER_FAVORITE.equals(Categories.TAGS[index]);
        category = onlyFavorite ? null : Categories.TAGS[index];

        loadProducts();
    }

    private void setupSort() {
        findViewById(R.id.btnSort).setOnClickListener(v -> showSortDialog());
    }

    private void showSortDialog() {
        String[] options = {
                getString(R.string.home_sort_default),
                getString(R.string.home_sort_price_asc),
                getString(R.string.home_sort_price_desc),
                getString(R.string.home_sort_name)
        };

        new AlertDialog.Builder(this)
                .setTitle(R.string.home_sort_title)
                .setSingleChoiceItems(options, sortMode, (dialog, which) -> {
                    sortMode = which;
                    applyFilter();
                    dialog.dismiss();
                })
                .show();
    }

    private void sortProducts() {
        if (sortMode == 1) {
            Collections.sort(products, (a, b) -> Double.compare(a.getPrice(), b.getPrice()));
        } else if (sortMode == 2) {
            Collections.sort(products, (a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        } else if (sortMode == 3) {
            Collections.sort(products, (a, b) -> SearchFilter.removeAccent(a.getName())
                    .compareTo(SearchFilter.removeAccent(b.getName())));
        }
    }

    private void setupSearch() {
        edtSearch = findViewById(R.id.edtSearch);

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
                applyFilter();
            }
        });
    }

    private void setupList() {
        gridProducts.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(Constants.EXTRA_PRODUCT, products.get(position));
            startActivity(intent);
        });
    }

    private void loadProducts() {
        allProducts = onlyFavorite
                ? AppDatabase.getFavorites(this)
                : AppDatabase.getProducts(this, category);

        applyFilter();
    }

    private void applyFilter() {
        products = new ArrayList<>();

        for (Product p : allProducts) {
            if (SearchFilter.matches(p, keyword)) {
                products.add(p);
            }
        }

        sortProducts();

        ProductAdapter adapter =
                new ProductAdapter(this, products, R.layout.item_product_grid);
        adapter.setFavorites(AppDatabase.getFavoriteIds(this), this::loadProducts);
        gridProducts.setAdapter(adapter);

        tvCount.setText(getString(R.string.home_product_count, products.size()));

        tvEmpty.setText(onlyFavorite && keyword.isEmpty()
                ? R.string.home_empty_favorite
                : R.string.home_empty);
        tvEmpty.setVisibility(products.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setupProfile() {
        String username = Session.getUsername(this);
        String email = Session.getEmail(this);

        TextView tvAvatarLetter = findViewById(R.id.tvAvatarLetter);
        TextView tvProfileName = findViewById(R.id.tvProfileName);
        TextView tvProfileEmail = findViewById(R.id.tvProfileEmail);
        TextView tvRowUsername = findViewById(R.id.tvRowUsername);
        TextView tvRowEmail = findViewById(R.id.tvRowEmail);

        tvAvatarLetter.setText(username.substring(0, 1).toUpperCase(Locale.ROOT));
        tvProfileName.setText(username);
        tvProfileEmail.setText(email);
        tvRowUsername.setText(username);
        tvRowEmail.setText(email);

        findViewById(R.id.rowOrderHistory).setOnClickListener(v ->
                startActivity(new Intent(this, OrderHistoryActivity.class)));

        findViewById(R.id.rowAddress).setOnClickListener(v ->
                startActivity(new Intent(this, AddressActivity.class)));

        findViewById(R.id.rowEditProfile).setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class)));

        findViewById(R.id.rowFirebase).setOnClickListener(v ->
                startActivity(new Intent(this, FirebaseActivity.class)));

        MaterialButton btnLogout = findViewById(R.id.btnProfileLogout);
        btnLogout.setOnClickListener(v -> logout());
    }

    private void showList() {
        scrollHome.setVisibility(View.VISIBLE);
        tilSearch.setVisibility(View.VISIBLE);
        layoutProfile.setVisibility(View.GONE);
        setTitle(R.string.home_title);
    }

    private void showProfile() {
        scrollHome.setVisibility(View.GONE);
        tilSearch.setVisibility(View.GONE);
        layoutProfile.setVisibility(View.VISIBLE);
        setTitle(R.string.info_title);
    }

    private void logout() {
        Session.clear(this);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
