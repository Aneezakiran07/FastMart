package com.example.fastmart.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.fastmart.R;
import com.example.fastmart.models.User;
import com.example.fastmart.utils.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileSetupFragment extends Fragment {

    EditText etName, etPhone, etCountry, etDob, etAddress;
    Spinner spinnerAccountType;
    RadioGroup rgGender;
    Button btnSaveProfile;

    DatabaseReference reference;
    SessionManager sessionManager;

    String userId, email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
        setupAccountTypeSpinner();

        // grab the userId and email passed from SignupFragment
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            email = getArguments().getString("email");
        }

        btnSaveProfile.setOnClickListener(v -> handleSaveProfile());
    }

    private void init(View view) {
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etCountry = view.findViewById(R.id.etCountry);
        etDob = view.findViewById(R.id.etDob);
        etAddress = view.findViewById(R.id.etAddress);
        spinnerAccountType = view.findViewById(R.id.spinnerAccountType);
        rgGender = view.findViewById(R.id.rgGender);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        sessionManager = new SessionManager(requireContext());
    }

    private void setupAccountTypeSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"Buyer", "Seller"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAccountType.setAdapter(adapter);
    }

    private void handleSaveProfile() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String dob = etDob.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String accountType = spinnerAccountType.getSelectedItem().toString();

        // figure out which gender radio button is selected
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        String gender = selectedGenderId == R.id.rbMale ? "Male" : "Female";

        if (name.isEmpty()) { etName.setError("Name is required"); return; }
        if (phone.isEmpty()) { etPhone.setError("Phone is required"); return; }
        if (country.isEmpty()) { etCountry.setError("Country is required"); return; }
        if (dob.isEmpty()) { etDob.setError("Date of birth is required"); return; }
        if (address.isEmpty()) { etAddress.setError("Address is required"); return; }
        if (selectedGenderId == -1) {
            Toast.makeText(requireContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSaveProfile.setEnabled(false);

        User user = new User(userId, name, email, address, gender, dob, phone, country, accountType);

        // push the full user object to firebase under their userId
        reference.child(userId).setValue(user)
                .addOnSuccessListener(unused -> {

                    // save to sharedprefs so user stays logged in next time
                    sessionManager.createLoginSession(userId, name, email, accountType);

                    // send buyer to MainActivity, seller to SellerActivity
                    if (accountType.equals("Seller")) {
                        startActivity(new Intent(requireActivity(), SellerActivity.class));
                    } else {
                        startActivity(new Intent(requireActivity(), MainActivity.class));
                    }

                    requireActivity().finish();
                })
                .addOnFailureListener(e -> {
                    btnSaveProfile.setEnabled(true);
                    Toast.makeText(requireContext(),
                            "Failed to save profile: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}