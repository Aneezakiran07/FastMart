package com.example.fastmart.view.Seller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastmart.R;
import com.example.fastmart.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerAddActivity extends AppCompatActivity {

    EditText etProductName, etProductType, etProductPrice, etProductDescription;
    Button btnAddProduct;
    DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_add);

        init();
        btnAddProduct.setOnClickListener(v -> handleAddProduct());
    }

    private void init() {
        etProductName        = findViewById(R.id.etProductName);
        etProductType        = findViewById(R.id.etProductType);
        etProductPrice       = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        btnAddProduct        = findViewById(R.id.btnAddProduct);
        productsRef          = FirebaseDatabase.getInstance().getReference("products");
    }

    private void handleAddProduct() {
        String name        = etProductName.getText().toString().trim();
        String type        = etProductType.getText().toString().trim();
        String price       = etProductPrice.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim();

        if (name.isEmpty())        { etProductName.setError("Name is required");               return; }
        if (type.isEmpty())        { etProductType.setError("Type is required");               return; }
        if (price.isEmpty())       { etProductPrice.setError("Price is required");             return; }
        if (description.isEmpty()) { etProductDescription.setError("Description is required"); return; }

        btnAddProduct.setEnabled(false);

        // hardcode a default image since seller cannot upload images yet
        Product product = new Product(name, "$" + price, "", type, description, R.drawable.sony);

        // push generates a unique firebase key for each product
        String productId = productsRef.push().getKey();
        product.setProductId(productId);

        productsRef.child(productId).setValue(product)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    btnAddProduct.setEnabled(true);
                    Toast.makeText(this,
                            "Failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}