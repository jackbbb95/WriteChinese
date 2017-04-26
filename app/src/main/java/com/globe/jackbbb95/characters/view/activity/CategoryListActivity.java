package com.globe.jackbbb95.characters.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.globe.jackbbb95.characters.R;
import com.globe.jackbbb95.characters.WriteChineseApp;
import com.globe.jackbbb95.characters.dialog.AboutDialog;
import com.globe.jackbbb95.characters.dialog.AddCategoryDialog;
import com.globe.jackbbb95.characters.view.fragment.CategoryListFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

//The activity that holds the different sets of characters
public class CategoryListActivity extends AppCompatActivity implements AddCategoryDialog.OnAddCategoryListener {

    private CategoryListFragment fragment;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        WriteChineseApp app = (WriteChineseApp) getApplication();
        mTracker = app.getDefaultTracker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        fragment = new CategoryListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment,"categoryListFragment").commit();


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.show();
            }
        }, 600);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategoryDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.about) {
            showAboutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + CategoryListActivity.class.getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void showAddCategoryDialog() {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog();
        addCategoryDialog.show(fm, "add_category");
    }

    private void showAboutDialog() {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        AboutDialog aboutDialog = new AboutDialog();
        aboutDialog.show(fm, "about_dialog");
    }

    @Override
    public void onAddCategory() {
        fragment.getEmptyText().setVisibility(View.GONE);
    }
}
