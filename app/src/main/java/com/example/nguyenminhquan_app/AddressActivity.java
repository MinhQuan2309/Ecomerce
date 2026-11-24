package com.example.nguyenminhquan_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ToolbarHelper.setupBack(this);

        TextInputEditText edtReceiver = findViewById(R.id.edtReceiver);
        TextInputEditText edtPhone = findViewById(R.id.edtPhone);
        TextInputEditText edtCity = findViewById(R.id.edtCity);
        TextInputEditText edtDetail = findViewById(R.id.edtDetail);

        edtReceiver.setText(Session.getReceiver(this));
        edtPhone.setText(Session.getPhone(this));
        edtCity.setText(Session.getCity(this));
        edtDetail.setText(Session.getDetail(this));

        MaterialButton btnSaveAddress = findViewById(R.id.btnSaveAddress);
        btnSaveAddress.setOnClickListener(v -> {
            String receiver = edtReceiver.getText() != null ? edtReceiver.getText().toString().trim() : "";
            String phone = edtPhone.getText() != null ? edtPhone.getText().toString().trim() : "";
            String city = edtCity.getText() != null ? edtCity.getText().toString().trim() : "";
            String detail = edtDetail.getText() != null ? edtDetail.getText().toString().trim() : "";

            if (receiver.isEmpty()) {
                edtReceiver.setError("Vui lòng nhập tên người nhận");
                return;
            }
            if (phone.isEmpty()) {
                edtPhone.setError("Vui lòng nhập số điện thoại");
                return;
            }
            if (detail.isEmpty()) {
                edtDetail.setError("Vui lòng nhập địa chỉ cụ thể");
                return;
            }

            Session.saveAddress(this, receiver, phone, city, detail);
            Toast.makeText(this, "Đã lưu địa chỉ nhận hàng thành công", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        });
    }
}
