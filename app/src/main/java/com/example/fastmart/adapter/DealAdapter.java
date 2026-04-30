package com.example.fastmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.example.fastmart.view.Buyer.ProductActivity;
import com.example.fastmart.R;
import com.example.fastmart.models.Product;

import java.util.List;

public class DealAdapter extends ArrayAdapter<Product> {

    private Context context;
    private List<Product> deals;

    public DealAdapter(@NonNull Context context, @NonNull List<Product> deals) {
        super(context, R.layout.item_deal, deals);
        this.context = context;
        this.deals = deals;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_deal, parent, false);
            holder = new ViewHolder();
            holder.card = convertView.findViewById(R.id.dealCard);
            holder.dealImage = convertView.findViewById(R.id.dealImage);
            holder.ivDealHeart = convertView.findViewById(R.id.ivDealHeart);
            holder.dealName = convertView.findViewById(R.id.dealName);
            holder.dealPrice = convertView.findViewById(R.id.dealPrice);
            holder.dealOriginalPrice = convertView.findViewById(R.id.dealOriginalPrice);
            holder.dealCategory = convertView.findViewById(R.id.dealCategory);
            holder.dealDescription = convertView.findViewById(R.id.dealDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // rotate item back upright
        convertView.setRotation(90f);

        // root should not eat touches
        convertView.setClickable(false);
        convertView.setFocusable(false);

        Product product = deals.get(position);

        holder.dealName.setText(product.getName());
        holder.dealPrice.setText(product.getPrice());
        holder.dealOriginalPrice.setText(product.getOriginalPrice());
        holder.dealCategory.setText(product.getModel());
        holder.dealDescription.setText(product.getDescription());
        holder.dealImage.setImageResource(product.getImageRes());

        // strikethrough original price
        holder.dealOriginalPrice.setPaintFlags(
                holder.dealOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        updateHeartIcon(holder.ivDealHeart, product.isFavourite());

        // heart toggle
        holder.ivDealHeart.setOnClickListener(v -> {
            product.setFavourite(!product.isFavourite());
            updateHeartIcon(holder.ivDealHeart, product.isFavourite());
        });

        // card click opens product detail
        holder.card.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("model", product.getModel());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("image", product.getImageRes());
            context.startActivity(intent);
        });

        return convertView;
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

    static class ViewHolder {
        CardView card;
        ImageView dealImage, ivDealHeart;
        TextView dealName, dealPrice, dealOriginalPrice, dealCategory, dealDescription;
    }
}