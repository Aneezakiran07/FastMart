package com.example.fastmart.model;

public class Product {
    String name;
    String price;
    String originalPrice;
    String model;        // used as category in deals
    String description;
    int imageRes;
    boolean isFavourite;

    public Product(String name, String price, String originalPrice,
                   String model, String description, int imageRes) {
        this.name = name;
        this.price = price;
        this.originalPrice = originalPrice;
        this.model = model;
        this.description = description;
        this.imageRes = imageRes;
        this.isFavourite = false;
    }

    // Getters
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getOriginalPrice() { return originalPrice; }
    public String getModel() { return model; }
    public String getDescription() { return description; }
    public int getImageRes() { return imageRes; }
    public boolean isFavourite() { return isFavourite; }
    public void setFavourite(boolean favourite) { isFavourite = favourite; }
}