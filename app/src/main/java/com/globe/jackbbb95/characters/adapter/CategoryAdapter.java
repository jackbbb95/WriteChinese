package com.globe.jackbbb95.characters.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.globe.jackbbb95.characters.R;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.model.CharacterObject;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private static ClickListener clickListener;
    private ArrayList<CategoryObject> categoryList;

    public CategoryAdapter(ArrayList<CategoryObject> categoryList){
        this.categoryList = categoryList;
    }


    public void setOnItemClickListener(ClickListener clickListener) {
        CategoryAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, final int pos) {
        CategoryObject cat = categoryList.get(pos);
        holder.categoryTitle.setText(cat.getName());
        String chars = 0 + "";
        if(cat.getCharacters() != null)
            chars = cat.getCharacters().size() + "";
        holder.charCount.setText(chars);
        if(cat.getDescription().length() < 1)
            holder.categoryDescription.setVisibility(View.GONE);
        else
            holder.categoryDescription.setText((cat.getDescription()));


        /* BINDING the characters in here  TODO Make this a grid?*/

        StringBuilder characters = new StringBuilder();
        int count = 0;
        for(CharacterObject c : categoryList.get(pos).getCharacters()){
            if(count <= 5) {
                characters.append(c.getHanyuCharacters());
                characters.append("        ");
            }
            count++;
        }
        holder.characterList.setText(characters);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView categoryTitle;
        TextView categoryDescription;
        TextView characterList;
        TextView charCount;

        CategoryViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
            categoryDescription = (TextView) itemView.findViewById(R.id.category_description);
            characterList = (TextView) itemView.findViewById(R.id.characters_textview);
            charCount = (TextView) itemView.findViewById(R.id.char_count_category);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

}
