package com.example.fastmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.root.MyApplication;
import com.example.fastmart.view.ProductActivity;
import com.example.fastmart.R;
import com.example.fastmart.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<Product> products;
    MyApplication app;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.app = (MyApplication) ((android.app.Activity) context).getApplication();
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
        holder.productImage.setImageResource(product.getImageRes());

        // show correct heart state
        updateHeartIcon(holder.ivHeart, product.isFavourite());

        // heart click saves to shared preferences
        holder.ivHeart.setOnClickListener(v -> {
            product.setFavourite(!product.isFavourite());
            updateHeartIcon(holder.ivHeart, product.isFavourite());
            saveFavourite(product);
        });

        // click card to open product detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("model", product.getModel());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("image", product.getImageRes());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private void saveFavourite(Product product) {
        SharedPreferences prefs = ((android.app.Activity) context)
                .getSharedPreferences("FastMartPrefs", Context.MODE_PRIVATE);
        String json = prefs.getString("user.favourites", null);

        // load existing favourites
        List<Product> favouritesList = new ArrayList<>();
        if (json != null) {
            Type type = new TypeToken<List<Product>>() {}.getType();
            List<Product> saved = new Gson().fromJson(json, type);
            if (saved != null) favouritesList = saved;
        }

        if (product.isFavourite()) {
            // add only if not already in list
            boolean alreadyExists = false;
            for (Product p : favouritesList) {
                if (p.getModel().equals(product.getModel())) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) favouritesList.add(product);
        } else {
            // remove from list
            favouritesList.removeIf(p -> p.getModel().equals(product.getModel()));
        }

        prefs.edit()
                .putString("user.favourites", new Gson().toJson(favouritesList))
                .apply();

        android.util.Log.d("FASTMART", "Favourite saved: "
                + product.getName() + " isFav: " + product.isFavourite());

        Toast.makeText(context,
                product.isFavourite() ? "Added to favourites" : "Removed from favourites",
                Toast.LENGTH_SHORT).show();
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
        TextView productName, productPrice, productModel, product;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productModel = itemView.findViewById(R.id.productModel);
            product = itemView.findViewById(R.id.product);
        }
    }
}