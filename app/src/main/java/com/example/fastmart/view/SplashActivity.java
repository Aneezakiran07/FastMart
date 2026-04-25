package com.example.fastmart.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastmart.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);

        // skip login screen if user already logged in previously
        if (sessionManager.isLoggedIn()) {
            String accountType = sessionManager.getAccountType();

            if (accountType.equals("Seller")) {
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