package com.example.nguyenminhquan_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public final class SearchHistory {
    private static final String PREF_NAME = "SearchPrefs";
    private static final String KEY_ITEMS = "items";
    private static final String SEPARATOR = "\n";
    private static final int MAX_ITEMS = 8;

    private SearchHistory() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static List<String> get(Context context) {
        String raw = prefs(context).getString(KEY_ITEMS, "");
        List<String> list = new ArrayList<>();

        if (raw.isEmpty()) return list;

        for (String item : raw.split(SEPARATOR)) {
            if (!item.trim().isEmpty()) {
                list.add(item);
            }
        }

        return list;
    }

    public static void add(Context context, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return;

        String value = keyword.trim();
        List<String> list = get(context);

        list.remove(value);
        list.add(0, value);

        while (list.size() > MAX_ITEMS) {
            list.remove(list.size() - 1);
        }

        save(context, list);
    }

    public static void remove(Context context, String keyword) {
        List<String> list = get(context);
        list.remove(keyword);
        save(context, list);
    }

    public static void clear(Context context) {
        save(context, new ArrayList<>());
    }

    private static void save(Context context, List<String> list) {
        prefs(context).edit()
                .putString(KEY_ITEMS, TextUtils.join(SEPARATOR, list))
                .apply();
    }
}
