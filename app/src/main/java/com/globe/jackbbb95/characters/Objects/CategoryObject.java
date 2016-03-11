package com.globe.jackbbb95.characters.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryObject implements Serializable{

    private String name;
    private ArrayList<CharacterObject> characters = new ArrayList<>();
    private String description;

    public CategoryObject(String name,String description){
        this.name = name;
        this.description = description;
    }

    public String getName(){return name;}
    public ArrayList<CharacterObject> getCharacters(){
        if(characters == null)
            characters = new ArrayList<>();
        return characters;
    }
    public String getDescription(){return description;}

    public void setName(String name){this.name = name;}
    public void setDescription(String description){this.description = description;}
}
