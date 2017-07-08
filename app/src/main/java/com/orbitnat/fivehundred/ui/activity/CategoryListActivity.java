package com.orbitnat.fivehundred.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orbitnat.fivehundred.R;
import com.orbitnat.fivehundred.ui.adapter.CategoryAdapter;
import com.orbitnat.fivehundred.ui.adapter.RecyclerItemClickListener;
import com.orbitnat.fivehundred.ui.widget.DividerItemDecoration;

import java.util.Arrays;
import java.util.List;

/**
 * Application home page.
 */
public class CategoryListActivity extends AppCompatActivity {

    // Preload category list.
    private List<String> mCategoryList = Arrays.asList(
            "Uncategorized",
            "Abstract",
            "Animals",
            "Black and White",
            "Celebrities",
            "City and Architecture",
            "Commercial",
            "Concert",
            "Family",
            "Fashion",
            "Film",
            "Fine Art",
            "Food",
            "Journalism",
            "Landscapes",
            "Macro",
            "Nature",
            "Nude",
            "People",
            "Performing",
            "Sport",
            "Still Life",
            "Street",
            "Transportation",
            "Travel",
            "Underwater",
            "Urban Exploration",
            "Wedding"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        // Recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            // Add divider
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this);
            recyclerView.addItemDecoration(dividerItemDecoration);

            // Set adapter
            CategoryAdapter adapter = new CategoryAdapter(mCategoryList);
            recyclerView.setAdapter(adapter);

            // Add click event on the item.
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener() {
                @Override
                public void onItemClick(RecyclerView parent, View view, int position, long id, View targetView) {
                    startActivity(RecentPhotoListActivity.createIntent(CategoryListActivity.this, mCategoryList.get(position)));
                }
            });
        }
    }
}
