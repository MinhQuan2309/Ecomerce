package com.example.nguyenminhquan_app;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private String description;
    private int imageRes;
    private String assetName;

    public Product(int id, String name, double price, String description, int imageRes) {
        this(id, name, price, description, imageRes, null);
    }

    public Product(int id, String name, double price, String description,
                   int imageRes, String assetName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageRes = imageRes;
        this.assetName = assetName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getAssetName() {
        return assetName;
    }
}
