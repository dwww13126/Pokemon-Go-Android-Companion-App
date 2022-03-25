package com.example.pokemongroupapp;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerData implements Serializable
{
    String name;
    String level;
    String team;
    ArrayList<String> favourite_gyms;

    public PlayerData(String name, String level, String team)
    {
        this.name = name;
        this.level = level;
        this.team = team;
        this.favourite_gyms = new ArrayList<String>();
    }

    public boolean sameAs(PlayerData compare)
    {
        if(compare.getName().compareTo(name) == 0 &&
                compare.getLevel().compareTo(level) == 0 &&
                compare.getTeam().compareTo(team) == 0)
        {
            return true;
        }
        return false;
    }

    public void addGym(String st){
        favourite_gyms.add(st);
    }
    public void removeGym(String st){
        favourite_gyms.remove(st);
    }
    public boolean containGym(String st){
        return favourite_gyms.contains(st);
    }
    public ArrayList<String> getUserGyms(){
        return favourite_gyms;
    }

    public String getName()
    {
        return name;
    }
    public String getLevel()
    {
        return level;
    }
    public String getTeam(){return team;}

    public void setName(String n){name = n;}
    public void setLevel(String i){level = i;}
    public void setTeam(String t){team = t;}

}
