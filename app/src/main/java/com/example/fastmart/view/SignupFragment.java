package com.example.fastmart.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;

public class SignupFragment extends Fragment {

    EditText etEmail, etPassword, etVerifyPassword;
    Button btnSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        btnSignUp.setOnClickListener(v -> handleSignup());
    }

    private void init(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etVerifyPassword = view.findViewById(R.id.etVerifyPassword);
        btnSignUp = view.findViewById(R.id.btnSignUp);
    }

    //validates the users input, checks if the email already exists,
    // saves the new credentials to SharedPreferences, and logs the user in.
    private void handleSignup() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String verifyPassword = etVerifyPassword.getText().toString().trim();

        // validate fields
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

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("FastMartPrefs", requireActivity().MODE_PRIVATE);

        // check if email already registered
        String existingEmail = prefs.getString("user.email", "");
        if (existingEmail.equals(email)) {
            etEmail.setError("Email already registered");
            return;
        }

        // save credentials
        prefs.edit()
                .putString("user.email", email)
                .putString("user.password", password)
                .putBoolean("user.isLoggedIn", true)
                .apply();

        Toast.makeText(requireActivity(), "Account created!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();

    }
}