package com.example.fastmart.view.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.adapter.ProductAdapter;
import com.example.fastmart.models.Product;
import com.example.fastmart.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerHomeFragment extends Fragment {

    RecyclerView rvProducts;
    FloatingActionButton fabAddProduct;
    TextView tvSellerName;

    DatabaseReference productsRef;
    ProductAdapter productAdapter;
    ArrayList<Product> productList;
    SessionManager sessionManager;

    public SellerHomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seller_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadProducts();
        fabAddProduct.setOnClickListener(v ->
                startActivity(new Intent(requireActivity(), SellerAddActivity.class)));
    }

    private void init(View view) {
        rvProducts    = view.findViewById(R.id.rvProducts);
        fabAddProduct = view.findViewById(R.id.fabAddProduct);
        tvSellerName  = view.findViewById(R.id.tvSellerName);

        sessionManager = new SessionManager(requireContext());
        productsRef    = FirebaseDatabase.getInstance().getReference("products");
        productList    = new ArrayList<>();

        tvSellerName.setText("Hello " + sessionManager.getName());

        rvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        productAdapter = new ProductAdapter(requireContext(), productList);
        rvProducts.setAdapter(productAdapter);
    }

    private void loadProducts() {
        // listen for realtime changes so new products show instantly
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Product product = child.getValue(Product.class);
                    if (product != null) {
                        product.setProductId(child.getKey());
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
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