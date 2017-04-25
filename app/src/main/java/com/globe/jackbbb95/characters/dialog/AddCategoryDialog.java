package com.globe.jackbbb95.characters.dialog;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.globe.jackbbb95.characters.view.activity.CategoryListActivity;
import com.globe.jackbbb95.characters.view.fragment.CategoryListFragment;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.R;

import java.util.ArrayList;


public class AddCategoryDialog extends DialogFragment {

    private ArrayList<CategoryObject> list;

    public AddCategoryDialog() {
    }

    @Override
    public void onStart() {
        super.onStart();
        //Set the width to match the parent
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_category_dialog, container);
        list = CategoryListFragment.getCategoryList();
        //Toolbar Setup
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Toolbar tb = (Toolbar) view.findViewById(R.id.add_catagory_toolbar);
        tb.setTitle(R.string.add_a_category);

        //Title Input
        TextInputLayout titleTil = (TextInputLayout) view.findViewById(R.id.create_catagory_title_til);
        titleTil.setHint(getString(R.string.title));
        final EditText titleET = (EditText) view.findViewById(R.id.category_title);

        //Description Input
        TextInputLayout descriptionTil = (TextInputLayout) view.findViewById(R.id.create_catagory_description_til);
        descriptionTil.setHint(getString(R.string.description));
        final EditText descriptionET = (EditText) view.findViewById(R.id.category_description);

        //Show keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


        //Cancel Button Setup
        Button cancel = (Button) view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                getDialog().cancel();
            }
        });

        //Create Button Setup
        Button create = (Button) view.findViewById(R.id.create_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleET.getText().toString().trim();
                String description = descriptionET.getText().toString().trim();
                if (title.length() < 1) {
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    titleET.startAnimation(shake);
                } else {
                    CategoryObject thingToAdd = new CategoryObject(title, description);
                    list.add(thingToAdd);
                    CategoryListFragment.getCategoryAdapter().notifyItemInserted(list.indexOf(thingToAdd));
                    CategoryListFragment.saveList(getContext());
                    ((CategoryListActivity)getActivity()).onAddCategory();

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Toast.makeText(getActivity(), thingToAdd.getDescription() + " Created", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    public interface OnAddCategoryListener {
        public void onAddCategory();
    }

}
