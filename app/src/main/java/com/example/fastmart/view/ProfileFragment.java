package com.example.fastmart.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;

public class ProfileFragment extends Fragment {

    TextView tvName, tvEmail, tvAddress, tvDob, tvGender, tvPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadUserInfo();
    }

    private void init(View view) {
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvDob = view.findViewById(R.id.tvDob);
        tvGender = view.findViewById(R.id.tvGender);
        tvPhone = view.findViewById(R.id.tvPhone);
    }

    // hardcoded uneditable values as per assignment
    private void loadUserInfo() {
        tvName.setText("Aneeza");
        tvEmail.setText("aneeza@gmail.com");
        tvAddress.setText("Lahore, Pakistan");
        tvDob.setText("01 Jan 3000");
        tvGender.setText("Female");
        tvPhone.setText("+92 300 1234567");
    }
}