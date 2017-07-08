package com.orbitnat.fivehundred.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

abstract class InternalBaseEntityJSONRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private List<T> mData;
    private JSONArray mJSONArray;
    private final Context mContext;

    public InternalBaseEntityJSONRecyclerAdapter(Context context) {
        this(context, null);
    }

    public InternalBaseEntityJSONRecyclerAdapter(Context context, JSONArray jsonArray) {
        super();

        mContext = context;
        mJSONArray = jsonArray;

        newDataCache(jsonArray);
    }

    public T getItem(int position) {
        if(mJSONArray == null || mData == null) {
            return null;
        }

        T item;

        try {
            if (mData.size() > position) {
                item = mData.get(position);
                if (item == null) {
                    item = tryGetItem(position);
                    mData.set(position, item);
                }
            } else {
                // Refill list with empty.
                while(mData.size() <= position) {
                    mData.add(null);
                }

                item = tryGetItem(position);
                mData.set(position, item);
            }
        }
        catch(JSONException ex) {
            throw new IllegalArgumentException("Cannot get data at given position (" + position + ").", ex);
        }

        return item;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, position, getItem(position));
    }

    @Override
    public int getItemCount() {
        return mJSONArray == null ? 0 : mJSONArray.length();
    }

    public JSONArray getJSONArray() {
        return mJSONArray;
    }

    public void setData(JSONArray jsonArray, boolean notifyChange) {
        newDataCache(jsonArray);

        mJSONArray = jsonArray;

        if(notifyChange) {
            notifyDataSetChanged();
        }
    }

    private void newDataCache(JSONArray jsonArray) {
        if(jsonArray == null) {
            mData = null;
        }
        else {
            mData = new ArrayList<>(jsonArray.length());
            for(int i = 0; i < jsonArray.length(); i++) {
                mData.add(null);
            }
        }
    }

    public abstract void onBindViewHolder(VH holder, int position, T item);
    protected abstract T tryGetItem(int position) throws JSONException;
}