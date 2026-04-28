package com.example.fastmart.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastmart.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("FastMartPrefs", MODE_PRIVATE);
        boolean isFirstTime = prefs.getBoolean("app.isFirstTime", true);

        // show onboarding only on very first app open ever
        if (isFirstTime) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }

        SessionManager sessionManager = new SessionManager(this);

        // skip login screen if user is already logged in
        if (sessionManager.isLoggedIn()) {
            String accountType = sessionManager.getAccountType();

            if ("Seller".equals(accountType)) {
                startActivity(new Intent(this, SellerActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
        } else {
            startActivity(new Intent(this, LoginSignupActivity.class));
        }

        finish();
    }
}