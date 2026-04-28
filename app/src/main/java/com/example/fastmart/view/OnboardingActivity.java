package com.example.fastmart.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fastmart.R;

public class OnboardingActivity extends AppCompatActivity {

    Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        btnGetStarted.setOnClickListener(v -> {
            // mark that onboarding has been seen so it never shows again
            SharedPreferences prefs = getSharedPreferences("FastMartPrefs", MODE_PRIVATE);
            prefs.edit().putBoolean("app.isFirstTime", false).apply();

            startActivity(new Intent(OnboardingActivity.this, LoginSignupActivity.class));
            finish();
        });
    }

    private void init() {
        btnGetStarted = findViewById(R.id.btnGetStarted);
    }
}