package com.example.fastmart.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;
import com.example.fastmart.utils.SessionManager;
import com.example.fastmart.view.Seller.SellerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.fastmart.models.User;

public class LoginFragment extends Fragment {

    EditText etEmail, etPassword;
    Button btnLogin;
    FirebaseAuth auth;
    DatabaseReference reference;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void init(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        sessionManager = new SessionManager(requireContext());
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

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

        btnLogin.setEnabled(false);

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = authResult.getUser().getUid();

                    // fetch user info from firebase to get accountType
                    reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);

                            if (user == null) {
                                btnLogin.setEnabled(true);
                                Toast.makeText(requireContext(),
                                        "User data not found",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // save session so user stays logged in next open
                            sessionManager.createLoginSession(
                                    userId,
                                    user.getName(),
                                    user.getEmail(),
                                    user.getAccountType()
                            );

                            // route to correct home based on account type
                            if (user.getAccountType().equals("Seller")) {
                                startActivity(new Intent(requireActivity(), SellerActivity.class));
                            } else {
                                startActivity(new Intent(requireActivity(), MainActivity.class));
                            }

                            requireActivity().finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            btnLogin.setEnabled(true);
                            Toast.makeText(requireContext(),
                                    "Database error: " + error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    btnLogin.setEnabled(true);
                    Toast.makeText(requireContext(),
                            "Login failed: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}