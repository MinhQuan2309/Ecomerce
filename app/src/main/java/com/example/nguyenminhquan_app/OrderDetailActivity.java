package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ToolbarHelper.setupBack(this);

        long orderId = getIntent().getLongExtra("order_id", -1);
        boolean fromCheckout = getIntent().getBooleanExtra("from_checkout", false);

        if (orderId <= 0) {
            Toast.makeText(this, "Không tìm thấy thông tin đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Order order = AppDatabase.getOrderById(this, orderId);
        if (order == null) {
            Toast.makeText(this, "Đơn hàng không tồn tại", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bindOrderData(order, fromCheckout);
        setupActions();
    }

    private void bindOrderData(Order order, boolean fromCheckout) {
        TextView tvOrderTitleStatus = findViewById(R.id.tvOrderTitleStatus);
        TextView tvOrderCode = findViewById(R.id.tvOrderCode);
        TextView tvOrderDate = findViewById(R.id.tvOrderDate);
        TextView tvStatusBadge = findViewById(R.id.tvStatusBadge);

        if (fromCheckout) {
            tvOrderTitleStatus.setText("Đặt hàng thành công!");
        } else {
            tvOrderTitleStatus.setText("Thông tin đơn hàng");
        }

        tvOrderCode.setText("Mã đơn hàng: #" + order.getOrderCode());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String dateStr = sdf.format(new Date(order.getCreatedAt()));
        tvOrderDate.setText("Đặt lúc: " + dateStr);

        tvStatusBadge.setText(order.getStatus() != null ? order.getStatus() : "Đã đặt hàng");

        TextView tvDetailReceiver = findViewById(R.id.tvDetailReceiver);
        TextView tvDetailEmail = findViewById(R.id.tvDetailEmail);
        TextView tvDetailAddress = findViewById(R.id.tvDetailAddress);
        TextView tvDetailNote = findViewById(R.id.tvDetailNote);

        tvDetailReceiver.setText(order.getReceiverName() + " • " + order.getReceiverPhone());
        tvDetailEmail.setText("Email: " + (order.getReceiverEmail() != null ? order.getReceiverEmail() : "Chưa cập nhật"));
        tvDetailAddress.setText(order.getReceiverAddress());

        if (order.getNote() != null && !order.getNote().trim().isEmpty()) {
            tvDetailNote.setVisibility(View.VISIBLE);
            tvDetailNote.setText("Ghi chú: " + order.getNote());
        } else {
            tvDetailNote.setVisibility(View.GONE);
        }

        TextView tvDetailShippingMethod = findViewById(R.id.tvDetailShippingMethod);
        TextView tvDetailPaymentMethod = findViewById(R.id.tvDetailPaymentMethod);

        tvDetailShippingMethod.setText(order.getShippingMethod() != null ? order.getShippingMethod() : "Giao hàng tiêu chuẩn");
        tvDetailPaymentMethod.setText(order.getPaymentMethod() != null ? order.getPaymentMethod() : "Thanh toán khi nhận hàng (COD)");

        TextView tvDetailProductsCount = findViewById(R.id.tvDetailProductsCount);
        int itemCount = order.getItems() != null ? order.getItems().size() : 0;
        tvDetailProductsCount.setText("DANH SÁCH MÓN HÀNG (" + itemCount + ")");

        RecyclerView rvOrderItems = findViewById(R.id.rvOrderItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(new OrderDetailItemAdapter(this, order.getItems()));

        TextView tvDetailSubtotal = findViewById(R.id.tvDetailSubtotal);
        TextView tvDetailShipping = findViewById(R.id.tvDetailShipping);
        View rowDetailDiscount = findViewById(R.id.rowDetailDiscount);
        TextView tvDetailDiscount = findViewById(R.id.tvDetailDiscount);
        TextView tvDetailTotal = findViewById(R.id.tvDetailTotal);

        tvDetailSubtotal.setText(Money.format(order.getSubtotal()));
        tvDetailShipping.setText(Money.format(order.getShippingFee()));

        if (order.getDiscount() > 0) {
            rowDetailDiscount.setVisibility(View.VISIBLE);
            tvDetailDiscount.setText("- " + Money.format(order.getDiscount()));
        } else {
            rowDetailDiscount.setVisibility(View.GONE);
        }

        tvDetailTotal.setText(Money.format(order.getTotal()));
    }

    private void setupActions() {
        MaterialButton btnOrderHistory = findViewById(R.id.btnOrderHistory);
        MaterialButton btnBackHome = findViewById(R.id.btnBackHome);

        btnOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
            finish();
        });

        btnBackHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
