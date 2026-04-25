package com.example.fastmart.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fastmart.view.LoginFragment;
import com.example.fastmart.view.SignupFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    // Constructor — just passes the activity to the parent class
    public ViewPagerAdapter(FragmentActivity activity) {
        super(activity);
    }

    // This method answers: "Which fragment goes on page number X?"
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new LoginFragment();
            case 1: return new SignupFragment();
            default: return new LoginFragment();
        }
    }

    // This method answers: "How many pages are there in total?"
    @Override
    public int getItemCount() {
        return 2;
    }
}