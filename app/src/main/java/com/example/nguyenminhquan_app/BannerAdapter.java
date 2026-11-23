package com.example.nguyenminhquan_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class BannerAdapter extends PagerAdapter {

    private static final int[] LABELS = {
            R.string.banner1_label, R.string.banner2_label, R.string.banner3_label
    };

    private static final int[] TITLES = {
            R.string.banner1_title, R.string.banner2_title, R.string.banner3_title
    };

    private static final String[] IMAGES = {
            "ao_khoac_gio.jpg", "giay_sneaker.jpg", "balo_laptop.jpg"
    };

    private final Context context;

    public BannerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return IMAGES.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_banner, container, false);

        TextView tvBannerLabel = view.findViewById(R.id.tvBannerLabel);
        TextView tvBannerTitle = view.findViewById(R.id.tvBannerTitle);
        ImageView imgBanner = view.findViewById(R.id.imgBanner);

        tvBannerLabel.setText(LABELS[position]);
        tvBannerTitle.setText(TITLES[position]);
        ImageLoader.applyAsset(imgBanner, IMAGES[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
