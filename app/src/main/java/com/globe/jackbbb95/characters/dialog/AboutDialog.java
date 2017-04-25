package com.globe.jackbbb95.characters.dialog;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.globe.jackbbb95.characters.R;

public class AboutDialog extends DialogFragment{
    @Override
    public void onStart(){
        super.onStart();
        //Set the width to match the parent
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about_dialog, container);
        //Toolbar Setup
        setHasOptionsMenu(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Toolbar tb = (Toolbar) view.findViewById(R.id.about_toolbar);
        tb.setTitle(R.string.about);
        tb.inflateMenu(R.menu.menu_about);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.rate) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    //Try Google play
                    intent.setData(Uri.parse("market://details?id=com.globe.jackbbb95.writechinese"));
                    if (!MyStartActivity(intent)) {
                        //Market (Google play) app seems not installed, let's try to open a webbrowser
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?com.globe.jackbbb95.writechinese"));
                        if (!MyStartActivity(intent)) {
                            //Well if this also fails, we have run out of options, inform the user.
                            Toast.makeText(getContext(), "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        //Handling the credits
        TextView links = (TextView) view.findViewById(R.id.links);
        String linkText = "Open Source Projects Used\n" +
                "Gson: \nhttps://github.com/google/gson \n" +
                "DialogPlus: \nhttps://github.com/orhanobut/dialogplus \n" +
                "JPinyin: \nhttps://github.com/stuxuhai/jpinyin \n" +
                "DrawableView: \nhttps://github.com/PaNaVTEC/DrawableView \n" +
                "Material: \nhttps://github.com/rey5137/material";
        links.setText(linkText);

        return view;
    }

    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

}
