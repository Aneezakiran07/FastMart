package com.example.fastmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.models.Product;
import com.example.fastmart.view.ProductActivity;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context  = context;
        this.products = products;
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

        // use hardcoded image if imageRes is 0 or not set
        if (product.getImageRes() != 0) {
            holder.productImage.setImageResource(product.getImageRes());
        } else {
            holder.productImage.setImageResource(R.drawable.sony);
        }

        // hide heart icon for seller view, show for buyer view
        if (holder.ivHeart != null) {
            updateHeartIcon(holder.ivHeart, product.isFavourite());

            holder.ivHeart.setOnClickListener(v -> {
                product.setFavourite(!product.isFavourite());
                updateHeartIcon(holder.ivHeart, product.isFavourite());
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
    public int getItemCount() {
        return products.size();
    }

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