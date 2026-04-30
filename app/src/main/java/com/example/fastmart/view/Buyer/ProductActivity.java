package com.example.fastmart.view.Buyer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fastmart.R;
import com.example.fastmart.models.Product;
import com.example.fastmart.root.MyApplication;
import com.example.fastmart.utils.DatabaseHelper;

public class ProductActivity extends AppCompatActivity {

    String name, price, category, description;
    int image;

    TextView productName, productPrice, productModel, productDescription, backButton;
    ImageView productImage;
    Button buyNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        getProductDetails();
        setProductDetails();
        setClickListeners();
    }

    private void init() {
        productName        = findViewById(R.id.productName);
        productPrice       = findViewById(R.id.productPrice);
        productModel       = findViewById(R.id.productModel);
        productDescription = findViewById(R.id.productDescription);
        productImage       = findViewById(R.id.productImage);
        buyNowButton       = findViewById(R.id.buyNowButton);
        backButton         = findViewById(R.id.backButton);
    }

    private void getProductDetails() {
        Intent intent = getIntent();
        name        = intent.getStringExtra("name");
        price       = intent.getStringExtra("price");
        category    = intent.getStringExtra("category");
        description = intent.getStringExtra("description");
        image       = intent.getIntExtra("image", R.drawable.sony);
    }

    private void setProductDetails() {
        productName.setText(name);
        productPrice.setText(price);
        productModel.setText(category);
        productDescription.setText(description);

        // fall back to default image if none was passed
        if (image != 0) {
            productImage.setImageResource(image);
        } else {
            productImage.setImageResource(R.drawable.sony);
        }
    }

    private void setClickListeners() {
        backButton.setOnClickListener(v -> finish());

        buyNowButton.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Buy Now")
                        .setMessage("Are you sure you want to buy " + name + "?")
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Confirm", (dialog, which) -> addToCart())
                        .show());
    }

    private void addToCart() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Product product = new Product(name, price, "", category, description, image);
        dbHelper.addToCart(product);
        Toast.makeText(this, name + " added to cart", Toast.LENGTH_SHORT).show();
    }
}