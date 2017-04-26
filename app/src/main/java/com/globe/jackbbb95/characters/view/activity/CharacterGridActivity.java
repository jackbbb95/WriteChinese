package com.globe.jackbbb95.characters.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.globe.jackbbb95.characters.R;
import com.globe.jackbbb95.characters.WriteChineseApp;
import com.globe.jackbbb95.characters.dialog.AddCharacterDialog;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.view.fragment.CategoryListFragment;
import com.globe.jackbbb95.characters.view.fragment.CharacterGridFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class CharacterGridActivity extends AppCompatActivity implements AddCharacterDialog.OnAddCharListener {

    private CharacterGridFragment fragment;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WriteChineseApp app = (WriteChineseApp) getApplication();
        mTracker = app.getDefaultTracker();

        final int categoryIndex = getIntent().getIntExtra("CategoryIndex", -1);
        ArrayList<CategoryObject> list = CategoryListFragment.getCategoryList();
        CategoryObject category = list.get(categoryIndex);
        setContentView(R.layout.activity_character_grid);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(category.getName());
        getSupportActionBar().setSubtitle(category.getDescription());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCharacterDialog(categoryIndex);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragment = new CharacterGridFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, "characterGridFragment").commit();

    }

    private void showAddCharacterDialog(int index) {
        AddCharacterDialog dialog = AddCharacterDialog.newInstance(index);
        dialog.show(getSupportFragmentManager(),"addChars");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_character_grid, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + CharacterGridActivity.class.getSimpleName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //for the practice mode activity
        switch (id) {
            case R.id.practice: {
                Intent toWriteActivityIntent = new Intent(this, WriteActivity.class);
                toWriteActivityIntent.putExtra("PracticeBoolean", true);
                startActivity(toWriteActivityIntent);
                return true;
            }
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAddChar() {
        fragment.getEmptyText().setVisibility(View.GONE);
    }
}

