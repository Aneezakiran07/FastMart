package com.example.fastmart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomePage extends AppCompatActivity {


    View product1, product2, product3, product4,hotdeal;
    ImageView image1, image2, image3, image4,dealimage;
    TextView price1, price2, price3, price4,dealprice;
    TextView name1, name2, name3, name4,dealname;
    TextView model1, model2, model3, model4,dealcategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setTextInProductsCards();
        clickevent();
    }

    protected void init() {
        product1 = findViewById(R.id.card1);
        product2 = findViewById(R.id.card2);
        product3 = findViewById(R.id.card3);
        product4 = findViewById(R.id.card4);
        hotdeal= findViewById(R.id.dealOfTheDayCard);

        name1 = product1.findViewById(R.id.productName);
        name2 = product2.findViewById(R.id.productName);
        name3 = product3.findViewById(R.id.productName);
        name4 = product4.findViewById(R.id.productName);
        dealname=hotdeal.findViewById(R.id.dealName);

        image1 = product1.findViewById(R.id.productImage);
        image2 = product2.findViewById(R.id.productImage);
        image3 = product3.findViewById(R.id.productImage);
        image4 = product4.findViewById(R.id.productImage);
        dealimage=hotdeal.findViewById(R.id.dealImage);

        price1 = product1.findViewById(R.id.productPrice);
        price2 = product2.findViewById(R.id.productPrice);
        price3 = product3.findViewById(R.id.productPrice);
        price4 = product4.findViewById(R.id.productPrice);
        dealprice=hotdeal.findViewById(R.id.dealPrice);

        model1 = product1.findViewById(R.id.productModel);
        model2 = product2.findViewById(R.id.productModel);
        model3 = product3.findViewById(R.id.productModel);
        model4 = product4.findViewById(R.id.productModel);
        dealcategory=hotdeal.findViewById(R.id.dealCategory);
    }

    protected void setTextInProductsCards() {
        name1.setText("SONY Premium Wireless Headphones");
        price1.setText("$320.99");
        model1.setText("Model: WH-1000XM5, Black");
        image1.setImageResource(R.drawable.sony);

        name2.setText("SONY Premium Wireless Headphones");
        price2.setText("$349.99");
        model2.setText("Model: WH-1000XM4, Black");
        image2.setImageResource(R.drawable.sony);

        name3.setText("SONY Premium Wireless Headphones");
        price3.setText("$349.99");
        model3.setText("Model: WH-1000XM4, Beige");
        image3.setImageResource(R.drawable.sonybeige);

        name4.setText("SONY Premium Wired RODE PodMic");
        price4.setText("$108.20");
        model4.setText("Model: WH-1000XM7, Beige");
        image4.setImageResource(R.drawable.podemic);
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomePage.this, ProductActivity.class);
            int clickedId = v.getId();

            if (clickedId == R.id.card1) {
                intent.putExtra("name", name1.getText().toString());
                intent.putExtra("price", price1.getText().toString());
                intent.putExtra("model", model1.getText().toString());
                intent.putExtra("description", "The technology with two noise sensors and two microphones on each ear cup detects ambient noise and sends the data to the HD noise minimization processor QN1.");
                intent.putExtra("image", R.drawable.sony);

            } else if (clickedId == R.id.card2) {
                intent.putExtra("name", name2.getText().toString());
                intent.putExtra("price", price2.getText().toString());
                intent.putExtra("model", model2.getText().toString());
                intent.putExtra("description", "The technology with two noise sensors and two microphones on each ear cup detects ambient noise and sends the data to the HD noise minimization processor QN1.");
                intent.putExtra("image", R.drawable.sony);

            } else if (clickedId == R.id.card3) {
                intent.putExtra("name", name3.getText().toString());
                intent.putExtra("price", price3.getText().toString());
                intent.putExtra("model", model3.getText().toString());
                intent.putExtra("description", "The technology with two noise sensors and two microphones on each ear cup detects ambient noise and sends the data to the HD noise minimization processor QN1.");
                intent.putExtra("image", R.drawable.sonybeige);

            } else if (clickedId == R.id.card4) {
                intent.putExtra("name", name4.getText().toString());
                intent.putExtra("price", price4.getText().toString());
                intent.putExtra("model", model4.getText().toString());
                intent.putExtra("description", "Dynamic microphone with internal shock mounting. Designed for recording podcasts, vocals and instruments in studio environments.");
                intent.putExtra("image", R.drawable.podemic);
            } else if(clickedId==R.id.dealOfTheDayCard){
                intent.putExtra("name", dealname.getText().toString());
                intent.putExtra("price", dealprice.getText().toString());
                intent.putExtra("model", dealcategory.getText().toString());
                intent.putExtra("description", "Dynamic microphone with internal shock mounting. Designed for recording podcasts, vocals and instruments in studio environments.");
                intent.putExtra("image", R.drawable.podemic);

            }

            startActivity(intent);
        }
    };

    protected void clickevent() {
        product1.setOnClickListener(myListener);
        product2.setOnClickListener(myListener);
        product3.setOnClickListener(myListener);
        product4.setOnClickListener(myListener);
        hotdeal.setOnClickListener(myListener);
    }
}