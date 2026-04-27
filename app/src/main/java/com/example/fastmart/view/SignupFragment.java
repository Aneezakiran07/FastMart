package com.example.fastmart.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {

    EditText etEmail, etPassword, etVerifyPassword;
    Button btnSignUp;
    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        btnSignUp.setOnClickListener(v -> handleSignup());
    }

    private void init(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etVerifyPassword = view.findViewById(R.id.etVerifyPassword);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        auth = FirebaseAuth.getInstance();
    }

    private void handleSignup() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String verifyPassword = etVerifyPassword.getText().toString().trim();


        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(verifyPassword)) {
            etVerifyPassword.setError("Passwords do not match");
            return;
        }

        btnSignUp.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = authResult.getUser().getUid();

                    // firebase account created, now collect profile info
                    ((LoginSignupActivity) requireActivity())
                            .navigateToProfileSetup(userId, email);
                })
                .addOnFailureListener(e -> {
                    btnSignUp.setEnabled(true);
                    Log.e("SignupFragment", "signup failed: " + e.getMessage());
                    Toast.makeText(requireActivity(),
                            "Signup failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}