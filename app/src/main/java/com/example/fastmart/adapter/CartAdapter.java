package com.example.fastmart.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.models.CartItem;
import com.example.fastmart.models.Product;
import com.example.fastmart.utils.DatabaseHelper;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    ArrayList<CartItem> cartItems;
    DatabaseHelper dbHelper;
    Runnable onCartChanged;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems,
                       DatabaseHelper dbHelper, Runnable onCartChanged) {
        this.context       = context;
        this.cartItems     = cartItems;
        this.dbHelper      = dbHelper;
        this.onCartChanged = onCartChanged;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item    = cartItems.get(position);
        Product product  = item.getProduct();

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());
        holder.tvModel.setText(product.getModel());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.ivProduct.setImageResource(product.getImageRes() != 0
                ? product.getImageRes() : R.drawable.sony);

        // strikethrough on original price
        holder.tvOriginalPrice.setText(product.getOriginalPrice());
        holder.tvOriginalPrice.setPaintFlags(
                holder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        // increase quantity via sqlite update query
        holder.btnIncrease.setOnClickListener(v -> {
            dbHelper.increaseCartQuantity(product.getModel());
            item.increaseQuantity();
            holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
            onCartChanged.run();
        });

        // decrease quantity via sqlite update query, never below 1
        holder.btnDecrease.setOnClickListener(v -> {
            dbHelper.decreaseCartQuantity(product.getModel());
            item.decreaseQuantity();
            holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
            onCartChanged.run();
        });

        // three dot deletes item from sqlite using delete query
        holder.ivMoreOptions.setOnClickListener(v -> {
            dbHelper.removeFromCart(product.getModel());
            cartItems.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());
            onCartChanged.run();
        });
    }

    @Override
    public int getItemCount() { return cartItems.size(); }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivMoreOptions;
        TextView tvName, tvPrice, tvOriginalPrice, tvModel, tvQuantity;
        TextView btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct       = itemView.findViewById(R.id.ivProduct);
            ivMoreOptions   = itemView.findViewById(R.id.ivMoreOptions);
            tvName          = itemView.findViewById(R.id.tvName);
            tvPrice         = itemView.findViewById(R.id.tvPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvModel         = itemView.findViewById(R.id.tvModel);
            tvQuantity      = itemView.findViewById(R.id.tvQuantity);
            btnIncrease     = itemView.findViewById(R.id.btnIncrease);
            btnDecrease     = itemView.findViewById(R.id.btnDecrease);
        }
    }
}