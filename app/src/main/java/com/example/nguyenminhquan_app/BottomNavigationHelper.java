package com.example.nguyenminhquan_app;

import android.app.Activity;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public final class BottomNavigationHelper {

    public static final int TAB_LIST = 1;
    public static final int TAB_CART = 2;
    public static final int TAB_CATEGORY = 3;
    public static final int TAB_INFO = 4;
    public static final int TAB_SEARCH = 5;

    private BottomNavigationHelper() {
    }

    public static void setup(Activity activity, int currentTab) {
        setup(activity, currentTab, null, null);
    }

    public static void setup(Activity activity, int currentTab,
                             Runnable onList, Runnable onInfo) {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottomNav);
        if (bottomNav == null) return;

        bottomNav.setSelectedItemId(menuIdOf(currentTab));

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_list) {
                if (onList != null) {
                    onList.run();
                    return true;
                }
                openMain(activity, false);
                return false;
            }

            if (id == R.id.nav_category) {
                if (currentTab == TAB_CATEGORY) return true;
                activity.startActivity(new Intent(activity, CategoryActivity.class));
                return false;
            }

            if (id == R.id.nav_search) {
                if (currentTab == TAB_SEARCH) return true;
                activity.startActivity(new Intent(activity, SearchActivity.class));
                return false;
            }

            if (id == R.id.nav_info) {
                if (onInfo != null) {
                    onInfo.run();
                    return true;
                }
                openMain(activity, true);
                return false;
            }

            if (id == R.id.nav_cart) {
                if (currentTab == TAB_CART) return true;
                activity.startActivity(new Intent(activity, CartActivity.class));
                return false;
            }

            return false;
        });

        updateCartBadge(activity);
    }

    public static void updateCartBadge(Activity activity) {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottomNav);
        if (bottomNav == null) return;

        int count = AppDatabase.getCartCount(activity);

        if (count > 0) {
            BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_cart);
            badge.setVisible(true);
            badge.setNumber(count);
            badge.setBackgroundColor(ContextCompat.getColor(activity, R.color.danger));
            badge.setBadgeTextColor(ContextCompat.getColor(activity, R.color.white));
        } else {
            bottomNav.removeBadge(R.id.nav_cart);
        }
    }

    private static void openMain(Activity activity, boolean showProfile) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(Constants.EXTRA_SHOW_PROFILE, showProfile);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    private static int menuIdOf(int tab) {
        if (tab == TAB_CART) return R.id.nav_cart;
        if (tab == TAB_CATEGORY) return R.id.nav_category;
        if (tab == TAB_INFO) return R.id.nav_info;
        if (tab == TAB_SEARCH) return R.id.nav_search;
        return R.id.nav_list;
    }
}
