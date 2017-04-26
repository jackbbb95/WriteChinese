package com.globe.jackbbb95.characters.view.fragment;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.globe.jackbbb95.characters.R;
import com.globe.jackbbb95.characters.adapter.CharacterAdapter;
import com.globe.jackbbb95.characters.dialog.EditCharacterDialog;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.model.CharacterObject;
import com.globe.jackbbb95.characters.view.activity.WriteActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;

public class CharacterGridFragment extends BaseFragment {

    private static CharacterAdapter characterAdapter;
    private DialogPlus editOrDeleteDialog;
    private CategoryObject currCategory;
    private ArrayList<CategoryObject> list;
    private TextView emptyText;

    public static CharacterAdapter getCharacterAdapter(){return characterAdapter;}
    public TextView getEmptyText(){return  emptyText;}

    public CharacterGridFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int categoryIndex = getActivity().getIntent().getIntExtra("CategoryIndex", -1);
        list = CategoryListFragment.getCategoryList();
        currCategory = list.get(categoryIndex);
        View rootView;
        //inflate the correct view based on whether this set has characters or not

        rootView = inflater.inflate(R.layout.fragment_character_grid, container, false);

        emptyText = (TextView) rootView.findViewById(R.id.empty_characters_text);
        if(currCategory.getCharacters().size() > 0)
            emptyText.setVisibility(View.GONE);
        else
            emptyText.setVisibility(View.VISIBLE);

        //Sets up the list with the characters
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.grid_item);
        characterAdapter = new CharacterAdapter(currCategory.getCharacters(),getContext());
        //DividerItemDecoration dividerItemDecorationHorizontal = new DividerItemDecoration(recyclerView.getContext(), OrientationHelper.HORIZONTAL);
        //DividerItemDecoration dividerItemDecorationVertical = new DividerItemDecoration(recyclerView.getContext(), OrientationHelper.VERTICAL);
        //recyclerView.addItemDecoration(dividerItemDecorationHorizontal);
        //recyclerView.addItemDecoration(dividerItemDecorationVertical);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(characterAdapter);

        //handling all the clickable events on each of the characters in the recyclerview
        final View header = inflater.inflate(R.layout.header, null);
        header.setClickable(false);
        characterAdapter.setOnItemClickListener(new CharacterAdapter.ClickListener() {
            boolean hasLongClicked = false;
            @Override
            public void onItemClick(int position, View v) {
                if (!hasLongClicked) {
                    Intent toWriteActivityIntent = new Intent(getActivity(), WriteActivity.class);
                    toWriteActivityIntent.putExtra("CategoryIndex",categoryIndex);
                    toWriteActivityIntent.putExtra("CharacterIndex", position);
                    getActivity().startActivity(toWriteActivityIntent);
                } else
                    hasLongClicked = false;
            }

            @Override
            public void onItemLongClick(int position, View v) {
                //TextView theHeader = (TextView) header.findViewById(R.id.character_header);
                //TextView theSubHeader = (TextView) header.findViewById(R.id.character_header_pinyin);
                Toolbar tb = (Toolbar) header.findViewById(R.id.header_toolbar);
                final int charIndex = position;

                //Get Current Item
                final CharacterObject currChar = currCategory.getCharacters().get(position);

                //Set Title
                //theHeader.setText(currChar.getHanyuCharacters());
                //theSubHeader.setText(currChar.getPinyin());
                tb.setTitle(currChar.getHanyuCharacters());
                tb.setSubtitle(currChar.getPinyin());

                ArrayAdapter<String> editOrDeleteAdapter = new ArrayAdapter<>(getActivity(), R.layout.edit_delete_dialog_list_item);
                editOrDeleteAdapter.add("Edit");
                editOrDeleteAdapter.add("Delete");
                //setup the dialog for when the user longclicks on an item
                editOrDeleteDialog = DialogPlus.newDialog(getContext())
                        .setHeader(header)
                        .setGravity(Gravity.CENTER)
                        .setAdapter(editOrDeleteAdapter)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                //For when the user clicks the edit option
                                //Creates popup where the user can edit the countr
                                if (position == 1) {
                                    editOrDeleteDialog.dismiss();
                                    showEditCharacterDialog(categoryIndex,charIndex);
                                }
                                //For if the User clicks Delete
                                //Popup Dialog asking if sure
                                else if (position == 2) {
                                    deleteCharacter(currChar);
                                }
                            }
                        })
                        .setExpanded(false, 300)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();

                editOrDeleteDialog.show();
                hasLongClicked = true;
            }
        });


        return rootView;
    }

    //handles the dialog that ensures the user wants to delete something, and does it if so
    private void deleteCharacter(final CharacterObject currCharacter) {
        AlertDialog confirmDelete = new AlertDialog.Builder(getActivity()).create();
        confirmDelete.setTitle("Delete?");
        confirmDelete.setMessage("Are you sure you want to delete " + currCharacter.getHanyuCharacters() + " ?");
        confirmDelete.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int index = currCategory.getCharacters().indexOf(currCharacter);
                        currCategory.getCharacters().remove(currCharacter);
                        characterAdapter.notifyItemRemoved(index);
                        CategoryListFragment.saveList(getContext());
                        dialog.dismiss();
                        editOrDeleteDialog.dismiss();
                        if(currCategory.getCharacters().size() == 0){
                            emptyText.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getActivity(), currCharacter.getHanyuCharacters() + " Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        confirmDelete.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        confirmDelete.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        CategoryListFragment.saveList(getContext());
        CategoryListFragment.getCategoryAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        list = CategoryListFragment.getCategoryList();
        characterAdapter.notifyDataSetChanged();
    }

    private void showEditCharacterDialog(int categoryIndex, int charIndex) {
        DialogFragment dialog = EditCharacterDialog.newInstance(categoryIndex,charIndex);
        dialog.show(getActivity().getFragmentManager(),"EditCharacterDialog");
    }
}