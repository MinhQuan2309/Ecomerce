package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ToolbarHelper.setupBack(this);

        showBuyer();
        showOrder();

        MaterialButton btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> confirmOrder());
    }

    private void showBuyer() {
        TextView tvBuyerName = findViewById(R.id.tvBuyerName);
        TextView tvBuyerEmail = findViewById(R.id.tvBuyerEmail);

        tvBuyerName.setText(Session.getUsername(this));
        tvBuyerEmail.setText(Session.getEmail(this));
    }

    private void showOrder() {
        List<CartItem> items = AppDatabase.getCartItems(this);

        List<String> lines = new ArrayList<>();
        for (CartItem item : items) {
            lines.add(item.getProduct().getName()
                    + "  x" + item.getQuantity()
                    + "   " + Money.format(item.getSubtotal()));
        }

        ListView listOrder = findViewById(R.id.listOrder);
        listOrder.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, lines));

        total = AppDatabase.getCartTotal(this) + Constants.SHIPPING_FEE;
        TextView tvCheckoutTotal = findViewById(R.id.tvCheckoutTotal);
        tvCheckoutTotal.setText(Money.format(total));
    }

    private void confirmOrder() {
        if (AppDatabase.getCartCount(this) == 0) {
            Toast.makeText(this, R.string.cart_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase.createOrder(this, Session.getUsername(this), total);
        AppDatabase.clearCart(this);

        new AlertDialog.Builder(this)
                .setTitle(R.string.checkout_done_title)
                .setMessage(getString(R.string.checkout_done_message, Money.format(total)))
                .setCancelable(false)
                .setPositiveButton(R.string.checkout_done_ok, (dialog, which) -> backToHome())
                .show();
    }

    private void backToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
