package com.example.nguyenminhquan_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.Set;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<Product> products;
    private int layoutRes;

    private Set<Integer> favoriteIds;
    private Runnable onFavoriteChanged;

    public ProductAdapter(Context context, List<Product> products) {
        this(context, products, R.layout.item_product);
    }

    public ProductAdapter(Context context, List<Product> products, int layoutRes) {
        this.context = context;
        this.products = products;
        this.layoutRes = layoutRes;
    }

    public void setFavorites(Set<Integer> favoriteIds, Runnable onFavoriteChanged) {
        this.favoriteIds = favoriteIds;
        this.onFavoriteChanged = onFavoriteChanged;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(layoutRes, parent, false);
        }
        ImageView imgProduct = convertView.findViewById(R.id.imgProduct);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        ImageView imgFavorite = convertView.findViewById(R.id.imgFavorite);

        Product p = products.get(position);

        ImageLoader.apply(imgProduct, p);

        tvName.setText(p.getName());
        tvPrice.setText(Money.format(p.getPrice()));

        if (tvDescription != null) {
            tvDescription.setText(p.getDescription());
        }

        if (imgFavorite != null) {
            showFavorite(imgFavorite, p);
        }

        return convertView;
    }

    private void showFavorite(ImageView imgFavorite, Product product) {
        boolean liked = favoriteIds != null && favoriteIds.contains(product.getId());

        if (liked) {
            imgFavorite.setImageResource(R.drawable.ic_favorite);
            imgFavorite.clearColorFilter();
        } else {
            imgFavorite.setImageResource(R.drawable.ic_favorite_border);
            imgFavorite.setColorFilter(ContextCompat.getColor(context, R.color.text_primary));
        }

        imgFavorite.setOnClickListener(v -> {
            AppDatabase.toggleFavorite(context, product.getId());

            if (onFavoriteChanged != null) {
                onFavoriteChanged.run();
            }
        });
    }
}
