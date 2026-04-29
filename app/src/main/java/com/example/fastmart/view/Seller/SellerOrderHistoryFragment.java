package com.example.fastmart.view.Seller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.adapter.OrderAdapter;
import com.example.fastmart.models.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerOrderHistoryFragment extends Fragment {

    RecyclerView rvOrders;
    DatabaseReference ordersRef;
    OrderAdapter orderAdapter;
    ArrayList<Order> orderList;

    public SellerOrderHistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_order_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadOrders();
    }

    private void init(View view) {
        rvOrders  = view.findViewById(R.id.rvOrders);
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        orderList = new ArrayList<>();

        rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        orderAdapter = new OrderAdapter(requireContext(), orderList);
        rvOrders.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        // fetch all orders from firebase in realtime
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Order order = child.getValue(Order.class);
                    if (order != null) orderList.add(order);
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(),
                        "Failed to load orders: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}