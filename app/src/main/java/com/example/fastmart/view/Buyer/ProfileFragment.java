package com.example.fastmart.view.Buyer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;
import com.example.fastmart.models.User;
import com.example.fastmart.utils.SessionManager;
import com.example.fastmart.view.LoginSignupActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    TextView tvName, tvPhone, tvCountry, tvDob, tvAddress, tvGender, tvAccountType;
    Button btnLogout;

    DatabaseReference reference;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        loadUserData();

        btnLogout.setOnClickListener(v -> handleLogout());
    }

    private void init(View view) {
        tvName        = view.findViewById(R.id.tvName);
        tvPhone       = view.findViewById(R.id.tvPhone);
        tvCountry     = view.findViewById(R.id.tvCountry);
        tvDob         = view.findViewById(R.id.tvDob);
        tvAddress     = view.findViewById(R.id.tvAddress);
        tvGender      = view.findViewById(R.id.tvGender);
        tvAccountType = view.findViewById(R.id.tvAccountType);
        btnLogout     = view.findViewById(R.id.btnLogout);

        reference      = FirebaseDatabase.getInstance().getReference("users");
        sessionManager = new SessionManager(requireContext());
    }

    private void loadUserData() {
        // get the userId stored in sharedprefs during login
        String userId = sessionManager.getUserId();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(requireContext(), "Session expired, please login again", Toast.LENGTH_SHORT).show();
            handleLogout();
            return;
        }

        // fetch user info from firebase using the userId
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user == null) {
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    return;
                }

                // populate all fields with data from firebase
                tvName.setText(user.getName());
                tvPhone.setText(user.getPhone());
                tvCountry.setText(user.getCountry());
                tvDob.setText(user.getDob());
                tvAddress.setText(user.getAddress());
                tvGender.setText(user.getGender());
                tvAccountType.setText(user.getAccountType());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(),
                        "Failed to load profile: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogout() {
        String userId = sessionManager.getUserId();

        // remove user data from firebase realtime db
        if (userId != null && !userId.isEmpty()) {
            reference.child(userId).removeValue();
        }

        // sign out from firebase auth
        FirebaseAuth.getInstance().signOut();

        // clear sharedprefs session
        sessionManager.logoutUser();

        // send user back to login screen and clear back stack
        Intent intent = new Intent(requireActivity(), LoginSignupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}