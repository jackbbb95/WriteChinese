package com.globe.jackbbb95.characters.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.globe.jackbbb95.characters.R;
import com.globe.jackbbb95.characters.WriteChineseApp;
import com.globe.jackbbb95.characters.dialog.PenDialog;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.view.fragment.CategoryListFragment;
import com.globe.jackbbb95.characters.view.fragment.WriteFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity implements PenDialog.OnPenSettingsSaved{

    private WriteFragment fragment;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        WriteChineseApp app = (WriteChineseApp) getApplication();
        mTracker = app.getDefaultTracker();

        boolean practice = getIntent().getBooleanExtra("PracticeBoolean",false);

        initToolbar(practice);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.pen_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPenDialog();
            }
        });

        fragment = new WriteFragment();
        Bundle b = new Bundle();
        b.putBoolean("PracticeBoolean",practice);
        b.putInt("CategoryIndex",getIntent().getIntExtra("CategoryIndex",-1));
        b.putInt("CharacterIndex",getIntent().getIntExtra("CharacterIndex",-1));
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment,"writeFragment").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.undo_button) {
            fragment.getDrawableView().undo();
            return true;
        }
        if (id == R.id.clear_button) {
            fragment.getDrawableView().clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar(boolean practiceMode) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = "";
        if(!practiceMode){
            ArrayList<CategoryObject> list = CategoryListFragment.getCategoryList();
            CategoryObject category = list.get(getIntent().getIntExtra("CategoryIndex", -1));
            title = category.getName();
            getSupportActionBar().setSubtitle(category.getDescription());
        }else title = "Practice Mode";

        getSupportActionBar().setTitle(title);
    }

    private void showPenDialog(){
        FragmentManager fm = getSupportFragmentManager();
        PenDialog penDialog = new PenDialog();
        penDialog.show(fm, "pen_dialog");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        reDrawFragment();
        fragment.getDrawableView().clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + WriteActivity.class.getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPenSettingsSaved() {
        reDrawFragment();
    }

    private void reDrawFragment(){
        Bundle b = fragment.getArguments();
        b.putInt("CharacterIndex",fragment.getCharIndex());
        getSupportFragmentManager().beginTransaction().detach(fragment).commit();
        getSupportFragmentManager().beginTransaction().attach(fragment).commit();
    }
}
