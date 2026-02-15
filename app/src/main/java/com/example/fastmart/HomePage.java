package com.example.fastmart;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    View product1,product2,product3,product4;
    ImageView image1,image2,image3,image4;
    TextView price1,price2,price3,price4;
    TextView name1,name2,name3,name4;
    TextView model1,model2,model3,model4;

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

    }


    protected void init(){
        product1=findViewById(R.id.card1);
        product2=findViewById(R.id.card2);
        product3=findViewById(R.id.card3);
        product4=findViewById(R.id.card4);

        name1=product1.findViewById(R.id.productName);
        name2=product2.findViewById(R.id.productName);
        name3=product3.findViewById(R.id.productName);
        name4=product4.findViewById(R.id.productName);

        image1=product1.findViewById(R.id.productImage);
        image2=product2.findViewById(R.id.productImage);
        image3=product3.findViewById(R.id.productImage);
        image4=product4.findViewById(R.id.productImage);

        price1=product1.findViewById(R.id.productPrice);
        price2=product2.findViewById(R.id.productPrice);
        price3=product3.findViewById(R.id.productPrice);
        price4=product4.findViewById(R.id.productPrice);

        model1=product1.findViewById(R.id.productModel);
        model2=product2.findViewById(R.id.productModel);
        model3=product3.findViewById(R.id.productModel);
        model4=product4.findViewById(R.id.productModel);

    }
    protected void setTextInProductsCards(){
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

    protected void OnClickListener(){

    }
}

