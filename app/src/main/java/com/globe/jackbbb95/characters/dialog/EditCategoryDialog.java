package com.globe.jackbbb95.characters.dialog;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.globe.jackbbb95.characters.view.fragment.CategoryListFragment;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.R;

import java.util.ArrayList;

public class EditCategoryDialog extends DialogFragment {

    private CategoryObject currCategory;
    private ArrayList<CategoryObject> list;

    public EditCategoryDialog() {
    }

    @Override
    public void onStart() {
        super.onStart();
        //setting the size of the edit dialog
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_category_dialog, container);
        int index = getArguments().getInt("Category");
        list = CategoryListFragment.getCategoryList();
        currCategory = list.get(index);
        //setup toolbar
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        Toolbar tb = (Toolbar) view.findViewById(R.id.edit_category_toolbar);
        tb.setTitle(currCategory.getDescription());

        //Title Input
        final TextInputLayout nameTil = (TextInputLayout) view.findViewById(R.id.edit_category_title_til);
        nameTil.setHint(currCategory.getName());
        final EditText nameET = (EditText) view.findViewById(R.id.edit_category_title);

        //Pinyin ET Setup
        final TextInputLayout descriptionTil = (TextInputLayout) view.findViewById(R.id.edit_category_description_til);
        descriptionTil.setHint(currCategory.getDescription());
        final EditText descriptionET = (EditText) view.findViewById(R.id.edit_category_description);


        //Show keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //Cancel Button Setup
        Button cancel = (Button) view.findViewById(R.id.edit_category_cancel_button);
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
        Button create = (Button) view.findViewById(R.id.edit_category_create_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean changed = false;
                String name = nameET.getText().toString().trim();
                String description = descriptionET.getText().toString().trim();
                //Handle odd cases where one thing is changed

                if (name.length() < 1 && description.length() > 0) {
                    currCategory.setDescription(description);
                    Toast.makeText(getActivity(), currCategory.getName() + " saved", Toast.LENGTH_SHORT).show();
                } else if (name.length() > 0 && description.length() < 1) {
                    currCategory.setName(name);
                    Toast.makeText(getActivity(), currCategory.getName() + " saved", Toast.LENGTH_SHORT).show();
                } else if (name.length() < 1 && description.length() < 1) {
                    getDialog().dismiss();
                } else {
                    currCategory.setName(name);
                    currCategory.setDescription(description);
                    Toast.makeText(getActivity(), currCategory.getName() + " saved", Toast.LENGTH_SHORT).show();
                }
                //save the list and update view
                int index = list.indexOf(currCategory);
                CategoryListFragment.getCategoryAdapter().notifyItemChanged(index);
                CategoryListFragment.saveList(getActivity());

                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                getDialog().dismiss();
            }
        });

        return view;
    }

    public static EditCategoryDialog newInstance(int categoryIndex) {
        EditCategoryDialog dialog = new EditCategoryDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("Category", categoryIndex);
        dialog.setArguments(bundle);
        return dialog;
    }

}
