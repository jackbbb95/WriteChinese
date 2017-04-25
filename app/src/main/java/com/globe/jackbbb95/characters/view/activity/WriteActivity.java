package com.globe.jackbbb95.characters.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.globe.jackbbb95.characters.dialog.PenDialog;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.R;
import com.globe.jackbbb95.characters.view.fragment.WriteFragment;
import com.globe.jackbbb95.characters.view.fragment.CategoryListFragment;

import java.util.ArrayList;

public class WriteActivity extends AppCompatActivity {

    private boolean isInFocus = false;
    private WriteFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean practice = getIntent().getBooleanExtra("PracticeBoolean",false);
        String title = "";
        if(!practice){
            ArrayList<CategoryObject> list = CategoryListFragment.getCategoryList();
            CategoryObject category = list.get(getIntent().getIntExtra("CategoryIndex", -1));
            title = category.getName();
            getSupportActionBar().setSubtitle(category.getDescription());
        }else title = "Practice Mode";

        getSupportActionBar().setTitle(title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.pen_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPenDialog();
            }
        });

        fragment = new WriteFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragment,"writeFragment").commit();

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
            WriteFragment.getDrawableView().undo();
            return true;
        }
        if (id == R.id.clear_button) {
            WriteFragment.getDrawableView().clear();
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
