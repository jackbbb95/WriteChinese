package com.globe.jackbbb95.characters;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.globe.jackbbb95.characters.Objects.CategoryObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SaveList {

    public static void saveList(Context context,ArrayList<CategoryObject> list) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.clear();
        editor.apply();
        editor.putString("Categories", json);
        editor.apply();
    }

    public static ArrayList<CategoryObject> getList(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("Categories", null);
        Type type = new TypeToken<ArrayList<CategoryObject>>() {}.getType();
        return gson.fromJson(json, type);

    }
}
