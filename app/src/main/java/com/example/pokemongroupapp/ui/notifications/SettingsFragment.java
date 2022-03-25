package com.example.pokemongroupapp.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pokemongroupapp.MainActivity;
import com.example.pokemongroupapp.R;

import java.lang.reflect.Array;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        Switch switch1 = (Switch) root.findViewById(R.id.notif_switch1);
        Switch switch2 = (Switch) root.findViewById(R.id.notif_switch2);
        Switch switch3 = (Switch) root.findViewById(R.id.notif_switch3);
        Switch switch4 = (Switch) root.findViewById(R.id.notif_switch4);
        Switch switch5 = (Switch) root.findViewById(R.id.notif_switch5);
        Switch switch6 = (Switch) root.findViewById(R.id.notif_switch6);
        Switch switch7 = (Switch) root.findViewById(R.id.notif_switch7);
        ConstraintLayout c1 = (ConstraintLayout) root.findViewById(R.id.Constraint_notify_1);
        ConstraintLayout c2 = (ConstraintLayout) root.findViewById(R.id.Constraint_notify_2);
        ConstraintLayout c6 = (ConstraintLayout) root.findViewById(R.id.Constraint_notify_6);
        ConstraintLayout c7 = (ConstraintLayout) root.findViewById(R.id.Constraint_notify_7);
        Switch switchs[] ={switch1,switch2,switch3,switch4,switch5,switch6,switch7};
        switch3.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                //send notifications
                ((MainActivity)getActivity()).notif_sending[2] = true;
            }
            else{
                //do not send notifications
                ((MainActivity)getActivity()).notif_sending[2] = false;
            }
        });
        switch4.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                //send notifications
                ((MainActivity)getActivity()).notif_sending[3] = true;
            }
            else{
                //do not send notifications
                ((MainActivity)getActivity()).notif_sending[3] = false;
            }
        });
        switch5.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                //send notifications
                ((MainActivity)getActivity()).notif_sending[4] = true;
            }
            else{
                //do not send notifications
                ((MainActivity)getActivity()).notif_sending[4] = false;
            }
        });
        //Sets all the switches to be as set by the array of booleans in main
        boolean switchState[] = ((MainActivity)getActivity()).notif_sending;
        //Goes through a loop of setting each state
        for (int i = 0; i <= 6; i++){
            switchs[i].setChecked(switchState[i]);
        }
        //Sets the switches currently not implemented to be non visible
        //(everything other than 3,4,5)
        c1.setVisibility(View.GONE);
        c2.setVisibility(View.GONE);
        c6.setVisibility(View.GONE);
        c7.setVisibility(View.GONE);
        return root;
    }
}