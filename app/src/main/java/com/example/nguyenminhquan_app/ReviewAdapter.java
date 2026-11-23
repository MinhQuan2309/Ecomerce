package com.example.nguyenminhquan_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ReviewAdapter extends BaseAdapter {
    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_review, parent, false);
        }

        LinearLayout layoutStars = convertView.findViewById(R.id.layoutStars);
        TextView tvComment = convertView.findViewById(R.id.tvComment);
        TextView tvAuthor = convertView.findViewById(R.id.tvAuthor);

        Review review = reviews.get(position);

        StarsHelper.show(layoutStars, review.getRating(), 16);
        tvComment.setText(review.getComment());
        tvAuthor.setText(review.getUsername() + "  •  " + timeAgo(review.getCreatedAt()));

        return convertView;
    }

    private String timeAgo(long createdAt) {
        long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - createdAt);

        if (days <= 0) return context.getString(R.string.review_time_today);
        return context.getString(R.string.review_time_days, days);
    }
}
