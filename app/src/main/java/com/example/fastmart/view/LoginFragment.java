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

public class LoginFragment extends Fragment {

    EditText etEmail, etPassword;
    Button btnLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void init(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
    }

    private void handleLogin() {
        String enteredEmail = etEmail.getText().toString().trim();
        String enteredPassword = etPassword.getText().toString().trim();

        // check empty fields
        if (enteredEmail.isEmpty()) {
            etEmail.setError("Email is required");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(enteredEmail).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }
        if (enteredPassword.isEmpty()) {
            etPassword.setError("Password is required");
            return;
        }

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("FastMartPrefs", requireActivity().MODE_PRIVATE);

        // fetch saved credentials from signup
        String savedEmail = prefs.getString("user.email", "");
        String savedPassword = prefs.getString("user.password", "");

        if (enteredEmail.equals(savedEmail) && enteredPassword.equals(savedPassword)) {
            // save login state so user stays logged in
            prefs.edit().putBoolean("user.isLoggedIn", true).apply();


            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        } else {
            Toast.makeText(requireActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}