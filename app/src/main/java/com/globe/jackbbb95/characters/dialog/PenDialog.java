package com.globe.jackbbb95.characters.dialog;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.globe.jackbbb95.characters.view.fragment.WriteFragment;
import com.globe.jackbbb95.characters.R;
import com.rey.material.widget.Slider;


public class PenDialog extends DialogFragment {

    private static Slider penSlider;

    @Override
    public void onStart(){
        super.onStart();
        //Set the width to match the parent
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width,height);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.pen_settings_dialog, container);
        //Toolbar Setup
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Toolbar tb = (Toolbar) view.findViewById(R.id.pen_settings_toolbar);
        //tb.setTitle(R.string.pen_settings);
        penSlider = (Slider) view.findViewById(R.id.pen_slider);
        final TextView penSize = (TextView) view.findViewById(R.id.pen_size_tv);
        penSlider.setValue(WriteFragment.getPenSize(), true);


        final String penSizeText = ("Pen Size: " + WriteFragment.getPenSize());
        penSize.setText(penSizeText);
        penSlider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                String newPenSizeText = ("Pen Size: " + penSlider.getValue());
                penSize.setText(newPenSizeText);
            }
        });

        //Cancel Button Setup
        Button cancel = (Button) view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

        //Create Button Setup
        Button save = (Button) view.findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteFragment.setPenSize(penSlider.getValue());
                WriteFragment.getConfig().setStrokeWidth(penSlider.getValue());
                SharedPreferences prefs = getActivity().getSharedPreferences("pen_prefs", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("PEN_SIZE", penSlider.getValue());
                editor.apply();
                Toast.makeText(getActivity(), "Pen Settings Saved", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            }
        });
        return view;
    }
}
