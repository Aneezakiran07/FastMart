package com.example.fastmart.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check login and onboarding state first
        SharedPreferences prefs = getSharedPreferences("FastMartPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("user.isLoggedIn", false);
        boolean isFirstTime = prefs.getBoolean("app.isFirstTime", true);

        if (!isLoggedIn && isFirstTime) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        } else if (!isLoggedIn) {
            startActivity(new Intent(this, LoginSignupActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        init();

        // show home fragment on launch
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        setupNavbar();
    }

    private void init() {
        bottomNav = findViewById(R.id.bottomNav);
    }

    private void setupNavbar() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return loadFragment(new HomeFragment());
            } else if (id == R.id.nav_search) {
                return loadFragment(new SearchFragment());
            } else if (id == R.id.nav_favourites) {
                return loadFragment(new FavouritesFragment());
            } else if (id == R.id.nav_cart) {
                return loadFragment(new CartFragment());
            } else if (id == R.id.nav_profile) {
                return loadFragment(new ProfileFragment());
            }
            return false;
        });
    }

    // swap fragment inside the container
    private boolean loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
        return true;
    }
}