package com.orbitnat.fivehundred.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.orbitnat.fivehundred.R;
import com.orbitnat.fivehundred.data.remote.Remote;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class ImageViewActivity extends AppCompatActivity {
    private static final String EXTRA_IMAGE_URL = "imageUrl";
    private static final String EXTRA_TITLE = "title";
    private static final String EXTRA_AUTHOR = "author";

    private View loadingProgress;
    private ImageViewTouch photo;

    public static Intent createIntent(Context context, String imageUrl, String title, String author) {
        Intent intent = new Intent(context, ImageViewActivity.class);
        intent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_AUTHOR, author);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_view);

        // Set full screen.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            android.support.v7.app.ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.hide();
            }
        }

        // Show loading
        loadingProgress = findViewById(R.id.loadingProgress);

        if (loadingProgress != null) {
            loadingProgress.setVisibility(View.VISIBLE);
        }

        // Load image.
        photo = (ImageViewTouch) findViewById(R.id.photo);
        if (photo != null) {
            photo.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            ImageLoader imageLoader = Remote.getInstance(this).getImageLoader();
            imageLoader.get(getIntent().getStringExtra(EXTRA_IMAGE_URL), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response != null) {
                        Bitmap bitmap = response.getBitmap();
                        if (bitmap != null) {
                            photo.setImageBitmap(bitmap);

                            if (loadingProgress != null) {
                                loadingProgress.setVisibility(View.GONE);
                            }
                        }
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (loadingProgress != null) {
                        loadingProgress.setVisibility(View.GONE);
                    }

                    Toast.makeText(ImageViewActivity.this, R.string.common_error, Toast.LENGTH_LONG).show();
                }
            });
        }


        TextView title = (TextView) findViewById(R.id.title);

        if (title != null) {
            title.setText(getIntent().getStringExtra(EXTRA_TITLE));
        }

        TextView author = (TextView) findViewById(R.id.author);

        if (author != null) {
            author.setText(getIntent().getStringExtra(EXTRA_AUTHOR));
        }
    }
}
