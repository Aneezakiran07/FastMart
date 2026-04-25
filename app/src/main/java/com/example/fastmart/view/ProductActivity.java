package com.example.fastmart.view;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.app.AlertDialog;
import android.widget.Toast;

import com.example.fastmart.root.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.model.Product;

public class ProductActivity extends AppCompatActivity {

    String name, price, model, description;
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
        getproductsdetails();
        setproductdetails();
        setClickListeners();
    }

    protected void getproductsdetails() {
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        model = intent.getStringExtra("model");
        description = intent.getStringExtra("description");
        image = intent.getIntExtra("image", 0);
    }

    protected void init() {
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productModel = findViewById(R.id.productModel);
        productDescription = findViewById(R.id.productDescription);
        productImage = findViewById(R.id.productImage);
        buyNowButton = findViewById(R.id.buyNowButton);
        backButton = findViewById(R.id.backButton);
    }

    protected void setproductdetails() {
        productName.setText(name);
        productPrice.setText(price);
        productModel.setText(model);
        productDescription.setText(description);
        productImage.setImageResource(image);
    }

    protected void setClickListeners() {
        backButton.setOnClickListener(v -> finish());

        buyNowButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Buy Now")
                    .setMessage("Are you sure you want to buy " + name + "?")
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Confirm", (dialog, which) -> addToCart())
                    .show();
        });
    }

    protected void addToCart() {
        MyApplication app = (MyApplication) getApplicationContext();

        Product p = new Product(name, price, "", model, description, image);

        app.addToCart(p);

        Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
    }
}