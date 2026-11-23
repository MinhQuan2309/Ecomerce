package com.example.nguyenminhquan_app;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

public final class StarsHelper {
    private static final int MAX_STARS = 5;

    private StarsHelper() {
    }

    public static void show(LinearLayout container, double rating, int sizeDp) {
        Context context = container.getContext();

        if (container.getChildCount() != MAX_STARS) {
            container.removeAllViews();

            for (int i = 0; i < MAX_STARS; i++) {
                container.addView(buildStar(context, sizeDp));
            }
        }

        for (int i = 0; i < MAX_STARS; i++) {
            ImageView star = (ImageView) container.getChildAt(i);
            boolean filled = rating >= i + 0.5;

            star.setImageResource(filled ? R.drawable.ic_star : R.drawable.ic_star_border);
            star.setColorFilter(ContextCompat.getColor(context,
                    filled ? R.color.rating : R.color.divider));
        }
    }

    private static ImageView buildStar(Context context, int sizeDp) {
        int size = Math.round(sizeDp * context.getResources().getDisplayMetrics().density);
        int gap = Math.round(2 * context.getResources().getDisplayMetrics().density);

        ImageView star = new ImageView(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMarginEnd(gap);
        star.setLayoutParams(params);

        return star;
    }
}
