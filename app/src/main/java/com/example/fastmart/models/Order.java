package com.example.fastmart.models;

import java.util.ArrayList;

public class Order {
    String orderId;
    String buyerName;
    String timestamp;
    String status;
    double totalPrice;
    ArrayList<CartItem> items;

    // empty constructor required by firebase
    public Order() {}

    public Order(String orderId, String buyerName, String timestamp,
                 double totalPrice, ArrayList<CartItem> items) {
        this.orderId   = orderId;
        this.buyerName = buyerName;
        this.timestamp = timestamp;
        this.totalPrice = totalPrice;
        this.items     = items;
        this.status    = "Processing";
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getBuyerName() { return buyerName; }
    public void setBuyerName(String buyerName) { this.buyerName = buyerName; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public ArrayList<CartItem> getItems() { return items; }
    public void setItems(ArrayList<CartItem> items) { this.items = items; }
}