package com.example.nguyenminhquan_app;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private double price;
    private String description;
    private int imageRes;
    private String assetName;
    private String category;
    private String productType;
    private String productLine;

    public Product(int id, String name, double price, String description, int imageRes) {
        this(id, name, price, description, imageRes, null, null, null, null);
    }

    public Product(int id, String name, double price, String description,
                   int imageRes, String assetName) {
        this(id, name, price, description, imageRes, assetName, null, null, null);
    }

    public Product(int id, String name, double price, String description,
                   int imageRes, String assetName, String category) {
        this(id, name, price, description, imageRes, assetName, category, null, null);
    }

    public Product(int id, String name, double price, String description,
                   int imageRes, String assetName, String category,
                   String productType, String productLine) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageRes = imageRes;
        this.assetName = assetName;
        this.category = category;
        this.productType = productType;
        this.productLine = productLine;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductType() {
        if (productType != null && !productType.isEmpty()) return productType;
        String n = name != null ? name.toLowerCase() : "";
        if (n.contains("polo")) return "Áo Thun Cổ Polo & Henley";
        if (n.contains("thun")) return "Áo Thun Cổ Tròn";
        if (n.contains("ngắn tay")) return "Áo Sơ Mi Tay Ngắn";
        if (n.contains("sơ mi")) return "Áo Sơ Mi Tay Dài";
        if (n.contains("khoác")) return "Áo Khoác Gió (Dù)";
        if (n.contains("jean")) return "Quần Jeans Ống Suông";
        if (n.contains("short")) return "Quần Short Kaki";
        if (n.contains("sneaker")) return "Giày Sneaker";
        if (n.contains("chạy bộ")) return "Giày Chạy Bộ";
        if (n.contains("mũ") || n.contains("nón")) return "Nón & Mũ";
        if (n.contains("balo")) return "Balo & Túi";
        if (n.contains("kính")) return "Kính Mát";
        if (n.contains("đồng hồ")) return "Đồng Hồ";
        if (n.contains("tai nghe")) return "Tai Nghe";
        if (n.contains("bình")) return "Bình Giữ Nhiệt";
        if (n.contains("sổ")) return "Sổ Tay Bìa Da";
        return category != null ? category : "Thời trang";
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductLine() {
        if (productLine != null && !productLine.isEmpty()) return productLine;
        String n = name != null ? name.toLowerCase() : "";
        if (n.contains("chạy bộ") || n.contains("thể thao")) return "Thể Thao";
        if (n.contains("polo") || n.contains("kẻ") || n.contains("sọc")) return "Bề Mặt Vải Khác Biệt";
        if (n.contains("kháng khuẩn") || n.contains("khử mùi") || n.contains("dry")) return "Nâng Cấp Hiệu Năng";
        if (n.contains("tek black")) return "Tek Black Jean";
        if (n.contains("khoác") || n.contains("kính") || n.contains("tai nghe")) return "Nâng Cấp Hiệu Năng";
        if (n.contains("balo") || n.contains("bình") || n.contains("sổ") || n.contains("mũ")) return "Phụ Kiện Thiết Yếu";
        return "Mặc Hằng Ngày";
    }

    public void setProductLine(String productLine) {
        this.productLine = productLine;
    }
}

