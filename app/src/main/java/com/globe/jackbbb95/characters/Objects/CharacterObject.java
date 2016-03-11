package com.globe.jackbbb95.characters.Objects;

import java.io.Serializable;

public class CharacterObject implements Serializable{

    private String hanyuCharacters;
    private String pinyin;
    private String definition;

    public CharacterObject(String hanyu, String pinyin, String definition){
        this.hanyuCharacters = hanyu;
        this.pinyin = pinyin;
        this.definition = definition;
    }

    public String getHanyuCharacters(){return hanyuCharacters;}
    public String getPinyin(){return pinyin;}
    public String getDefinition(){return definition;}

    public void setHanyuCharacters(String newCharacters){this.hanyuCharacters = newCharacters;}
    public void setPinyin(String newPinyin){this.pinyin = newPinyin;}
    public void setDefinition(String newDefinition){this.definition = newDefinition;}
}
