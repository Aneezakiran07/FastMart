package com.example.fastmart.view.Buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.adapter.FavouritesAdapter;
import com.example.fastmart.models.Product;
import com.example.fastmart.utils.DatabaseHelper;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {

    RecyclerView rvFavourites;
    TextView tvEmpty;
    ArrayList<Product> favouritesList;
    FavouritesAdapter favouritesAdapter;
    DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadFavourites();
    }

    @Override
    public void onResume() {
        super.onResume();
        // reload from sqlite every time user switches to this tab
        loadFavourites();
    }

    private void init(View view) {
        rvFavourites   = view.findViewById(R.id.rvFavourites);
        tvEmpty        = view.findViewById(R.id.tvEmpty);
        dbHelper       = new DatabaseHelper(requireContext());
        favouritesList = new ArrayList<>();

        favouritesAdapter = new FavouritesAdapter(
                requireContext(), favouritesList, this::onDeleteProduct, dbHelper);
        rvFavourites.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvFavourites.setAdapter(favouritesAdapter);
    }

    private void loadFavourites() {
        // fetch all favourites from sqlite and refresh the list
        favouritesList.clear();
        favouritesList.addAll(dbHelper.getAllFavourites());
        favouritesAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void onDeleteProduct(int position) {
        Product product = favouritesList.get(position);

        // delete from sqlite using model as unique key
        dbHelper.removeFavourite(product.getModel());

        favouritesList.remove(position);
        favouritesAdapter.notifyItemRemoved(position);
        updateEmptyState();
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