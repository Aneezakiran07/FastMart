package com.example.fastmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.root.MyApplication;
import com.example.fastmart.R;
import com.example.fastmart.model.Product;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavViewHolder> {

    Context context;
    List<Product> favourites;
    OnDeleteListener deleteListener;
    MyApplication app;

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public FavouritesAdapter(Context context, List<Product> favourites,
                             OnDeleteListener deleteListener, MyApplication app) {
        this.context = context;
        this.favourites = favourites;
        this.deleteListener = deleteListener;
        this.app = app;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_favourite, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Product product = favourites.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());
        holder.tvModel.setText(product.getModel());
        holder.ivProduct.setImageResource(product.getImageRes());

        // triple dot shows delete confirmation dialog
        holder.ivMoreOptions.setOnClickListener(v ->
                new AlertDialog.Builder(context)
                        .setTitle("Remove Favourite")
                        .setMessage("Do you want to delete this product from favourites?")
                        .setPositiveButton("Yes", (dialog, which) ->
                                deleteListener.onDelete(holder.getAdapterPosition()))
                        .setNegativeButton("No", null)
                        .show());

        // cart icon adds to global cart via app
        holder.ivCart.setOnClickListener(v -> {
            app.addToCart(product);
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    static class FavViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivCart, ivMoreOptions;
        TextView tvName, tvPrice, tvModel;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            ivCart = itemView.findViewById(R.id.ivCart);
            ivMoreOptions = itemView.findViewById(R.id.ivMoreOptions);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvModel = itemView.findViewById(R.id.tvModel);
        }
    }
}