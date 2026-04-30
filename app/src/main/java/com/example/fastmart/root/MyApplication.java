package com.example.fastmart.root;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.fastmart.models.Product;

import java.util.ArrayList;

public class MyApplication extends Application {

    public ArrayList<Product> masterProductList;

    @Override
    public void onCreate() {
        super.onCreate();
        masterProductList = new ArrayList<>();

        // apply saved theme on every app start so theme persists
        SharedPreferences prefs = getSharedPreferences("FastMartPrefs", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("isDarkTheme", false);

        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}