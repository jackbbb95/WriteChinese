package com.globe.jackbbb95.characters.Dialogs;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.globe.jackbbb95.characters.R;

public class AboutDialog extends DialogFragment{
    @Override
    public void onStart(){
        super.onStart();
        //Set the width to match the parent
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width,height);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about_dialog, container);
        //Toolbar Setup
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Toolbar tb = (Toolbar) view.findViewById(R.id.about_toolbar);
        tb.setTitle(R.string.about);

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
}
