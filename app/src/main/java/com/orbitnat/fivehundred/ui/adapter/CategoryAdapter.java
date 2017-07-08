package com.orbitnat.fivehundred.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orbitnat.fivehundred.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.categoryHolder> {
    List<String> mData;

    public CategoryAdapter(List<String> data) {
        mData = data;
    }

    @Override
    public CategoryAdapter.categoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);

        return new CategoryAdapter.categoryHolder(view);
    }

    @Override
    public void onBindViewHolder(categoryHolder holder, int position) {
        holder.name.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class categoryHolder extends RecyclerView.ViewHolder {
        TextView name;

        public categoryHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
