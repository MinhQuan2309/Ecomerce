package com.example.nguyenminhquan_app;

import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public final class ImageLoader {

    private ImageLoader() {
    }

    public static void apply(ImageView view, Product product) {
        String assetName = product.getAssetName();

        if (assetName != null && decode(view, assetName)) {
            return;
        }

        if (product.getImageRes() != 0) {
            view.setImageResource(product.getImageRes());
        } else {
            view.setImageResource(R.drawable.ic_image_placeholder);
        }
    }

    public static void applyAsset(ImageView view, String assetName) {
        if (!decode(view, assetName)) {
            view.setImageResource(R.drawable.ic_image_placeholder);
        }
    }

    private static boolean decode(ImageView view, String assetName) {
        try (InputStream is = view.getContext().getAssets().open(assetName)) {
            view.setImageBitmap(BitmapFactory.decodeStream(is));
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
