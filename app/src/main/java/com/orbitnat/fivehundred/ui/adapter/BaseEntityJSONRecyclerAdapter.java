package com.orbitnat.fivehundred.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseEntityJSONRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends InternalBaseEntityJSONRecyclerAdapter<T, VH> {
    public BaseEntityJSONRecyclerAdapter(Context context) {
        this(context, null);
    }

    public BaseEntityJSONRecyclerAdapter(Context context, JSONArray jsonArray) {
        super(context, jsonArray);
    }

    @Override
    protected T tryGetItem(int position) throws JSONException {
        JSONObject jsonObject = getJSONArray().getJSONObject(position);
        return convertToObject(jsonObject);
    }

    protected abstract T convertToObject(JSONObject jsonObject) throws JSONException;
}
