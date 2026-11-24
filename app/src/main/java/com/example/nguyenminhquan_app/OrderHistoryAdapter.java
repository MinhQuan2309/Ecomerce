package com.example.nguyenminhquan_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends ArrayAdapter<Order> {
    private final LayoutInflater inflater;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public OrderHistoryAdapter(@NonNull Context context, @NonNull List<Order> orders) {
        super(context, 0, orders);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order_history, parent, false);
        }

        Order order = getItem(position);
        if (order != null) {
            TextView tvOrderCode = convertView.findViewById(R.id.tvOrderCode);
            TextView tvOrderStatus = convertView.findViewById(R.id.tvOrderStatus);
            TextView tvOrderDate = convertView.findViewById(R.id.tvOrderDate);
            ImageView ivOrderThumb = convertView.findViewById(R.id.ivOrderThumb);
            TextView tvOrderSummaryText = convertView.findViewById(R.id.tvOrderSummaryText);
            TextView tvOrderAddressBrief = convertView.findViewById(R.id.tvOrderAddressBrief);
            TextView tvOrderItemsCount = convertView.findViewById(R.id.tvOrderItemsCount);
            TextView tvOrderTotal = convertView.findViewById(R.id.tvOrderTotal);

            tvOrderCode.setText("#" + order.getOrderCode());
            tvOrderStatus.setText(order.getStatus() != null ? order.getStatus() : "Đã đặt hàng");
            tvOrderDate.setText(sdf.format(new Date(order.getCreatedAt())));

            List<OrderItem> items = order.getItems();
            int totalItems = 0;
            if (items != null && !items.isEmpty()) {
                for (OrderItem it : items) {
                    totalItems += it.getQuantity();
                }
                OrderItem first = items.get(0);
                if (items.size() == 1) {
                    tvOrderSummaryText.setText(first.getProductName() + " (x" + first.getQuantity() + ")");
                } else {
                    tvOrderSummaryText.setText(first.getProductName() + " (+" + (items.size() - 1) + " sản phẩm khác)");
                }

                if (first.getProductImage() != null && !first.getProductImage().isEmpty()) {
                    ImageLoader.applyAsset(ivOrderThumb, first.getProductImage());
                } else {
                    ivOrderThumb.setImageResource(R.drawable.ic_image_placeholder);
                }
            } else {
                tvOrderSummaryText.setText("Chi tiết đơn hàng");
                ivOrderThumb.setImageResource(R.drawable.ic_order);
            }

            tvOrderItemsCount.setText(totalItems > 0 ? (totalItems + " sản phẩm") : "1 đơn hàng");
            tvOrderAddressBrief.setText("Giao đến: " + (order.getReceiverAddress() != null ? order.getReceiverAddress() : ""));
            tvOrderTotal.setText(Money.format(order.getTotal()));
        }

        return convertView;
    }
}
