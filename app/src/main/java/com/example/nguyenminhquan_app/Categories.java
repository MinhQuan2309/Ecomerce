package com.example.nguyenminhquan_app;

public final class Categories {

    public static final String[] TAGS = {
            null,
            Constants.FILTER_FAVORITE,
            Constants.CATEGORY_SHIRT,
            Constants.CATEGORY_PANTS,
            Constants.CATEGORY_SHOES,
            Constants.CATEGORY_ACCESSORY,
            Constants.CATEGORY_OTHER
    };

    public static final int[] LABELS = {
            R.string.filter_all,
            R.string.filter_favorite,
            R.string.filter_shirt,
            R.string.filter_pants,
            R.string.filter_shoes,
            R.string.filter_accessory,
            R.string.filter_other
    };

    public static final String[] IMAGES = {
            null,
            null,
            "ao_thun.jpg",
            "quan_jean.jpg",
            "giay_sneaker.jpg",
            "kinh_ram.jpg",
            "balo_laptop.jpg"
    };

    public static final int[] ICONS = {
            R.drawable.ic_tab_grid,
            R.drawable.ic_favorite,
            0, 0, 0, 0, 0
    };

    public static final int[] BROWSE = {0, 2, 3, 4, 5, 6};

    private Categories() {
    }
}
