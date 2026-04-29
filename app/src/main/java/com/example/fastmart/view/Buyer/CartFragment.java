package com.example.fastmart.view.Buyer;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.root.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.adapter.CartAdapter;
import com.example.fastmart.models.CartItem;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    RecyclerView rvCart;
    TextView tvTotalPrice, tvEmpty;
    Button btnCheckout;
    CartAdapter cartAdapter;
    MyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = (MyApplication) requireActivity().getApplication();
        init(view);
        updateTotal();
        updateEmptyState();

        btnCheckout.setOnClickListener(v -> handleCheckout());
    }

    @Override
    public void onResume() {
        super.onResume();
        // refresh cart every time user opens this tab
        if (cartAdapter != null) {
            cartAdapter.notifyDataSetChanged();
            updateTotal();
        }
    }

    private void init(View view) {
        rvCart = view.findViewById(R.id.rvCart);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        cartAdapter = new CartAdapter(requireContext(), app, this::updateTotal);
        rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCart.setAdapter(cartAdapter);
    }

    public void updateTotal() {
        tvTotalPrice.setText(String.format("$%.2f", app.getCartTotal()));
        updateEmptyState();
        if (cartAdapter != null) cartAdapter.notifyDataSetChanged();
    }

    private void updateEmptyState() {
        if (app.getCartItems().isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvCart.setVisibility(View.GONE);
            btnCheckout.setEnabled(false);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvCart.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(true);
        }
    }

    private void handleCheckout() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.SEND_SMS}, 101);
            return;
        }

        // build order summary for sms
        StringBuilder order = new StringBuilder("FastMart Order:\n");
        for (CartItem item : app.getCartItems()) {
            double itemTotal = Double.parseDouble(
                    item.getProduct().getPrice().replace("$", "")) * item.getQuantity();
            order.append(item.getProduct().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = $").append(String.format("%.2f", itemTotal))
                    .append("\n");
        }
        order.append("Total: $")
                .append(String.format("%.2f", app.getCartTotal()));

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("FastMartPrefs", requireActivity().MODE_PRIVATE);
        String phone = prefs.getString("user.phone", "03224224164");

        if (phone.isEmpty()) {
            Toast.makeText(requireContext(),
                    "No phone number saved", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(order.toString());
            sms.sendMultipartTextMessage(phone, null, parts, null, null);

            Toast.makeText(requireContext(),
                    "Order placed! SMS sent.", Toast.LENGTH_SHORT).show();
            app.getCartItems().clear();
            cartAdapter.notifyDataSetChanged();
            updateTotal();
        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "SMS failed", Toast.LENGTH_SHORT).show();
        }
    }
}