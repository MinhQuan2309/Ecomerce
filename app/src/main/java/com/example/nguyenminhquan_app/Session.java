package com.example.nguyenminhquan_app;

import android.content.Context;
import android.content.SharedPreferences;

public final class Session {

    private Session() {
    }

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_RECEIVER = "receiver";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_DETAIL = "detail";

    public static void saveLogin(Context context, String username, String email) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_USERNAME, "");
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_EMAIL, "user@gmail.com");
    }

    public static void saveAddress(Context context, String receiver, String phone, String city, String detail) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_RECEIVER, receiver);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_DETAIL, detail);
        editor.apply();
    }

    public static String getReceiver(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String saved = prefs.getString(KEY_RECEIVER, "");
        if (saved != null && !saved.trim().isEmpty()) {
            return saved;
        }
        String username = getUsername(context);
        return (username != null && !username.trim().isEmpty()) ? username : "Nguyễn Minh Quân";
    }

    public static String getPhone(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_PHONE, "0901 234 567");
    }

    public static String getCity(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_CITY, "TP. Hồ Chí Minh");
    }

    public static String getDetail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_DETAIL, "123 Đường 3/2, Phường 11, Quận 10");
    }

    public static String getFullAddress(Context context) {
        String detail = getDetail(context);
        String city = getCity(context);
        if (city.isEmpty()) return detail;
        if (detail.contains(city)) return detail;
        return detail + ", " + city;
    }

    public static void clear(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
