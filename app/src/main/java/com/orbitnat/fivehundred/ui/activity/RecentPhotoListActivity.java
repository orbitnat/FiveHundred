package com.orbitnat.fivehundred.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.orbitnat.fivehundred.R;
import com.orbitnat.fivehundred.data.constant.JSONKeys;
import com.orbitnat.fivehundred.data.model.Photo;
import com.orbitnat.fivehundred.data.remote.Remote;
import com.orbitnat.fivehundred.ui.adapter.RecentPhotoAdapter;
import com.orbitnat.fivehundred.ui.adapter.RecyclerItemClickListener;
import com.orbitnat.fivehundred.util.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * List of photo in selected category.
 */
public class RecentPhotoListActivity extends AppCompatActivity {
    private static final String EXTRA_CATEGORY_NAME = "category_name";

    private final String TAG = "Five Hundred";

    private RecentPhotoAdapter adapter;
    private View loadingProgress;
    LinearLayoutManager layoutManager;

    private int previousTotal = 0;
    private boolean loading = true;
    private int currentPage = 1;

    public static Intent createIntent(Context context, String categoryName) {
        Intent intent = new Intent(context, RecentPhotoListActivity.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recent_photo_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
        }

        // Recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RecentPhotoAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);

        // Add click event.
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id, View targetView) {
                Photo photo = adapter.getItem(position);

                if (photo.getImages() != null && photo.getImages().size() >= 2) {
                    String title = photo.getUser() != null ? photo.getUser().getFullname() : null;

                    startActivity(ImageViewActivity.createIntent(RecentPhotoListActivity.this,
                            photo.getImages().get(1).getUrl(),
                            photo.getName(),
                            title));
                }
                else {
                    // No photo.
                    Toast.makeText(RecentPhotoListActivity.this, R.string.common_error, Toast.LENGTH_LONG).show();
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                // Check for scroll down
                if(dy > 0)
                {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= pastVisiblesItems) {
                        Log.i(TAG, "Load more...");
                        loading = true;
                        currentPage++;

                        String url = getPhotoSearchUrl(currentPage);

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        if (response != null && response.has(JSONKeys.PHOTOS)) {
                                            try {
                                                JSONArray currentJsonArray = adapter.getJSONArray();
                                                JSONArray newJsonArray = response.getJSONArray(JSONKeys.PHOTOS);
                                                JSONArray resultJsonArray = JSONUtils.concatJsonArray(currentJsonArray, newJsonArray);

                                                adapter.setData(resultJsonArray, false);
                                                adapter.notifyItemRangeInserted(currentJsonArray != null ? currentJsonArray.length() : 0,
                                                        newJsonArray != null ? newJsonArray.length() : 0);
                                            }
                                            catch (JSONException e) {
                                                Log.e(TAG, "Response error", e);
                                            }
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(RecentPhotoListActivity.this, R.string.common_error, Toast.LENGTH_LONG).show();
                                    }
                                });

                        Remote.getInstance(RecentPhotoListActivity.this).addToRequestQueue(jsObjRequest);
                    }
                }
            }
        });

        String url = getPhotoSearchUrl(currentPage);

        // Create request for photo search.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.has(JSONKeys.PHOTOS)) {
                                adapter.setData(response.getJSONArray(JSONKeys.PHOTOS), true);
                            }
                            else {
                                Toast.makeText(RecentPhotoListActivity.this, R.string.common_error, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Response error", e);
                        }

                        if (loadingProgress != null) {
                            loadingProgress.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (loadingProgress != null) {
                            loadingProgress.setVisibility(View.GONE);
                        }

                        Toast.makeText(RecentPhotoListActivity.this, R.string.common_error, Toast.LENGTH_LONG).show();
                    }
                });

        // Add request queue.
        Remote.getInstance(this).addToRequestQueue(jsObjRequest);
        loadingProgress = findViewById(R.id.loadingProgress);

        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return false;
    }

    /**
     * Compose url for photo search.
     *
     * @param page Page number
     * @return url
     */
    private String getPhotoSearchUrl(int page) {
        if (page < 1) {
            page = 1;
        }

        // Prepare url
        String baseServiceUrl = getString(R.string.base_service_url);
        String photoSearchUrl = "/v1/photos/search";
        String consumer_key = getString(R.string.consumer_key);
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);

        try {
            categoryName = URLEncoder.encode(categoryName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Cannot encode category name", e);
        }

        String query = "?consumer_key=" + consumer_key + "&only=" + categoryName + "&image_size=4%2C5&sort=created_at&rpp=10&page=" + page;

        return baseServiceUrl + photoSearchUrl + query;
    }
}
