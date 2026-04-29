package com.example.fastmart.models;

public class Product {
    String productId;
    String name;
    String price;
    String originalPrice;
    String model;
    String description;
    int imageRes;
    boolean isFavourite;

    // empty constructor required by firebase to deserialize data
    public Product() {}

    public Product(String name, String price, String originalPrice,
                   String model, String description, int imageRes) {
        this.name          = name;
        this.price         = price;
        this.originalPrice = originalPrice;
        this.model         = model;
        this.description   = description;
        this.imageRes      = imageRes;
        this.isFavourite   = false;
    }

    public String getProductId()                    { return productId; }
    public void setProductId(String productId)      { this.productId = productId; }
    public String getName()                         { return name; }
    public void setName(String name)                { this.name = name; }
    public String getPrice()                        { return price; }
    public void setPrice(String price)              { this.price = price; }
    public String getOriginalPrice()                { return originalPrice; }
    public void setOriginalPrice(String p)          { this.originalPrice = p; }
    public String getModel()                        { return model; }
    public void setModel(String model)              { this.model = model; }
    public String getDescription()                  { return description; }
    public void setDescription(String description)  { this.description = description; }
    public int getImageRes()                        { return imageRes; }
    public void setImageRes(int imageRes)           { this.imageRes = imageRes; }
    public boolean isFavourite()                    { return isFavourite; }
    public void setFavourite(boolean favourite)     { this.isFavourite = favourite; }
}