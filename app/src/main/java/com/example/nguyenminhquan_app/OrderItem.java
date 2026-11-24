package com.example.nguyenminhquan_app;

import java.io.Serializable;

public class OrderItem implements Serializable {
    private int id;
    private int orderId;
    private int productId;
    private String productName;
    private double productPrice;
    private int quantity;
    private String productImage;

    public OrderItem(int id, int orderId, int productId, String productName,
                     double productPrice, int quantity, String productImage) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.productImage = productImage;
    }

    public OrderItem(int productId, String productName, double productPrice,
                     int quantity, String productImage) {
        this(0, 0, productId, productName, productPrice, quantity, productImage);
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public double getSubtotal() {
        return productPrice * quantity;
    }
}
