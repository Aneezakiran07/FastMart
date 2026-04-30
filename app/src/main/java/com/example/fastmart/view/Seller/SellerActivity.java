package com.example.fastmart.view.Seller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;
import com.example.fastmart.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;
import android.view.View;

public class SellerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navView;
    SessionManager sessionManager;
    Button btnLightTheme, btnDarkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        drawerLayout   = findViewById(R.id.drawerLayout);
        toolbar        = findViewById(R.id.toolbar);
        navView        = findViewById(R.id.navView);
        sessionManager = new SessionManager(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        // setup theme buttons from drawer header
        View headerView = navView.getHeaderView(0);
        btnLightTheme = headerView.findViewById(R.id.btnLightTheme);
        btnDarkTheme  = headerView.findViewById(R.id.btnDarkTheme);

        // show seller name and email from session in drawer header
        TextView tvDrawerName  = headerView.findViewById(R.id.tvDrawerName);
        TextView tvDrawerEmail = headerView.findViewById(R.id.tvDrawerEmail);
        tvDrawerName.setText(sessionManager.getName());
        tvDrawerEmail.setText(sessionManager.getEmail());

        btnLightTheme.setOnClickListener(v -> applyTheme(false));
        btnDarkTheme.setOnClickListener(v -> applyTheme(true));

        if (savedInstanceState == null) {
            loadFragment(new SellerHomeFragment());
            navView.setCheckedItem(R.id.nav_home);
        }
    }


    private void applyTheme(boolean isDark) {
        // save theme preference to sharedprefs so it persists across app restarts
        SharedPreferences prefs = getSharedPreferences("FastMartPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isDarkTheme", isDark).apply();

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(new SellerHomeFragment());
        } else if (id == R.id.nav_orders) {
            loadFragment(new SellerOrderHistoryFragment());
        } else if (id == R.id.nav_account) {
            loadFragment(new SellerAccountFragment());
        }

        drawerLayout.closeDrawers();
        return true;
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sellerFragmentContainer, fragment)
                .commit();
    }
}