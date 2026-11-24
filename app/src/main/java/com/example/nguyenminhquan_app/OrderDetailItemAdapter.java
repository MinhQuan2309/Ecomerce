package com.example.nguyenminhquan_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderDetailItemAdapter extends RecyclerView.Adapter<OrderDetailItemAdapter.ViewHolder> {
    private final Context context;
    private final List<OrderItem> items;

    public OrderDetailItemAdapter(Context context, List<OrderItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem item = items.get(position);

        holder.tvProductName.setText(item.getProductName());
        holder.tvProductType.setText("Sản phẩm chính hãng");
        holder.tvProductPrice.setText(Money.format(item.getProductPrice()));
        holder.tvProductQuantity.setText("x " + item.getQuantity());
        holder.tvItemSubtotal.setText(Money.format(item.getSubtotal()));

        if (item.getProductImage() != null && !item.getProductImage().isEmpty()) {
            ImageLoader.applyAsset(holder.ivProduct, item.getProductImage());
        } else {
            holder.ivProduct.setImageResource(R.drawable.ic_image_placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvProductName;
        TextView tvProductType;
        TextView tvProductPrice;
        TextView tvProductQuantity;
        TextView tvItemSubtotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductType = itemView.findViewById(R.id.tvProductType);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductQuantity = itemView.findViewById(R.id.tvProductQuantity);
            tvItemSubtotal = itemView.findViewById(R.id.tvItemSubtotal);
        }
    }
}
