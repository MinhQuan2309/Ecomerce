package com.example.nguyenminhquan_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SearchHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<String> keywords;
    private OnRemoveListener onRemove;

    public interface OnRemoveListener {
        void onRemove(String keyword);
    }

    public SearchHistoryAdapter(Context context, List<String> keywords, OnRemoveListener onRemove) {
        this.context = context;
        this.keywords = keywords;
        this.onRemove = onRemove;
    }

    @Override
    public int getCount() {
        return keywords.size();
    }

    @Override
    public Object getItem(int position) {
        return keywords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_search_recent, parent, false);
        }

        TextView tvKeyword = convertView.findViewById(R.id.tvKeyword);
        ImageView btnRemove = convertView.findViewById(R.id.btnRemove);

        String keyword = keywords.get(position);
        tvKeyword.setText(keyword);

        btnRemove.setOnClickListener(v -> onRemove.onRemove(keyword));

        return convertView;
    }
}
