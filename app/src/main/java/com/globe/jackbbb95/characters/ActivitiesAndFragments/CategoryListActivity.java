package com.globe.jackbbb95.characters.ActivitiesAndFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.globe.jackbbb95.characters.Dialogs.AboutDialog;
import com.globe.jackbbb95.characters.Dialogs.AddCategoryDialog;
import com.globe.jackbbb95.characters.R;

//The activity that holds the different sets of characters
public class CategoryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);


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
}
