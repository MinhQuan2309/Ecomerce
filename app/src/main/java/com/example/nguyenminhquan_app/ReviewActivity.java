package com.example.nguyenminhquan_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ReviewActivity extends AppCompatActivity {
    private static final int MAX_STARS = 5;

    private int productId;

    private ListView listReviews;
    private View layoutEmpty;
    private View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ToolbarHelper.setupBack(this);

        productId = getIntent().getIntExtra(Constants.EXTRA_PRODUCT_ID, 0);

        listReviews = findViewById(R.id.listReviews);
        layoutEmpty = findViewById(R.id.layoutEmpty);

        headerView = LayoutInflater.from(this)
                .inflate(R.layout.view_rating_summary, listReviews, false);
        listReviews.addHeaderView(headerView, null, false);

        MaterialButton btnWriteReview = findViewById(R.id.btnWriteReview);
        btnWriteReview.setOnClickListener(v -> showWriteDialog());

        showReviews();
    }

    private void showReviews() {
        List<Review> reviews = AppDatabase.getReviews(this, productId);
        RatingSummary summary = AppDatabase.getRatingSummary(this, productId);

        showSummary(summary);

        listReviews.setAdapter(new ReviewAdapter(this, reviews));

        boolean empty = reviews.isEmpty();
        listReviews.setVisibility(empty ? View.GONE : View.VISIBLE);
        layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    private void showSummary(RatingSummary summary) {
        TextView tvAverage = headerView.findViewById(R.id.tvAverage);
        TextView tvTotal = headerView.findViewById(R.id.tvTotal);
        TextView tvReviewCount = headerView.findViewById(R.id.tvReviewCount);
        LinearLayout layoutAverageStars = headerView.findViewById(R.id.layoutAverageStars);
        LinearLayout layoutBars = headerView.findViewById(R.id.layoutBars);

        tvAverage.setText(String.format(java.util.Locale.US, "%.1f", summary.getAverage()));
        tvTotal.setText(getString(R.string.review_count, summary.getTotal()));
        tvReviewCount.setText(getString(R.string.review_count, summary.getTotal()));

        StarsHelper.show(layoutAverageStars, summary.getAverage(), 20);

        showBars(layoutBars, summary);
    }

    private void showBars(LinearLayout layoutBars, RatingSummary summary) {
        layoutBars.removeAllViews();

        for (int star = MAX_STARS; star >= 1; star--) {
            View row = LayoutInflater.from(this)
                    .inflate(R.layout.item_rating_bar, layoutBars, false);

            TextView tvStarLabel = row.findViewById(R.id.tvStarLabel);
            TextView tvStarCount = row.findViewById(R.id.tvStarCount);
            ProgressBar barStar = row.findViewById(R.id.barStar);

            int count = summary.getCount(star);
            int percent = summary.getTotal() == 0 ? 0 : count * 100 / summary.getTotal();

            tvStarLabel.setText(String.valueOf(star));
            tvStarCount.setText(String.valueOf(count));
            barStar.setProgress(percent);

            layoutBars.addView(row);
        }
    }

    private void showWriteDialog() {
        View form = LayoutInflater.from(this).inflate(R.layout.dialog_write_review, null);

        RatingBar ratingBar = form.findViewById(R.id.ratingBar);
        EditText edtComment = form.findViewById(R.id.edtComment);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.review_write)
                .setView(form)
                .setPositiveButton(R.string.review_send, null)
                .setNegativeButton(R.string.review_cancel, null)
                .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            int rating = Math.round(ratingBar.getRating());
            String comment = edtComment.getText().toString().trim();

            if (rating < 1) {
                Toast.makeText(this, R.string.review_need_rating, Toast.LENGTH_SHORT).show();
                return;
            }

            if (comment.isEmpty()) {
                Toast.makeText(this, R.string.review_need_comment, Toast.LENGTH_SHORT).show();
                return;
            }

            AppDatabase.addReview(this, productId, Session.getUsername(this), rating, comment);
            dialog.dismiss();

            Toast.makeText(this, R.string.review_thanks, Toast.LENGTH_SHORT).show();
            showReviews();
        });
    }
}
