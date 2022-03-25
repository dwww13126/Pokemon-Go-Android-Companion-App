package com.example.pokemongroupapp.ui.hostOptions;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pokemongroupapp.MainActivity;
import com.example.pokemongroupapp.R;
import com.example.pokemongroupapp.RaidData;
import com.example.pokemongroupapp.ui.raidDetails.RaidDetailsFragment;

public class HostOptionsFragment extends Fragment
{
    private HostOptionsViewModel hostOptionsViewModel;
    RaidData raid;
    TimePicker timeSpinner;
    View root;
    MainActivity ma;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_hostoptions, container, false);
        raid = (RaidData) getArguments().getSerializable("raid");
        timeSpinner = (TimePicker) root.findViewById(R.id.hostoption_timeSpinner);
        ma = (MainActivity)getActivity();
        setLayout();
        TextView deleteButtonText = (TextView) root.findViewById(R.id.hostoption_deletePostText);
        ImageButton deleteButtonImg = (ImageButton) root.findViewById(R.id.hostoption_deletePostBtn);

        //Delete ONCLICK
        deleteButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemovePost();
            }
        });
        deleteButtonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemovePost();
            }
        });

        TextView updateButtonText = (TextView) root.findViewById(R.id.hostoption_UpdatePostText);
        ImageButton updateButtonImg = (ImageButton) root.findViewById(R.id.hostoption_UpdatePostBtn);

        //Update ONCLICK
        updateButtonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateInfo();
            }
        });
        updateButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateInfo();
            }
        });

        TextView confirmText = (TextView) root.findViewById(R.id.hostoption_ConfirmRaidText);
        ImageButton confirmButton = (ImageButton) root.findViewById(R.id.hostoption_ConfirmRaidButton);

        //Confirm Raid ONCLICK
        confirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetConfirmed();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetConfirmed();
            }
        });

        TextView jumpInButtonText = (TextView) root.findViewById(R.id.hostoption_jumpInButtonText);
        ImageButton jumpInButton = (ImageButton) root.findViewById(R.id.hostoption_jumpInButton);

        //Jump In ONCLICK
        jumpInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raid.setJumpedIn(true);
                SetBattleTime();
            }
        });
        jumpInButtonText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                raid.setJumpedIn(true);
                SetBattleTime();
            }
        });

        return root;
    }

    public void RemovePost()
    {
        ma.RaidUpdateDetailsPost(raid.getRaidTime(), raid.getName(), raid.getHost().getPlayer().getName(), raid.getBattleTime(),
                raid.getHatchTime(), raid.getCertainty(), raid.getJumpTime(), raid.getPokemon(), "Y");
    }

    public void UpdateInfo()
    {
        EditText pokemonInput = (EditText) root.findViewById(R.id.hostoption_pokemonInput);
        String pokemon = pokemonInput.getText().toString();


        Button timebox = (Button)root.findViewById(R.id.CreatePost_timeBox2);
        String timeboxtext = timebox.getText().toString();
        if (timeboxtext.compareTo("Set Time") == 0) {
                ma.toast("Please enter a time!");
        }
        else {

            String[] split = timeboxtext.split(":");
            int hour = Integer.parseInt(split[0]);
            int min = Integer.parseInt(split[1]);
            String time = ma.formatTime(hour, min);

            ma.RaidUpdateDetailsPost(raid.getRaidTime(), raid.getName(), raid.getHost().getPlayer().getName(), raid.getBattleTime(),
                    raid.getHatchTime(), raid.getCertainty(), time, pokemon, "N");
        }




/*        Bundle bundle = new Bundle();
        bundle.putSerializable("raid", raid);
        bundle.putSerializable("user", ma.user);
        Fragment fragment = new RaidDetailsFragment();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();*/
    }

    public void SetConfirmed()
    {
        //Allows the button to determine if it needs to set jump time or if it is just confirming a
        //raid which was posted with the jump time initially
        if (raid.getJumpTime().compareTo("NONE") == 0) {

            Button timebox = (Button)root.findViewById(R.id.CreatePost_timeBox2);
            String timeboxtext = timebox.getText().toString();
            if (timeboxtext.compareTo("Set Time") == 0) {
                ma.toast("Please enter a time!");
            }
            else {
                String[] split = timeboxtext.split(":");
                int hour = Integer.parseInt(split[0]);
                int min = Integer.parseInt(split[1]);
                String time = ma.formatTime(hour, min);

                ma.RaidUpdateDetailsPost(raid.getRaidTime(), raid.getName(), raid.getHost().getPlayer().getName(), raid.getBattleTime(),
                        raid.getHatchTime(), "C", time, raid.getPokemon(), "N");
            }
        }
        else{
            ma.RaidUpdateDetailsPost(raid.getRaidTime(), raid.getName(), raid.getHost().getPlayer().getName(), raid.getBattleTime(),
                    raid.getHatchTime(), "C", raid.getJumpTime(), raid.getPokemon(), "N");
        }
        //raid.setConfirmed(true);
        //ma.openDashboard();
    }

    public void SetBattleTime()
    {
        Button timebox = (Button)root.findViewById(R.id.CreatePost_timeBox2);
        String timeboxtext = timebox.getText().toString();
        if (timeboxtext.compareTo("Set Time") == 0) {
            ma.toast("Please enter a time!");
        }
        else {
            String[] split = timeboxtext.split(":");
            int hour = Integer.parseInt(split[0]);
            int min = Integer.parseInt(split[1]);
            String time = ma.formatTime(hour, min);

            ma.RaidUpdateDetailsPost(raid.getRaidTime(), raid.getName(), raid.getHost().getPlayer().getName(), time,
                    raid.getHatchTime(), raid.getCertainty(), raid.getJumpTime(), raid.getPokemon(), "N");
        }
        //raid.setConfirmed(true);
        //ma.openDashboard();
    }

    @SuppressLint("SetTextI18n")
    public void setLayout()
    {
        TextView subtitle = (TextView) root.findViewById(R.id.hostoption_time);
        TextView timeText = (TextView) root.findViewById(R.id.hostoption_hatchedTime);
        EditText pokemonInput = (EditText) root.findViewById(R.id.hostoption_pokemonInput);
        TextView pokemonText = (TextView) root.findViewById(R.id.hostoption_pokemonNameText);
        Button timebox = (Button)root.findViewById(R.id.CreatePost_timeBox2);
        //Sets the pokemon to be inserted into the text field
        pokemonInput.setText(raid.getPokemon());
        RelativeLayout UpdateBtn = (RelativeLayout) root.findViewById(R.id.hostoption_UpdatePostGroup);
        RelativeLayout SendNotification = (RelativeLayout) root.findViewById(R.id.hostoption_jumpInButtonGroup);
        RelativeLayout confirmButton = (RelativeLayout) root.findViewById(R.id.hostoption_ConfirmRaidGroup);
        UpdateBtn.setVisibility(View.GONE);
        pokemonInput.setVisibility(View.GONE);
        pokemonText.setVisibility(View.GONE);
        SendNotification.setVisibility(View.GONE);
        confirmButton.setVisibility(View.GONE);
        //Checks the details of the raid to know what text needs to be displayed to either
        //update the jump in time or raid end expiry
        if(raid.getPokemon().compareTo("Unknown") == 0){
            subtitle.setText("Update to Hatched Status");
            timeText.setText("Jump In Time");
            pokemonInput.setVisibility(View.VISIBLE);
            pokemonText.setVisibility(View.VISIBLE);
            UpdateBtn.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.GONE);
        }
        //Checks if the raid is confirmed
        else if (raid.getBattleTime().compareTo("NONE") == 0 && raid.getCertainty().compareTo("C") != 0){
            subtitle.setText("Confirm Raid Before Beginning");
            timeText.setText("");
            timebox.setVisibility(View.GONE);
            confirmButton.setVisibility(View.VISIBLE);
        }
        else if(raid.getBattleTime().compareTo("NONE") == 0) {
            subtitle.setText("Set Before Beginning Raid");
            timeText.setText("Group Finish Time:");
            SendNotification.setVisibility(View.VISIBLE);
        }
        if(raid.isConfirmed() == true)
        {
            confirmButton.setVisibility(View.GONE);
        }
        if(raid.isJumpedIn())
        {
            confirmButton.setVisibility(View.GONE);
            subtitle.setText("Delete the Raid");
            timeText.setVisibility(View.GONE);
            SendNotification.setVisibility(View.GONE);
            timebox.setVisibility(View.GONE);
        }
    }
}
