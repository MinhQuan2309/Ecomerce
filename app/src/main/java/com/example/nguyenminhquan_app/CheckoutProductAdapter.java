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

public class CheckoutProductAdapter extends RecyclerView.Adapter<CheckoutProductAdapter.ViewHolder> {
    private final Context context;
    private final List<CartItem> items;

    public CheckoutProductAdapter(Context context, List<CartItem> items) {
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
        CartItem cartItem = items.get(position);
        Product product = cartItem.getProduct();

        holder.tvProductName.setText(product.getName());
        holder.tvProductType.setText(product.getProductType() != null ? product.getProductType() : "Thời trang");
        holder.tvProductPrice.setText(Money.format(product.getPrice()));
        holder.tvProductQuantity.setText("x " + cartItem.getQuantity());
        holder.tvItemSubtotal.setText(Money.format(cartItem.getSubtotal()));

        ImageLoader.apply(holder.ivProduct, product);
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
