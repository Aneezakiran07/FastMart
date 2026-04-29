package com.example.fastmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastmart.R;
import com.example.fastmart.models.CartItem;
import com.example.fastmart.models.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<Order> orderList;

    public OrderAdapter(Context context, ArrayList<Order> orderList) {
        this.context   = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderId.setText("#" + order.getOrderId());
        holder.tvTimestamp.setText(order.getTimestamp());
        holder.tvBuyerName.setText("Buyer: " + order.getBuyerName());
        holder.tvStatus.setText(order.getStatus());
        holder.tvTotalPrice.setText("$" + String.format("%.2f", order.getTotalPrice()));

        // build product summary string without nested recyclerview
        StringBuilder itemsSummary = new StringBuilder();
        if (order.getItems() != null) {
            for (CartItem item : order.getItems()) {
                itemsSummary.append(item.getProduct().getName())
                        .append(" x").append(item.getQuantity())
                        .append(" — $").append(item.getProduct().getPrice())
                        .append("\n");
            }
        }
        holder.tvItemsSummary.setText(itemsSummary.toString().trim());
    }

    @Override
    public int getItemCount() { return orderList.size(); }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTimestamp, tvBuyerName, tvStatus, tvTotalPrice, tvItemsSummary;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId      = itemView.findViewById(R.id.tvOrderId);
            tvTimestamp    = itemView.findViewById(R.id.tvTimestamp);
            tvBuyerName    = itemView.findViewById(R.id.tvBuyerName);
            tvStatus       = itemView.findViewById(R.id.tvStatus);
            tvTotalPrice   = itemView.findViewById(R.id.tvTotalPrice);
            tvItemsSummary = itemView.findViewById(R.id.tvItemsSummary);
        }
    }
}