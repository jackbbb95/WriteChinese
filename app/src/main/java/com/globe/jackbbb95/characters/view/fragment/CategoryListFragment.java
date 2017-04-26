package com.globe.jackbbb95.characters.view.fragment;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
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
import com.globe.jackbbb95.characters.adapter.CategoryAdapter;
import com.globe.jackbbb95.characters.dialog.EditCategoryDialog;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.util.SaveUtil;
import com.globe.jackbbb95.characters.view.activity.CharacterGridActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;

public class CategoryListFragment extends BaseFragment {

    private static ArrayList<CategoryObject> categoryList = new ArrayList<>();
    private static CategoryAdapter categoryAdapter;
    private  TextView emptyText;
    private DialogPlus editOrDeleteDialog;

    public CategoryListFragment() {}

    public static ArrayList<CategoryObject> getCategoryList() {
        return categoryList;
    }

    public static CategoryAdapter getCategoryAdapter() {
        return categoryAdapter;
    }

    public TextView getEmptyText() {
        return emptyText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        final Context context = getContext();
        categoryList = SaveUtil.getList(getContext());
        //for first start
        if (categoryList == null)
            categoryList = new ArrayList<>();
        //dealing with whether the list is empty or not
        emptyText = (TextView) rootView.findViewById(R.id.empty_text);
        if (categoryList.size() > 0)
            emptyText.setVisibility(View.GONE);
        else
            emptyText.setVisibility(View.VISIBLE);

        //Setup the list using a RecyclerView
        RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.catagory_list);
        DividerItemDecoration dividerItemDecorationVertical = new DividerItemDecoration(getContext(), OrientationHelper.VERTICAL);
        recycler.addItemDecoration(dividerItemDecorationVertical);
        recycler.setLayoutManager(new LinearLayoutManager(context));
        categoryAdapter = new CategoryAdapter(categoryList);
        recycler.setAdapter(categoryAdapter);

        //setting up the functions of when you press a set
        final View header = inflater.inflate(R.layout.header, null);
        categoryAdapter.setOnItemClickListener(new CategoryAdapter.ClickListener() {
            boolean hasLongClicked = false;

            @Override
            public void onItemClick(int position, View v) {
                if (!hasLongClicked) {
                    Intent toCharacterGridActivityIntent = new Intent(getActivity(), CharacterGridActivity.class);
                    toCharacterGridActivityIntent.putExtra("CategoryIndex", position);
                    getActivity().startActivity(toCharacterGridActivityIntent);
                } else
                    hasLongClicked = false;
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Toolbar tb = (Toolbar) header.findViewById(R.id.header_toolbar);
                final int index = position;
                final CategoryObject category = categoryList.get(position);
                tb.setTitle(category.getName());
                tb.setSubtitle(category.getDescription());

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
                                    showEditCategoryDialog(index);
                                }
                                //For if the User clicks Delete
                                //Popup Dialog asking if sure
                                else if (position == 2) {
                                    deleteCategory(category);
                                }
                            }
                        })
                        .setExpanded(false, 300)
                        .create();

                editOrDeleteDialog.show();
                hasLongClicked = true;
            }
        });
        return rootView;
    }

    //Handling the dialog that comes up ensuring you want to delete something, and deletes it if so
    private void deleteCategory(final CategoryObject category) {
        AlertDialog confirmDelete = new AlertDialog.Builder(getActivity()).create();
        confirmDelete.setTitle("Delete?");
        confirmDelete.setMessage("Are you sure you want to delete " + category.getName() + " ?");
        confirmDelete.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int index = categoryList.indexOf(category);
                        categoryList.remove(category);
                        categoryAdapter.notifyItemRemoved(index);
                        saveList(getContext());
                        dialog.dismiss();
                        editOrDeleteDialog.dismiss();
                        if (categoryList.size() == 0) {
                            emptyText.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(getActivity(), "Category '" + category.getName() + "' Deleted", Toast.LENGTH_SHORT).show();
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

    private void showEditCategoryDialog(int categoryIndex) {
        DialogFragment dialog = EditCategoryDialog.newInstance(categoryIndex);
        dialog.show(getActivity().getFragmentManager(), "EditCharacterDialog");
    }

    @Override
    public void onPause() {
        super.onPause();
        SaveUtil.saveList(getContext(), categoryList);
    }

    //Called from everywhere throughout the app to save the list in its current state
    public static void saveList(Context context) {
        SaveUtil.saveList(context, categoryList);
    }

}

