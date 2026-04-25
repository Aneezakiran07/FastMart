package com.example.fastmart.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.root.MyApplication;
import com.example.fastmart.adapter.ProductAdapter;
import com.example.fastmart.R;
import com.example.fastmart.adapter.DealAdapter;
import com.example.fastmart.models.Product;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ListView lvDeals;
    RecyclerView rvRecommended;
    MyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = (MyApplication) requireActivity().getApplication();
        init(view);
        setupDeals();
        setupRecommended();
    }

    private void init(View view) {
        lvDeals = view.findViewById(R.id.lvDeals);
        rvRecommended = view.findViewById(R.id.rvRecommended);
    }

    private void setupDeals() {

        ArrayList<Product> dealList = new ArrayList<>(
                app.masterProductList.subList(0, 3));

        lvDeals.setAdapter(new DealAdapter(requireContext(), dealList));
    }

    private void setupRecommended() {
        rvRecommended.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvRecommended.setAdapter(new ProductAdapter(
                requireContext(), app.masterProductList));
    }
}