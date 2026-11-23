package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ListView listCart;
    private View layoutEmpty;
    private TextView tvCartTotal;
    private MaterialButton btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        ToolbarHelper.setupBack(this);

        listCart = findViewById(R.id.listCart);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tvCartTotal = findViewById(R.id.tvCartTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        btnCheckout.setOnClickListener(v ->
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class)));

        MaterialButton btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v -> finish());

        BottomNavigationHelper.setup(this, BottomNavigationHelper.TAB_CART);

        MaterialButton btnStartShopping = findViewById(R.id.btnStartShopping);
        btnStartShopping.setOnClickListener(v -> finish());

        loadCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

    public void changeQuantity(int productId, int delta) {
        AppDatabase.changeQuantity(this, productId, delta);
        loadCart();
    }

    private void loadCart() {
        List<CartItem> items = AppDatabase.getCartItems(this);
        listCart.setAdapter(new CartAdapter(this, items));

        boolean empty = items.isEmpty();
        listCart.setVisibility(empty ? View.GONE : View.VISIBLE);
        layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        btnCheckout.setEnabled(!empty);
        findViewById(R.id.cardBottom).setVisibility(empty ? View.GONE : View.VISIBLE);

        double subtotal = AppDatabase.getCartTotal(this);
        double shipping = empty ? 0 : Constants.SHIPPING_FEE;

        TextView tvSummarySubtotal = findViewById(R.id.tvSummarySubtotal);
        TextView tvSummaryShipping = findViewById(R.id.tvSummaryShipping);

        tvSummarySubtotal.setText(Money.format(subtotal));
        tvSummaryShipping.setText(Money.format(shipping));
        tvCartTotal.setText(Money.format(subtotal + shipping));

        BottomNavigationHelper.updateCartBadge(this);
    }
}
