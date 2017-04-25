package com.globe.jackbbb95.characters.dialog;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.globe.jackbbb95.characters.view.fragment.CategoryListFragment;
import com.globe.jackbbb95.characters.view.fragment.CharacterGridFragment;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.model.CharacterObject;
import com.globe.jackbbb95.characters.R;

import java.util.ArrayList;

public class EditCharacterDialog extends DialogFragment {

    private CharacterObject currCharacter;
    private CategoryObject currCategory;

    public EditCharacterDialog(){}

    @Override
    public void onStart(){
        super.onStart();
        //setting the size of the edit dialog
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width,height);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.edit_character_dialog, container);
        ArrayList<CategoryObject> list = CategoryListFragment.getCategoryList();
        currCategory = list.get(getArguments().getInt("Category", -1));
        currCharacter = currCategory.getCharacters().get(getArguments().getInt("Character",-1));
        //setup toolbar
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);


        Toolbar tb = (Toolbar) view.findViewById(R.id.edit_word_toolbar);
        tb.setTitle(currCharacter.getHanyuCharacters());

        //Title Input
        final TextInputLayout characterTil = (TextInputLayout) view.findViewById(R.id.edit_character_til);
        characterTil.setHint(currCharacter.getHanyuCharacters());
        final EditText characterET = (EditText) view.findViewById(R.id.edit_character_edit_text);

        //Pinyin ET Setup
        final TextInputLayout pinyinTil = (TextInputLayout) view.findViewById(R.id.edit_pinyin_til);
        pinyinTil.setHint(currCharacter.getPinyin());
        final EditText pinyinET = (EditText) view.findViewById(R.id.edit_pinyin_edit_text);


        //Definition Setup
        final TextInputLayout defTil = (TextInputLayout) view.findViewById(R.id.edit_definition_til);
        if(currCharacter.getDefinition() != null)
            defTil.setHint(currCharacter.getDefinition());
        else
            defTil.setHint(getString(R.string.Definition));
        final EditText defET = (EditText) view.findViewById(R.id.edit_definition_edit_text);

        //Setup Live pinyin changes to pinyinET
        characterET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                pinyinET.setText(" ");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pinyinET.setText(PinyinHelper.convertToPinyinString(characterET.getText().toString(), " ", PinyinFormat.WITH_TONE_MARK));
                    }
                }, 200);
            }
        });

        //Show keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        //Cancel Button Setup
        Button cancel = (Button) view.findViewById(R.id.edit_dialog_cancel_button);
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
        Button create = (Button) view.findViewById(R.id.edit_dialog_create_button);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hanyuInput = characterET.getText().toString();
                String pinyinInput = pinyinET.getText().toString();
                String defInput = defET.getText().toString();
                String defaultCharacter = String.valueOf(characterTil.getHint());
                String defaultPinyin = String.valueOf(pinyinTil.getHint());
                String defaultDef = String.valueOf(defTil.getHint());

                //Handle odd cases where one thing is changed
                if(hanyuInput.length() < 1 && defInput.length() < 1 && pinyinInput.length() > 0){
                    currCharacter.setPinyin(pinyinInput);
                    Toast.makeText(getActivity(), currCharacter.getHanyuCharacters() + " saved in " + currCategory.getDescription(), Toast.LENGTH_SHORT).show();
                }else if(hanyuInput.length() > 0 && defInput.length() < 1 && pinyinInput.length() < 1){
                    currCharacter.setHanyuCharacters(hanyuInput);
                    Toast.makeText(getActivity(), currCharacter.getHanyuCharacters() + " saved in " + currCategory.getDescription(), Toast.LENGTH_SHORT).show();
                }else if(hanyuInput.length() < 1 && defInput.length() > 0 && pinyinInput.length() < 1){
                    currCharacter.setDefinition(defInput);
                    Toast.makeText(getActivity(), currCharacter.getHanyuCharacters() + " saved in " + currCategory.getDescription(), Toast.LENGTH_SHORT).show();
                }else if(hanyuInput.length() > 0 && defInput.length() > 0 && pinyinInput.length() < 1){
                    currCharacter.setHanyuCharacters(hanyuInput);
                    currCharacter.setDefinition(defInput);
                    Toast.makeText(getActivity(), currCharacter.getHanyuCharacters() + " saved in " + currCategory.getDescription(), Toast.LENGTH_SHORT).show();
                }else if(hanyuInput.length() < 1 && defInput.length() > 0 && pinyinInput.length() > 0){
                    currCharacter.setPinyin(pinyinInput);
                    currCharacter.setDefinition(defInput);
                    Toast.makeText(getActivity(), currCharacter.getHanyuCharacters() + " saved in " + currCategory.getDescription(), Toast.LENGTH_SHORT).show();
                }else if(hanyuInput.length() > 0 && defInput.length() < 1 && pinyinInput.length() > 0){
                    currCharacter.setHanyuCharacters(hanyuInput);
                    currCharacter.setPinyin(pinyinInput);
                    Toast.makeText(getActivity(), currCharacter.getHanyuCharacters() + " saved in " + currCategory.getDescription(), Toast.LENGTH_SHORT).show();
                }else if(hanyuInput.length() < 1 && pinyinInput.length() < 1 && defInput.length() < 1)
                    getDialog().dismiss();
                else{ //default case for changing all three
                    currCharacter.setHanyuCharacters(hanyuInput);
                    currCharacter.setPinyin(pinyinInput);
                    currCharacter.setDefinition(defInput);
                    Toast.makeText(getActivity(), currCharacter.getHanyuCharacters() + " saved in " + currCategory.getDescription(), Toast.LENGTH_SHORT).show();
                }

                //save the list and update view
                int index = currCategory.getCharacters().indexOf(currCharacter);
                currCategory.getCharacters().set(index,currCharacter);
                CharacterGridFragment.getCharacterAdapter().notifyItemChanged(index);
                CategoryListFragment.saveList(getActivity());

                //Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                getDialog().dismiss();
            }
        });

        return view;
    }

    public static EditCharacterDialog newInstance(int categoryIndex,int charIndex) {
        EditCharacterDialog dialog = new EditCharacterDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("Category",categoryIndex);
        bundle.putInt("Character", charIndex);
        dialog.setArguments(bundle);
        return dialog;
    }
}
