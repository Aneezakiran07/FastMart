package com.example.fastmart.models;

public class CartItem {

    Product product;
    int quantity;

    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public void increaseQuantity() { quantity++; }
    public void decreaseQuantity() { if (quantity > 1) quantity--; }
}