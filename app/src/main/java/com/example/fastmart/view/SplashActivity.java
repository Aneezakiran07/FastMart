package com.example.fastmart.view;

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
import android.content.SharedPreferences;

import com.example.fastmart.R;

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
        SharedPreferences prefs = getSharedPreferences("FastMartPrefs", MODE_PRIVATE);

        boolean isFirstTime = prefs.getBoolean("app.isFirstTime", true);
        boolean isLoggedIn = prefs.getBoolean("user.isLoggedIn", false);

        if (isFirstTime) {
            // first time opening app, goto onboarding
            startActivity(new Intent(SplashActivity.this, OnboardingActivity.class));
        }
        else if (isLoggedIn) {
            // is logged in, goto main screen
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        else {
            //been here before, but NOT logged in, goto Login/Signup
            startActivity(new Intent(SplashActivity.this, LoginSignupActivity.class));
        }

        // close splash screen so pressing back wont take us to splash
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