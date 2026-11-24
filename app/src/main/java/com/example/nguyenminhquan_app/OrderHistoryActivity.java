package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView listOrders;
    private LinearLayout layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        ToolbarHelper.setupBack(this);

        listOrders = findViewById(R.id.listOrders);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        String username = Session.getUsername(this);
        List<Order> orders = AppDatabase.getOrdersByUser(this, username);

        if (orders == null || orders.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            listOrders.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            listOrders.setVisibility(View.VISIBLE);

            OrderHistoryAdapter adapter = new OrderHistoryAdapter(this, orders);
            listOrders.setAdapter(adapter);

            listOrders.setOnItemClickListener((parent, view, position, id) -> {
                Order order = orders.get(position);
                Intent intent = new Intent(this, OrderDetailActivity.class);
                intent.putExtra("order_id", order.getId());
                startActivity(intent);
            });
        }
    }
}
