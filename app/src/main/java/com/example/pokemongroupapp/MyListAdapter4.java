package com.example.pokemongroupapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class MyListAdapter4 extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] Gyms;
    private final ArrayList<String> userGyms;

    public MyListAdapter4(Activity context, String[] Gyms, ArrayList<String> usergyms) {
        super(context, R.layout.mylistdashboard, Gyms);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.Gyms = Gyms;
        this.userGyms = usergyms;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.myliststatuschange, null, true);

        TextView statusText = (TextView) rowView.findViewById(R.id.Details_StatusChoose);
        ImageView singleItem = (ImageView) rowView.findViewById(R.id.Details_StatusSingle);
        //ImageButton statusImg = (ImageButton)rowView.findViewById(R.id.Details_listStatusColorChoose);


        if(userGyms.contains(Gyms[position]))
        {
            singleItem.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.ConfirmedClr));
        }

        statusText.setText(Gyms[position]);

        return rowView;

    }
}
