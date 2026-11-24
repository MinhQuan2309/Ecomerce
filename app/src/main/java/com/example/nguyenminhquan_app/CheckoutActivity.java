package com.example.nguyenminhquan_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvReceiverNamePhone;
    private TextView tvReceiverEmail;
    private TextView tvReceiverAddress;

    private TextView tvItemCountBadge;
    private RecyclerView rvCheckoutProducts;

    private RadioGroup rgShipping;
    private RadioButton rbShippingStandard;
    private RadioButton rbShippingExpress;

    private RadioGroup rgPayment;
    private RadioButton rbPaymentCod;
    private RadioButton rbPaymentMomo;
    private RadioButton rbPaymentBank;
    private RadioButton rbPaymentCard;

    private TextInputEditText edtVoucherCode;
    private MaterialButton btnApplyVoucher;
    private TextView tvVoucherMessage;
    private TextView chipGiam10;
    private TextView chipFreeship;
    private TextView chipSale50k;

    private TextInputEditText edtOrderNote;

    private TextView tvSummarySubtotal;
    private TextView tvSummaryShipping;
    private View rowDiscount;
    private TextView tvSummaryDiscount;
    private TextView tvSummaryTotal;
    private TextView tvBottomTotal;
    private MaterialButton btnConfirmOrder;

    private List<CartItem> cartItems;
    private double subtotal = 0;
    private double shippingFee = 30000;
    private double discount = 0;
    private double total = 0;
    private String appliedVoucher = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        ToolbarHelper.setupBack(this);

        initViews();
        loadCart();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddress();
    }

    private void initViews() {
        tvReceiverNamePhone = findViewById(R.id.tvReceiverNamePhone);
        tvReceiverEmail = findViewById(R.id.tvReceiverEmail);
        tvReceiverAddress = findViewById(R.id.tvReceiverAddress);

        MaterialCardView cardAddress = findViewById(R.id.cardAddress);
        TextView btnChangeAddress = findViewById(R.id.btnChangeAddress);
        View.OnClickListener openAddress = v -> {
            Intent intent = new Intent(this, AddressActivity.class);
            startActivity(intent);
        };
        cardAddress.setOnClickListener(openAddress);
        btnChangeAddress.setOnClickListener(openAddress);

        tvItemCountBadge = findViewById(R.id.tvItemCountBadge);
        rvCheckoutProducts = findViewById(R.id.rvCheckoutProducts);
        rvCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));

        rgShipping = findViewById(R.id.rgShipping);
        rbShippingStandard = findViewById(R.id.rbShippingStandard);
        rbShippingExpress = findViewById(R.id.rbShippingExpress);

        rgPayment = findViewById(R.id.rgPayment);
        rbPaymentCod = findViewById(R.id.rbPaymentCod);
        rbPaymentMomo = findViewById(R.id.rbPaymentMomo);
        rbPaymentBank = findViewById(R.id.rbPaymentBank);
        rbPaymentCard = findViewById(R.id.rbPaymentCard);

        edtVoucherCode = findViewById(R.id.edtVoucherCode);
        btnApplyVoucher = findViewById(R.id.btnApplyVoucher);
        tvVoucherMessage = findViewById(R.id.tvVoucherMessage);
        chipGiam10 = findViewById(R.id.chipGiam10);
        chipFreeship = findViewById(R.id.chipFreeship);
        chipSale50k = findViewById(R.id.chipSale50k);

        edtOrderNote = findViewById(R.id.edtOrderNote);

        tvSummarySubtotal = findViewById(R.id.tvSummarySubtotal);
        tvSummaryShipping = findViewById(R.id.tvSummaryShipping);
        rowDiscount = findViewById(R.id.rowDiscount);
        tvSummaryDiscount = findViewById(R.id.tvSummaryDiscount);
        tvSummaryTotal = findViewById(R.id.tvSummaryTotal);
        tvBottomTotal = findViewById(R.id.tvBottomTotal);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
    }

    private void loadAddress() {
        String receiver = Session.getReceiver(this);
        String phone = Session.getPhone(this);
        String email = Session.getEmail(this);
        String address = Session.getFullAddress(this);

        tvReceiverNamePhone.setText(receiver + " • " + phone);
        tvReceiverEmail.setText("Email: " + email);
        tvReceiverAddress.setText(address);
    }

    private void loadCart() {
        cartItems = AppDatabase.getCartItems(this);
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvItemCountBadge.setText(cartItems.size() + " món");
        rvCheckoutProducts.setAdapter(new CheckoutProductAdapter(this, cartItems));

        subtotal = AppDatabase.getCartTotal(this);
        shippingFee = 30000;
        recalculateTotals();
    }

    private void setupListeners() {
        rgShipping.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbShippingExpress) {
                shippingFee = 50000;
            } else {
                shippingFee = 30000;
            }
            if ("FREESHIP".equalsIgnoreCase(appliedVoucher)) {
                discount = shippingFee;
            }
            recalculateTotals();
        });

        btnApplyVoucher.setOnClickListener(v -> {
            String code = edtVoucherCode.getText() != null ? edtVoucherCode.getText().toString().trim().toUpperCase() : "";
            applyVoucherCode(code);
        });

        chipGiam10.setOnClickListener(v -> {
            edtVoucherCode.setText("GIAM10");
            applyVoucherCode("GIAM10");
        });

        chipFreeship.setOnClickListener(v -> {
            edtVoucherCode.setText("FREESHIP");
            applyVoucherCode("FREESHIP");
        });

        chipSale50k.setOnClickListener(v -> {
            edtVoucherCode.setText("SALE50K");
            applyVoucherCode("SALE50K");
        });

        btnConfirmOrder.setOnClickListener(v -> processCheckout());
    }

    private void applyVoucherCode(String code) {
        if (code.isEmpty()) {
            discount = 0;
            appliedVoucher = "";
            tvVoucherMessage.setVisibility(View.GONE);
            recalculateTotals();
            return;
        }

        if ("GIAM10".equalsIgnoreCase(code)) {
            discount = subtotal * 0.10;
            appliedVoucher = "GIAM10";
            tvVoucherMessage.setText("✓ Đã áp dụng mã GIAM10: Giảm 10% (-" + Money.format(discount) + ")");
            tvVoucherMessage.setTextColor(getResources().getColor(R.color.status_text, getTheme()));
            tvVoucherMessage.setVisibility(View.VISIBLE);
        } else if ("FREESHIP".equalsIgnoreCase(code)) {
            discount = shippingFee;
            appliedVoucher = "FREESHIP";
            tvVoucherMessage.setText("✓ Đã áp dụng mã FREESHIP: Miễn phí vận chuyển (-" + Money.format(discount) + ")");
            tvVoucherMessage.setTextColor(getResources().getColor(R.color.status_text, getTheme()));
            tvVoucherMessage.setVisibility(View.VISIBLE);
        } else if ("SALE50K".equalsIgnoreCase(code)) {
            discount = Math.min(50000, subtotal);
            appliedVoucher = "SALE50K";
            tvVoucherMessage.setText("✓ Đã áp dụng mã SALE50K: Giảm ngay 50.000 đ");
            tvVoucherMessage.setTextColor(getResources().getColor(R.color.status_text, getTheme()));
            tvVoucherMessage.setVisibility(View.VISIBLE);
        } else {
            discount = 0;
            appliedVoucher = "";
            tvVoucherMessage.setText("✕ Mã giảm giá không hợp lệ hoặc đã hết hạn");
            tvVoucherMessage.setTextColor(getResources().getColor(R.color.danger, getTheme()));
            tvVoucherMessage.setVisibility(View.VISIBLE);
        }

        recalculateTotals();
    }

    private void recalculateTotals() {
        total = Math.max(0, subtotal + shippingFee - discount);

        tvSummarySubtotal.setText(Money.format(subtotal));
        tvSummaryShipping.setText(Money.format(shippingFee));

        if (discount > 0) {
            rowDiscount.setVisibility(View.VISIBLE);
            tvSummaryDiscount.setText("- " + Money.format(discount));
        } else {
            rowDiscount.setVisibility(View.GONE);
        }

        tvSummaryTotal.setText(Money.format(total));
        tvBottomTotal.setText(Money.format(total));
    }

    private void processCheckout() {
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng rỗng, không thể đặt hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        String receiver = Session.getReceiver(this);
        String phone = Session.getPhone(this);
        String address = Session.getFullAddress(this);

        if (receiver.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ địa chỉ và số điện thoại người nhận", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AddressActivity.class);
            startActivity(intent);
            return;
        }

        String shippingMethod = rbShippingExpress.isChecked() ?
                "Giao hàng hỏa tốc 2H" : "Giao hàng tiêu chuẩn (2-3 ngày)";

        String paymentMethod = "Thanh toán khi nhận hàng (COD)";
        if (rbPaymentMomo.isChecked()) {
            paymentMethod = "Ví điện tử MoMo";
        } else if (rbPaymentBank.isChecked()) {
            paymentMethod = "Chuyển khoản VietQR";
        } else if (rbPaymentCard.isChecked()) {
            paymentMethod = "Thẻ ATM / Visa / Mastercard";
        }

        String note = edtOrderNote.getText() != null ? edtOrderNote.getText().toString().trim() : "";

        String orderCode = "DH" + (System.currentTimeMillis() % 10000000);

        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setUsername(Session.getUsername(this));
        order.setReceiverName(receiver);
        order.setReceiverPhone(phone);
        order.setReceiverEmail(Session.getEmail(this));
        order.setReceiverAddress(address);
        order.setShippingMethod(shippingMethod);
        order.setPaymentMethod(paymentMethod);
        order.setNote(note);
        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setDiscount(discount);
        order.setTotal(total);
        order.setStatus("Đã đặt hàng thành công");
        order.setCreatedAt(System.currentTimeMillis());

        long orderId = AppDatabase.createOrderFull(this, order, cartItems);

        AppDatabase.clearCart(this);

        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order_id", orderId);
        intent.putExtra("from_checkout", true);
        startActivity(intent);
        finish();
    }
}
