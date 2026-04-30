package com.example.fastmart.view.Buyer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.adapter.CartAdapter;
import com.example.fastmart.models.CartItem;
import com.example.fastmart.models.Order;
import com.example.fastmart.utils.DatabaseHelper;
import com.example.fastmart.utils.SessionManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartFragment extends Fragment {

    RecyclerView rvCart;
    TextView tvTotalPrice, tvEmpty;
    Button btnCheckout;

    CartAdapter cartAdapter;
    ArrayList<CartItem> cartItems;
    DatabaseHelper dbHelper;
    SessionManager sessionManager;
    DatabaseReference ordersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadCart();
        btnCheckout.setOnClickListener(v -> handleCheckout());
    }

    @Override
    public void onResume() {
        super.onResume();
        // reload from sqlite every time user switches to this tab
        loadCart();
    }

    private void init(View view) {
        rvCart         = view.findViewById(R.id.rvCart);
        tvTotalPrice   = view.findViewById(R.id.tvTotalPrice);
        tvEmpty        = view.findViewById(R.id.tvEmpty);
        btnCheckout    = view.findViewById(R.id.btnCheckout);

        dbHelper       = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());
        ordersRef      = FirebaseDatabase.getInstance().getReference("orders");
        cartItems      = new ArrayList<>();

        cartAdapter = new CartAdapter(requireContext(), cartItems,
                dbHelper, this::updateTotal);
        rvCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvCart.setAdapter(cartAdapter);
    }

    private void loadCart() {
        // fetch latest cart from sqlite and refresh ui
        cartItems.clear();
        cartItems.addAll(dbHelper.getAllCartItems());
        cartAdapter.notifyDataSetChanged();
        updateTotal();
    }

    public void updateTotal() {
        // always recalculate from sqlite so total is accurate
        double total = dbHelper.getCartTotal();
        tvTotalPrice.setText(String.format("$%.2f", total));
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (cartItems.isEmpty()) {
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

        ArrayList<CartItem> currentItems = dbHelper.getAllCartItems();
        double total = dbHelper.getCartTotal();

        // build sms summary from sqlite data
        StringBuilder smsText = new StringBuilder("FastMart Order:\n");
        for (CartItem item : currentItems) {
            double itemTotal = Double.parseDouble(
                    item.getProduct().getPrice().replace("$", ""))
                    * item.getQuantity();
            smsText.append(item.getProduct().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = $").append(String.format("%.2f", itemTotal))
                    .append("\n");
        }
        smsText.append("Total: $").append(String.format("%.2f", total));

        String phone = sessionManager.getPhone();
        if (phone == null || phone.isEmpty()) phone = "03000000000";

        try {
            SmsManager sms = SmsManager.getDefault();
            ArrayList<String> parts = sms.divideMessage(smsText.toString());
            sms.sendMultipartTextMessage(phone, null, parts, null, null);

            // save order to firebase so seller can view it
            saveOrderToFirebase(currentItems, total);

            // clear sqlite cart after successful checkout
            dbHelper.clearCart();
            cartItems.clear();
            cartAdapter.notifyDataSetChanged();
            updateTotal();

            Toast.makeText(requireContext(),
                    "Order placed! SMS sent.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(requireContext(),
                    "SMS failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveOrderToFirebase(ArrayList<CartItem> items, double total) {
        String orderId   = ordersRef.push().getKey();
        String buyerName = sessionManager.getName();
        String timestamp = new SimpleDateFormat("MMM dd, yyyy hh:mm a",
                Locale.getDefault()).format(new Date());

        Order order = new Order(orderId, buyerName, timestamp, total, items);

        // push order to firebase under unique id for seller to see
        ordersRef.child(orderId).setValue(order)
                .addOnFailureListener(e ->
                        Toast.makeText(requireContext(),
                                "Order save failed: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
    }
}