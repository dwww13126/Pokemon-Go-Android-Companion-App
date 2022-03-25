package com.example.pokemongroupapp;

import java.io.Serializable;
import java.util.ArrayList;

public class RaidData implements Serializable
{
    String raidName;
    //Is the initial time which a raid was posted at
    //to allow for this to be a key
    String raidTime;
    String raidKey;
    String pokemon;
    InterestData host;
    int stars;
    boolean jumpedIn;
    boolean confirmed;
    ArrayList<InterestData> playerList;
    String battleTime = "NONE";
    String hatchTime = "NONE";
    String jumpTime = "NONE";

    public RaidData(String raidName, int stars, String raidTime, InterestData host, String Pokemon, boolean Confirmed, boolean JumpedIn)
    {
        this.pokemon = Pokemon;
        this.confirmed = Confirmed;
        this.raidName = raidName;
        this.stars = stars;
        this.raidTime = raidTime;
        this.hatchTime = raidTime;
        this.raidKey = raidName + "~" + raidTime + "~" + host.getPlayer().getName();
        this.playerList = new ArrayList<InterestData>();
        this.host = host;
        this.jumpedIn = JumpedIn;
        addPlayer(host);
    }

    public void addPlayer(InterestData p)
    {
        playerList.add(p);
    }

    public void printOut()
    {
        System.out.println(raidName + " " + raidTime + " " + getRaidPlayerCount());
    }

    public boolean isInterested(PlayerData player)
    {
        for(InterestData id : playerList)
        {
            if(player.sameAs(id.getPlayer()))
            {
                return true;
            }
        }
        return false;
    }

    public void changeInterest(PlayerData player, int interest)
    {
        if(interest == 6)
        {
            if(player.sameAs(host.getPlayer()))
            {
                return;
            }
            else
                {
                    InterestData remove = null;
                    for(InterestData id : playerList)
                    {
                        if(id.getPlayer().sameAs(player))
                        {
                            remove = id;
                        }
                    }
                    playerList.remove(remove);
                }
        }
        else

            {
                for(InterestData p : playerList)
                {
                    if(p.getPlayer().sameAs(player))
                    {
                        p.setStatus(interest);
                        break;
                    }
                }
            }
    }

    public boolean isConfirmed(){ return confirmed; }
    public boolean isJumpedIn(){ return jumpedIn; }
    public InterestData getHost(){ return host; }
    public String getName()
    {
        return raidName;
    }
    public String getRaidTime()
    {
        return raidTime;
    }
    public int getRaidPlayerCount()
    {
        return playerList.size();
    }
    public InterestData getPlayer(int position)
    {
        return playerList.get(position);
    }
    public int getStars()
    {
    return stars;
}

    public boolean isSameAs(String hostName, String name, String time)
    {
        if(hostName.compareTo(host.getPlayer().getName()) == 0 &&
        name.compareTo(raidName) == 0 && time.compareTo(raidTime) == 0)
        {
            return true;
        }
        return false;
    }
    //A method which allows for the details of a raid to be updated when details are received
    public void updateDetails(String confirmed, String battleTime, String jumpTime, String pokemon) {
        if(confirmed.compareTo("C") == 0){
            this.confirmed = true;
        }
        this.battleTime = battleTime;
        this.jumpTime = jumpTime;
        this.pokemon = pokemon;
    }
    //Allows for an initial post to be set to have a jump in time rather than just hatch time
    public void setJumpTime(String jumpTime) { this.jumpTime = jumpTime; }

    //Getter methods for reading data
    public String getPokemon()
    {
        return pokemon;
    }
    public String getCertainty()
    {
        //If confirmed == true then return "C" for Confirmed, "P" for
        //proposed otherwise
        if(confirmed == true){
            return "C";
        }
        else {
            return "P";
        }
    }
    public String getKey() { return raidKey; }
    public ArrayList<InterestData> getPlayerData() { return playerList; }
    public String getHatchTime() { return hatchTime; }
    public String getJumpTime() { return jumpTime; }
    public String getBattleTime() { return battleTime; }


    public void setJumpedIn(boolean jumpedIn) { this.jumpedIn = jumpedIn; }
}
