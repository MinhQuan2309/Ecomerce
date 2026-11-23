package com.example.nguyenminhquan_app;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseActivity extends AppCompatActivity {
    private ListView listView;
    private List<Product> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        ToolbarHelper.setupBack(this);

        listView = findViewById(R.id.listFirebase);
        listView.setOnItemClickListener((parent, view, position, id) -> showProduct(position));

        loadFromFirestore();
    }

    private void showProduct(int position) {
        Product p = products.get(position);

        new AlertDialog.Builder(this)
                .setTitle(p.getName())
                .setMessage(Money.format(p.getPrice()) + "\n\n" + p.getDescription())
                .setPositiveButton(R.string.firebase_dialog_close, null)
                .show();
    }

    private void loadFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products")
                .get()
                .addOnSuccessListener(snapshots -> {
                    products.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        products.add(readProduct(doc));
                    }
                    listView.setAdapter(new ProductAdapter(this, products));
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }

    private Product readProduct(QueryDocumentSnapshot doc) {
        String name = doc.getString("name");
        double price = doc.getDouble("price");
        String description = doc.getString("description");
        String imageName = doc.getString("image");

        return new Product(0, name, price, description, 0, imageName);
    }
}