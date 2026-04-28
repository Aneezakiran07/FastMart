package com.example.fastmart.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;
import com.example.fastmart.utils.SessionManager;
import com.example.fastmart.view.Seller.SellerAccountFragment;
import com.example.fastmart.view.Seller.SellerHomeFragment;
import com.example.fastmart.view.Seller.SellerOrderHistoryFragment;
import com.google.android.material.navigation.NavigationView;

public class SellerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navView;
    SessionManager sessionManager;

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

        // load home by default on first open
        if (savedInstanceState == null) {
            loadFragment(new SellerHomeFragment());
            navView.setCheckedItem(R.id.nav_home);
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