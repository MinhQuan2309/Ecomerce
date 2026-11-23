package com.example.nguyenminhquan_app;

import java.util.Locale;

public final class Money {

    private Money() {
    }
    public static String format(double value) {
        String number = String.format(Locale.US, "%,d", (long) value).replace(',', '.');
        return number + " đ";
    }
}
