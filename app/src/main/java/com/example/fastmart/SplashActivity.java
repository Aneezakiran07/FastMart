package com.example.fastmart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {


    View get_tv_title, get_tv_tagline;
    ImageView get_iv_truck;
    Animation left_to_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        applyAnimation();

        new Handler().postDelayed(() -> {
            moveToHomePage();
        }, 3000);

    }

    private void moveToHomePage() {
        startActivity(new Intent(SplashActivity.this, HomePage.class));
        finish();
    }
    protected void applyAnimation(){
        get_iv_truck.setAnimation(left_to_right);

    }
    protected void init(){
        get_iv_truck=findViewById(R.id.iv_truck);
        get_tv_title=findViewById(R.id.tv_title);
        get_tv_tagline=findViewById(R.id.tv_tagline);
        left_to_right=AnimationUtils.loadAnimation(this,R.anim.left_to_right);

    }
}