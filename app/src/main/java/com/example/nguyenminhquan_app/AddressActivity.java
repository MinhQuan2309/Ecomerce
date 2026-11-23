package com.example.nguyenminhquan_app;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        ToolbarHelper.setupBack(this);

        MaterialButton btnSaveAddress = findViewById(R.id.btnSaveAddress);
        btnSaveAddress.setOnClickListener(v ->
                Toast.makeText(this, R.string.feature_coming_soon, Toast.LENGTH_SHORT).show());
    }
}
