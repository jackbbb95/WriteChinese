package com.globe.jackbbb95.characters.ActivitiesAndFragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.globe.jackbbb95.characters.Dialogs.PenDialog;
import com.globe.jackbbb95.characters.Objects.CategoryObject;
import com.globe.jackbbb95.characters.R;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {

    private boolean isInFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean practice = getIntent().getBooleanExtra("PracticeBoolean",false);
        String title = "";
        if(!practice){
        ArrayList<CategoryObject> list = CategoryListActivityFragment.getCategoryList();
        CategoryObject category = list.get(getIntent().getIntExtra("CategoryIndex", -1));
            if(category.getDescription().length() < 1)
                title = category.getName();
            else
                title = category.getDescription();
        }else title = "Practice Mode";

        setContentView(R.layout.activity_write);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.pen_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPenDialog();
            }
        });

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
            WriteActivityFragment.getDrawableView().undo();
            return true;
        }
        if (id == R.id.clear_button) {
            WriteActivityFragment.getDrawableView().clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPenDialog(){
        FragmentManager fm = getSupportFragmentManager();
        PenDialog penDialog = new PenDialog();
        penDialog.show(fm, "pen_dialog");
    }

    //handles orientation changes
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        isInFocus = hasFocus;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isInFocus) finish();
    }
}
