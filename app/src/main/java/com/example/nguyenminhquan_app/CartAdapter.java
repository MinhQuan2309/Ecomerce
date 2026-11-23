package com.example.nguyenminhquan_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    private CartActivity activity;
    private List<CartItem> items;

    public CartAdapter(CartActivity activity, List<CartItem> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity)
                    .inflate(R.layout.item_cart, parent, false);
        }

        ImageView imgCart = convertView.findViewById(R.id.imgCart);
        TextView tvCartName = convertView.findViewById(R.id.tvCartName);
        TextView tvCartPrice = convertView.findViewById(R.id.tvCartPrice);
        TextView tvCartSubtotal = convertView.findViewById(R.id.tvCartSubtotal);
        TextView tvQty = convertView.findViewById(R.id.tvQty);
        MaterialButton btnMinus = convertView.findViewById(R.id.btnMinus);
        MaterialButton btnPlus = convertView.findViewById(R.id.btnPlus);

        CartItem item = items.get(position);
        Product p = item.getProduct();

        ImageLoader.apply(imgCart, p);
        tvCartName.setText(p.getName());
        tvCartPrice.setText(Money.format(p.getPrice()));
        tvCartSubtotal.setText(Money.format(item.getSubtotal()));
        tvQty.setText(String.valueOf(item.getQuantity()));

        btnMinus.setOnClickListener(v -> activity.changeQuantity(p.getId(), -1));
        btnPlus.setOnClickListener(v -> activity.changeQuantity(p.getId(), 1));

        return convertView;
    }
}
