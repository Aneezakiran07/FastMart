package com.example.fastmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.models.Product;
import com.example.fastmart.utils.DatabaseHelper;
import com.example.fastmart.view.Buyer.ProductActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<Product> products;
    DatabaseHelper dbHelper;

    public ProductAdapter(Context context, List<Product> products) {
        this.context  = context;
        this.products = products;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productModel.setText(product.getModel());

        holder.productImage.setImageResource(product.getImageRes() != 0
                ? product.getImageRes() : R.drawable.sony);

        if (holder.ivHeart != null) {
            // check sqlite to show correct heart state on load
            boolean isFav = dbHelper.isFavourite(product.getModel());
            product.setFavourite(isFav);
            updateHeartIcon(holder.ivHeart, isFav);

            holder.ivHeart.setOnClickListener(v -> {
                boolean currentlyFav = dbHelper.isFavourite(product.getModel());

                if (currentlyFav) {
                    // remove from sqlite favourites
                    dbHelper.removeFavourite(product.getModel());
                    product.setFavourite(false);
                    updateHeartIcon(holder.ivHeart, false);
                    Toast.makeText(context, "Removed from favourites",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // add to sqlite favourites
                    dbHelper.addFavourite(product);
                    product.setFavourite(true);
                    updateHeartIcon(holder.ivHeart, true);
                    Toast.makeText(context, "Added to favourites",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        // open product detail screen on card click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("category", product.getModel());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("image", product.getImageRes());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return products.size(); }

    private void updateHeartIcon(ImageView heartView, boolean isFavourite) {
        if (isFavourite) {
            heartView.setImageResource(R.drawable.ic_heart_filled);
            heartView.setColorFilter(context.getColor(R.color.heart_active));
        } else {
            heartView.setImageResource(R.drawable.ic_heart_outline);
            heartView.setColorFilter(context.getColor(R.color.heart_default));
        }
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, ivHeart;
        TextView productName, productPrice, productModel;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            ivHeart      = itemView.findViewById(R.id.ivHeart);
            productName  = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productModel = itemView.findViewById(R.id.productModel);
        }
    }
}