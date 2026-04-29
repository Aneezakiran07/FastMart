package com.example.fastmart.models;

public class CartItem {
    Product product;
    int quantity;

    public CartItem() {}

    public CartItem(Product product) {
        this.product  = product;
        this.quantity = 1;
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void increaseQuantity() { this.quantity++; }
    public void decreaseQuantity() { if (this.quantity > 1) this.quantity--; }
}