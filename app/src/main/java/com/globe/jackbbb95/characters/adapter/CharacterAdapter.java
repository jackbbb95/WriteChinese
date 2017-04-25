package com.globe.jackbbb95.characters.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.globe.jackbbb95.characters.model.CharacterObject;
import com.globe.jackbbb95.characters.R;

import java.util.ArrayList;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder> {

    private static ClickListener characterClickListener;
    private ArrayList<CharacterObject> characters;

    public CharacterAdapter(ArrayList<CharacterObject> characters,Context context){
        this.characters = characters;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CharacterAdapter.characterClickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }


    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.character_grid_item,parent,false);
        return new CharacterAdapter.CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        CharacterObject ch = characters.get(position);
        holder.characterTv.setText(ch.getHanyuCharacters());
        holder.pinyinTv.setText(ch.getPinyin()); // add translation
        holder.defTV.setText(ch.getDefinition());
    }


    @Override
    public int getItemCount() {
        if(characters == null)
            return 0;
        return characters.size();
    }


    static class CharacterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        RelativeLayout characterItem;
        TextView characterTv;
        TextView pinyinTv;
        TextView defTV;

        CharacterViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            characterItem = (RelativeLayout)itemView.findViewById(R.id.character_item);
            characterTv = (TextView) itemView.findViewById(R.id.character_card_character);
            pinyinTv = (TextView) itemView.findViewById(R.id.character_card_pinyin);
            defTV = (TextView) itemView.findViewById(R.id.character_card_def);
        }
        @Override
        public void onClick(View v) {
            characterClickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            characterClickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

}
