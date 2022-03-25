package com.example.pokemongroupapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyListAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    private final ArrayList<String> raidNames;
    private final ArrayList<RaidData> raids;

    public MyListAdapter(Activity context, ArrayList<String> raidNames,ArrayList<RaidData> raidData) {
        super(context, R.layout.mylistdashboard, raidNames);
        // TODO Auto-generated constructor stub

        this.raidNames = raidNames;
        this.context = context;
        this.raids = raidData;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylistdashboard, null, true);

        TextView nameText = (TextView) rowView.findViewById(R.id.raidName);
        ImageView starImage = (ImageView)rowView.findViewById(R.id.stars_dash);
        TextView timeText = (TextView) rowView.findViewById(R.id.raidTime);
        TextView playerText = (TextView) rowView.findViewById(R.id.raidPlayers);
        ImageView raidBoarder = (ImageView) rowView.findViewById(R.id.Dash_raidBoarder);

        nameText.setText(raidNames.get(position));
        if(raids.get(position).isConfirmed() == true)
        {
            raidBoarder.setImageDrawable(getContext().getDrawable(R.drawable.ic_background_pokemon2));
        }
        if (raids.get(position).getBattleTime().compareTo("NONE") != 0)
        {
            timeText.setText("Group finishes at: " + raids.get(position).getBattleTime());
        }
        else if (raids.get(position).getJumpTime().compareTo("NONE") != 0)
        {
            timeText.setText("Jump in time: " + raids.get(position).getJumpTime());
        }
        else if(raids.get(position).getPokemon().compareTo("Unknown") == 0)
        {
            timeText.setText("Hatching at: " + raids.get(position).getHatchTime());
        }
        playerText.setText("Interested Players: " + raids.get(position).getRaidPlayerCount());

        if(raids.get(position).getStars() == 1)
        {
            starImage.setImageResource(R.drawable.ic_stars1);
        }
        else if(raids.get(position).getStars() == 2)
        {
            starImage.setImageResource(R.drawable.ic_stars2);
        }
        else if(raids.get(position).getStars() == 3)
        {
            starImage.setImageResource(R.drawable.ic_stars3);
        }
        else if(raids.get(position).getStars() == 4)
        {
            starImage.setImageResource(R.drawable.ic_stars4);
        }
        else if(raids.get(position).getStars() == 5)
        {
            starImage.setImageResource(R.drawable.ic_stars5);
        }

        return rowView;

    }
}
