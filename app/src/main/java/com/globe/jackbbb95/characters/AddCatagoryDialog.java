package com.globe.jackbbb95.characters;

import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AddCatagoryDialog extends DialogFragment {

    public AddCatagoryDialog(){}

    @Override
    public void onStart(){
        super.onStart();
        //setting the size of the edit dialog
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width,height);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_catagory_dialog, container);
        //setup toolbar
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Toolbar tb = (Toolbar) view.findViewById(R.id.add_catagory_toolbar);
        tb.setTitle(R.string.add_a_catagory);

        TextInputLayout titleTil = (TextInputLayout) view.findViewById(R.id.create_catagory_title_til);
        titleTil.setHint(getString(R.string.title));
/*

        //get the current countr to be edited
        Bundle bundle = getArguments();
        curCountr = (Countr) bundle.getSerializable("Countr");
        //for the name of the countr
        name = (EditText) view.findViewById(R.id.edit_name);
        TextInputLayout nameTil = (TextInputLayout) view.findViewById(R.id.edit_name_til);
        nameTil.setHint("Name");
        //configure keyboard to show the correct keyboard for each edittext
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY); //show keyboard


*/

        return view;
    }
}
