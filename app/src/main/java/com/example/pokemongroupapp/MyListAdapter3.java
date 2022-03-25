package com.example.pokemongroupapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyListAdapter3 extends ArrayAdapter<String>
{
    private final Activity context;
    private final String[] Status;

    public MyListAdapter3(Activity context, String[] Status) {
        super(context, R.layout.mylistdashboard, Status);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.Status = Status;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.myliststatuschange, null, true);

        TextView statusText = (TextView) rowView.findViewById(R.id.Details_StatusChoose);
        //ImageButton statusImg = (ImageButton)rowView.findViewById(R.id.Details_listStatusColorChoose);

        statusText.setText(Status[position]);

        return rowView;

    }
}
