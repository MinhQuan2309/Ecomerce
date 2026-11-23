package com.example.nguyenminhquan_app;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ToolbarHelper.setupBack(this);

        EditText edtName = findViewById(R.id.edtName);
        EditText edtEmail = findViewById(R.id.edtEmail);

        edtName.setText(Session.getUsername(this));
        edtEmail.setText(Session.getEmail(this));

        MaterialButton btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnSaveProfile.setOnClickListener(v ->
                Toast.makeText(this, R.string.feature_coming_soon, Toast.LENGTH_SHORT).show());
    }
}
