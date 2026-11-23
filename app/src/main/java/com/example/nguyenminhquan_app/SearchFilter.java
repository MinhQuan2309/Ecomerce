package com.example.nguyenminhquan_app;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class SearchFilter {

    private SearchFilter() {
    }

    public static String removeAccent(String text) {
        String lower = text.toLowerCase(Locale.ROOT).replace('đ', 'd');
        String normalized = Normalizer.normalize(lower, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "");
    }

    public static boolean matches(Product product, String keyword) {
        if (keyword.isEmpty()) return true;

        String[] nameWords = removeAccent(product.getName()).split("\\s+");

        for (String part : keyword.split("\\s+")) {
            boolean found = false;

            for (String word : nameWords) {
                if (word.startsWith(part)) {
                    found = true;
                    break;
                }
            }

            if (!found) return false;
        }

        return true;
    }

    public static List<Product> filter(List<Product> source, String keyword) {
        List<Product> result = new ArrayList<>();

        for (Product p : source) {
            if (matches(p, keyword)) {
                result.add(p);
            }
        }

        return result;
    }
}
