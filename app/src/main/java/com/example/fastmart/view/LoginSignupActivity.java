package com.example.fastmart.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fastmart.R;
import com.example.fastmart.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.view.View;
import android.view.ViewGroup;

public class LoginSignupActivity extends AppCompatActivity {

    ViewPagerAdapter adapter;
    ViewPager2 viewPager2;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setupTabs();
        setupPageChangeListener();
    }

    private void init() {
        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);
    }

    private void setupTabs() {
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Login"); break;
                case 1: tab.setText("Sign Up"); break;
            }
        }).attach();
    }

    private void setupPageChangeListener() {
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewPager2.postDelayed(() -> updateViewPagerHeight(position), 100);
            }
        });
    }

    private void updateViewPagerHeight(int position) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag("f" + position);

        if (fragment == null || fragment.getView() == null) return;

        View child = fragment.getView();
        child.measure(
                View.MeasureSpec.makeMeasureSpec(viewPager2.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );

        ViewGroup.LayoutParams params = viewPager2.getLayoutParams();
        params.height = child.getMeasuredHeight();
        viewPager2.setLayoutParams(params);
    }

    // signup fragment calls this after firebase creates the user
    // it hides the tablayout and viewpager and shows the profile setup screen
    public void navigateToProfileSetup(String userId, String email) {
        tabLayout.setVisibility(View.GONE);
        viewPager2.setVisibility(View.GONE);

        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("email", email);

        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
