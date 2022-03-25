package com.example.pokemongroupapp;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;

public class InterestData implements Serializable
{
    String status;
    PlayerData player;
    String[] statues;
    int numStatus;

    public InterestData(Context context,PlayerData player, int status)
    {
        statues = context.getResources().getStringArray(R.array.Expression_Options);
        this.player = player;
        this.status = statues[status];
        numStatus = status;
    }

    public PlayerData getPlayer()
    {
        return player;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(int i) {
        this.status = statues[i];
        numStatus = i;
    }

    public int getNumStatus(){
        return numStatus;
    }
}
