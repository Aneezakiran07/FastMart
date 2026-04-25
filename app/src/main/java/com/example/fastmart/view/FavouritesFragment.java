package com.example.fastmart.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.root.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.adapter.FavouritesAdapter;
import com.example.fastmart.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    RecyclerView rvFavourites;
    TextView tvEmpty;
    List<Product> favouritesList;
    FavouritesAdapter favouritesAdapter;
    MyApplication app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = (MyApplication) requireActivity().getApplication();
        init(view);
        loadFavourites();
    }

    @Override
    public void onResume() {
        super.onResume();
        // reload every time user switches to this tab
        favouritesList.clear();
        loadFavourites();
    }

    private void init(View view) {
        rvFavourites = view.findViewById(R.id.rvFavourites);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        favouritesList = new ArrayList<>();

        favouritesAdapter = new FavouritesAdapter(
                requireContext(), favouritesList, this::onDeleteProduct, app);
        rvFavourites.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFavourites.setAdapter(favouritesAdapter);
    }

    private void loadFavourites() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("FastMartPrefs", requireActivity().MODE_PRIVATE);
        String json = prefs.getString("user.favourites", null);

        if (json != null) {
            Type type = new TypeToken<List<Product>>() {}.getType();
            List<Product> saved = new Gson().fromJson(json, type);
            if (saved != null) favouritesList.addAll(saved);
        }

        updateEmptyState();
        favouritesAdapter.notifyDataSetChanged();
    }

    private void onDeleteProduct(int position) {
        favouritesList.remove(position);
        favouritesAdapter.notifyItemRemoved(position);
        saveFavourites();
        updateEmptyState();
    }

    private void saveFavourites() {
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("FastMartPrefs", requireActivity().MODE_PRIVATE);
        prefs.edit()
                .putString("user.favourites", new Gson().toJson(favouritesList))
                .apply();
    }

    private void updateEmptyState() {
        if (favouritesList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvFavourites.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvFavourites.setVisibility(View.VISIBLE);
        }
    }
}