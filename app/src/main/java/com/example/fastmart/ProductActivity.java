package com.example.fastmart;

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
import android.content.Intent;

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



}
