package com.example.nguyenminhquan_app;

public class Review {
    private int id;
    private String username;
    private int rating;
    private String comment;
    private long createdAt;

    public Review(int id, String username, int rating, String comment, long createdAt) {
        this.id = id;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
