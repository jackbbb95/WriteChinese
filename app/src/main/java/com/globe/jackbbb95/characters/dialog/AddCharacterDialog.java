package com.globe.jackbbb95.characters.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.globe.jackbbb95.characters.R;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.model.CharacterObject;
import com.globe.jackbbb95.characters.view.activity.CharacterGridActivity;
import com.globe.jackbbb95.characters.view.fragment.CategoryListFragment;
import com.globe.jackbbb95.characters.view.fragment.CharacterGridFragment;

import java.util.ArrayList;


//The dialog that pops up to add a new character to the selected set
public class AddCharacterDialog extends DialogFragment {

    private CategoryObject currCategory;

    public AddCharacterDialog(){}

    @Override
    public void onStart(){
        super.onStart();
        //setting the size of the edit dialog
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width,height);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_character_dialog, container);
        ArrayList<CategoryObject> list = CategoryListFragment.getCategoryList();
        currCategory = list.get(getArguments().getInt("Category"));
        //setup toolbar
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Toolbar tb = (Toolbar) view.findViewById(R.id.add_word_toolbar);
        tb.setTitle(R.string.add_characters);

        //Title Input
        TextInputLayout characterTil = (TextInputLayout) view.findViewById(R.id.create_character_til);
        characterTil.setHint(getString(R.string.character));
        final EditText characterET = (EditText) view.findViewById(R.id.character_edit_text);

        //Pinyin ET Setup
        TextInputLayout pinyinTil = (TextInputLayout) view.findViewById(R.id.pinyin_til);
        pinyinTil.setHint(getString(R.string.pinyin));
        final EditText pinyinET = (EditText) view.findViewById(R.id.pinyin_edit_text);


        //Setup Definition
        TextInputLayout definitionTil = (TextInputLayout) view.findViewById(R.id.definition_til);
        definitionTil.setHint(getString(R.string.Definition));
        final EditText definitionET = (EditText) view.findViewById(R.id.definition_edit_text);

        final Button create = (Button) view.findViewById(R.id.create_button);
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
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinyinInput = pinyinET.getText().toString().trim();
                String hanyuInput = characterET.getText().toString().trim();
                String definitionInput = definitionET.getText().toString().trim();

                if (hanyuInput.length() < 1) {
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    characterET.startAnimation(shake);
                }else if(definitionInput.length() < 1){
                    Animation shake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
                    definitionET.startAnimation(shake);
                }else{
                    //save the list and update view
                    CharacterObject characterToAdd = new CharacterObject(hanyuInput, pinyinInput, definitionInput);
                    currCategory.getCharacters().add(characterToAdd);
                    CharacterGridFragment.getCharacterAdapter().notifyItemInserted(currCategory.getCharacters().indexOf(characterToAdd));
                    CategoryListFragment.saveList(getContext());
                    ((CharacterGridActivity)getActivity()).onAddChar();

                    //Hide Keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    //Toast
                    Toast.makeText(getActivity(), characterToAdd.getHanyuCharacters() + " added to " + currCategory.getDescription(), Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }

    public static AddCharacterDialog newInstance(int categoryIndex) {
        AddCharacterDialog dialog = new AddCharacterDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("Category",categoryIndex);
        dialog.setArguments(bundle);
        return dialog;
    }

    public interface OnAddCharListener {
        public void onAddChar();
    }

}
