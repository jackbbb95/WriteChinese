package com.globe.jackbbb95.characters;

import java.util.ArrayList;

public class CatagoryObject {

    private String name;
    private ArrayList<String> characters;
    private int characterCount;

    public CatagoryObject(String name){
        this.name = name;
    }

    public String getName(){return name;}
    public ArrayList<String> getCharacters(){return characters;}
    public int getCharacterCount(){return characterCount;}

    public void setName(String name){this.name = name;}
    public void setCharacters(ArrayList<String> characters){
        this.characters = characters;
        for(String i : characters){
            characterCount++;
        }
    }
}
