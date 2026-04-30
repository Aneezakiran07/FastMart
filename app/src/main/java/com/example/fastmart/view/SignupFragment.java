package com.example.fastmart.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;
import com.example.fastmart.models.User;
import com.example.fastmart.utils.SessionManager;
import com.example.fastmart.view.Seller.SellerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupFragment extends Fragment {

    EditText etEmail, etPassword, etVerifyPassword, etFullName,
            etPhone, etDob, etAddress, etCountry;
    RadioGroup rgGender, rgAccountType;
    Button btnSignUp;
    FirebaseAuth auth;
    DatabaseReference dbRef;
    SessionManager sessionManager;

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
        etEmail          = view.findViewById(R.id.etEmail);
        etPassword       = view.findViewById(R.id.etPassword);
        etVerifyPassword = view.findViewById(R.id.etVerifyPassword);
        etFullName       = view.findViewById(R.id.etFullName);
        etPhone          = view.findViewById(R.id.etPhone);
        etDob            = view.findViewById(R.id.etDob);
        etAddress        = view.findViewById(R.id.etAddress);
        etCountry        = view.findViewById(R.id.etCountry);
        rgGender         = view.findViewById(R.id.rgGender);
        rgAccountType    = view.findViewById(R.id.rgAccountType);
        btnSignUp        = view.findViewById(R.id.btnSignUp);

        auth           = FirebaseAuth.getInstance();
        dbRef          = FirebaseDatabase.getInstance().getReference("users");
        sessionManager = new SessionManager(requireContext());
    }

    private void handleSignup() {
        String email       = etEmail.getText().toString().trim();
        String password    = etPassword.getText().toString().trim();
        String verify      = etVerifyPassword.getText().toString().trim();
        String fullName    = etFullName.getText().toString().trim();
        String phone       = etPhone.getText().toString().trim();
        String dob         = etDob.getText().toString().trim();
        String address     = etAddress.getText().toString().trim();
        String country     = etCountry.getText().toString().trim();

        int genderId  = rgGender.getCheckedRadioButtonId();
        int accountId = rgAccountType.getCheckedRadioButtonId();

        String gender      = (genderId == R.id.rbMale) ? "Male" : "Female";
        String accountType = (accountId == R.id.rbBuyer) ? "Buyer" : "Seller";

        if (email.isEmpty())    { etEmail.setError("Email is required");        return; }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email"); return; }
        if (password.isEmpty()) { etPassword.setError("Password is required");  return; }
        if (password.length() < 6) { etPassword.setError("Minimum 6 characters"); return; }
        if (!password.equals(verify)) { etVerifyPassword.setError("Passwords do not match"); return; }
        if (genderId == -1) {
            Toast.makeText(requireContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if (accountId == -1) {
            Toast.makeText(requireContext(), "Please select an account type", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSignUp.setEnabled(false);



        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String userId = authResult.getUser().getUid();
                    android.util.Log.d("SIGNUP_DEBUG", "Auth success userId: " + userId);

                    User newUser = new User(userId, fullName, email, address, gender, dob, phone, country, accountType);
                    android.util.Log.d("SIGNUP_DEBUG", "User object created: " + newUser.getName());

                    dbRef.child(userId).setValue(newUser)
                            .addOnSuccessListener(unused -> {
                                android.util.Log.d("SIGNUP_DEBUG", "DB write success");
                                if (!isAdded()) return;

                                // use session manager so all keys are consistent
                                sessionManager.createLoginSession(
                                        userId, fullName, email, accountType, phone);

                                Toast.makeText(requireActivity(),
                                        "Registration Successful", Toast.LENGTH_SHORT).show();

                                // route to correct screen based on account type
                                if (accountType.equals("Seller")) {
                                    startActivity(new Intent(requireActivity(),
                                            SellerActivity.class));
                                } else {
                                    startActivity(new Intent(requireActivity(),
                                            MainActivity.class));
                                }
                                requireActivity().finish();// rest of code
                            })
                            .addOnFailureListener(e -> {
                                android.util.Log.e("SIGNUP_DEBUG", "DB write FAILED: " + e.getMessage());
                                btnSignUp.setEnabled(true);
                                Toast.makeText(requireActivity(), "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("SIGNUP_DEBUG", "Auth FAILED: " + e.getMessage());
                    btnSignUp.setEnabled(true);
                    Toast.makeText(requireActivity(), "Auth error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}



