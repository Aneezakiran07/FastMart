package com.example.fastmart.view.Buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.adapter.DealAdapter;
import com.example.fastmart.adapter.ProductAdapter;
import com.example.fastmart.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ListView lvDeals;
    RecyclerView rvRecommended;

    DatabaseReference productsRef;
    ArrayList<Product> productList;
    ArrayList<Product> dealList;
    ProductAdapter productAdapter;
    DealAdapter dealAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadProductsFromFirebase();
    }

    private void init(View view) {
        lvDeals       = view.findViewById(R.id.lvDeals);
        rvRecommended = view.findViewById(R.id.rvRecommended);

        productsRef = FirebaseDatabase.getInstance().getReference("products");
        productList = new ArrayList<>();
        dealList    = new ArrayList<>();

        // setup adapters with empty lists first, they update when firebase responds
        rvRecommended.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        productAdapter = new ProductAdapter(requireContext(), productList);
        rvRecommended.setAdapter(productAdapter);

        dealAdapter = new DealAdapter(requireContext(), dealList);
        lvDeals.setAdapter(dealAdapter);
    }

    private void loadProductsFromFirebase() {
        // valueEventListener fires instantly and on every future change
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                dealList.clear();

                for (DataSnapshot child : snapshot.getChildren()) {
                    Product product = child.getValue(Product.class);
                    if (product != null) {
                        product.setProductId(child.getKey());
                        productList.add(product);
                    }
                }

                // use first 3 products as deals, rest go to recommended grid
                dealList.addAll(productList.subList(0,
                        Math.min(3, productList.size())));

                productAdapter.notifyDataSetChanged();
                dealAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(),
                        "Failed to load products: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}