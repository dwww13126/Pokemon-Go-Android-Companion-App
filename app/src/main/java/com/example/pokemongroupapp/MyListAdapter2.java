package com.example.pokemongroupapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class MyListAdapter2 extends ArrayAdapter<String>
{
    private final Activity context;
    private final ArrayList<String>  playerLevels;
    private final ArrayList<String>  playerNames;
    private final ArrayList<String>  Status;

    public MyListAdapter2(Activity context, ArrayList<String> playerLevels,ArrayList<String>  playerNames, ArrayList<String>  Status) {
        super(context, R.layout.mylistdashboard, playerNames);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.playerLevels = playerLevels;
        this.playerNames = playerNames;
        this.Status = Status;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylistinterestedplayers, null, true);

        TextView levelText = (TextView) rowView.findViewById(R.id.Details_Level);
        TextView nameText = (TextView) rowView.findViewById(R.id.Details_playerName);
        TextView statusText = (TextView) rowView.findViewById(R.id.Details_Status);
        ImageView statusImg = (ImageView) rowView.findViewById(R.id.Details_StatusColor);

//                <item>Interested</item>
//        <item>Coming if enough people</item>
//        <item>Confirmed</item>
//        <item>On My Way!</item>
//        <item>Ready to Jump in!</item>
//        <item>Jumped in!</item>
//        <item>No Longer Interested</item>

        String[] Expressions = getContext().getResources().getStringArray(R.array.Expression_Options);
        if(Status.get(position).compareTo(Expressions[1]) == 0)
        {
            statusImg.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.ComingIfEnoughPeopleClr));
        }
        else if (Status.get(position).compareTo(Expressions[2]) == 0)
        {
            statusImg.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.ConfirmedClr));
        }
        else if(Status.get(position).compareTo(Expressions[3]) == 0)
        {
            statusImg.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.OnMyWayClr));
        }
        else if(Status.get(position).compareTo(Expressions[4]) == 0)
        {
            statusImg.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.ReadyJumpClr));
        }
        else if(Status.get(position).compareTo(Expressions[5]) == 0)
        {
            statusImg.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.JumpedInClr));
        }
        else
        {
            statusImg.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryDark));
        }

        levelText.setText(playerLevels.get(position));
        nameText.setText(playerNames.get(position));
        statusText.setText(Status.get(position));

        return rowView;

    }
}
