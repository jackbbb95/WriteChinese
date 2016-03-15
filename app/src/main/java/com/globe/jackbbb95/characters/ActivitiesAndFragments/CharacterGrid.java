package com.globe.jackbbb95.characters.ActivitiesAndFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.globe.jackbbb95.characters.Objects.CategoryObject;
import com.globe.jackbbb95.characters.Dialogs.AddCharacterDialog;
import com.globe.jackbbb95.characters.R;

import java.util.ArrayList;

public class CharacterGrid extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            final int categoryIndex = getIntent().getIntExtra("CategoryIndex", -1);
            ArrayList<CategoryObject> list = CategoryListActivityFragment.getCategoryList();
            CategoryObject category = list.get(categoryIndex);

            setContentView(R.layout.activity_character_grid);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(category.getName());


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddCharacterDialog(categoryIndex);
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(Exception e) {
            finish();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //for the practice mode activity
        if (id == R.id.practice) {
            Intent toWriteActivityIntent = new Intent(this, WriteActivity.class);
            toWriteActivityIntent.putExtra("PracticeBoolean",true);
            startActivity(toWriteActivityIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

