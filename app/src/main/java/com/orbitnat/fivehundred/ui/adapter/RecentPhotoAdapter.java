package com.orbitnat.fivehundred.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.orbitnat.fivehundred.R;
import com.orbitnat.fivehundred.data.model.Photo;
import com.orbitnat.fivehundred.data.remote.Remote;

import org.json.JSONException;
import org.json.JSONObject;

public class RecentPhotoAdapter extends BaseEntityJSONRecyclerAdapter<Photo, RecentPhotoAdapter.PhotoHolder> {

    public RecentPhotoAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position, Photo item) {
        holder.title.setText(item.getName());

        if (item.getUser() != null) {
            holder.author.setText(item.getUser().getFullname());
        }

        if (item.getImages() != null && item.getImages().size() > 0) {
            ImageLoader imageLoader = Remote.getInstance(getContext()).getImageLoader();
            holder.photo.setImageUrl(item.getImages().get(0).getUrl(), imageLoader);
        }
    }

    @Override
    protected Photo convertToObject(JSONObject jsonObject) throws JSONException {
        return new Gson().fromJson(jsonObject.toString(), Photo.class);
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recent_photo, parent, false);

        return new RecentPhotoAdapter.PhotoHolder(view);
    }

    public static class PhotoHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        NetworkImageView photo;

        public PhotoHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            photo = (NetworkImageView) itemView.findViewById(R.id.photo);
        }
    }
}
