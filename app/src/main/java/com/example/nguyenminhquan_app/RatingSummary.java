package com.example.nguyenminhquan_app;

public class RatingSummary {
    private int total;
    private double average;
    private int[] stars;

    public RatingSummary(int total, double average, int[] stars) {
        this.total = total;
        this.average = average;
        this.stars = stars;
    }

    public int getTotal() {
        return total;
    }

    public double getAverage() {
        return average;
    }

    public int getCount(int star) {
        return stars[star - 1];
    }

    public boolean isEmpty() {
        return total == 0;
    }
}
