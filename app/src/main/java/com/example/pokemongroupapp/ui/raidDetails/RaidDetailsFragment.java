package com.example.pokemongroupapp.ui.raidDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.example.pokemongroupapp.InterestData;
import com.example.pokemongroupapp.MainActivity;
import com.example.pokemongroupapp.MyListAdapter2;
import com.example.pokemongroupapp.MyListAdapter3;
import com.example.pokemongroupapp.PlayerData;
import com.example.pokemongroupapp.R;
import com.example.pokemongroupapp.RaidData;
import com.example.pokemongroupapp.ui.hostOptions.HostOptionsFragment;

import java.util.ArrayList;

public class RaidDetailsFragment extends ListFragment implements AdapterView.OnItemClickListener
{
    private RaidDetailsViewModel raidDetailsViewModel;
    RaidData raid;
    ArrayList<String> playerLevel;
    ArrayList<String> playerNames;
    ArrayList<String> playerStatus;
    boolean ListEnabled = false;
    TextView listShowText;
    ImageButton listShow;
    TextView listShowText2;
    ImageButton listShow2;
    String[] statues;
    PlayerData enteringUser;
    int activeList;
    View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_raiddetails, container, false);

        raid = (RaidData) getArguments().getSerializable("raid");
        enteringUser = (PlayerData)getArguments().getSerializable("user");


        TextView raidName = (TextView) root.findViewById(R.id.Details_raidName);
        TextView raidPokemon = (TextView) root.findViewById(R.id.Details_PokemonName);
        RatingBar rating = (RatingBar) root.findViewById(R.id.Details_Rating);

        raidPokemon.setText("Pokemon: " + raid.getPokemon());
        setTimeLayout();



        raidName.setText(raid.getName());
        rating.setRating(raid.getStars());

        playerLevel = new ArrayList<String>();
        playerNames = new ArrayList<String>();
        playerStatus = new ArrayList<String>();

        for(int i=0; i<raid.getRaidPlayerCount();i++)
        {
            playerLevel.add(raid.getPlayer(i).getPlayer().getLevel());
            playerNames.add(raid.getPlayer(i).getPlayer().getName());
            playerStatus.add(raid.getPlayer(i).getStatus());
            System.out.println(playerLevel + "," + playerNames + "," + playerStatus);
        }
        System.out.println(playerNames.size());

        refreshButtons();

        return root;
    }

    public void refreshButtons()
    {
        if(!enteringUser.sameAs(raid.getHost().getPlayer()))
        {
            RelativeLayout hostOptions = (RelativeLayout) root.findViewById(R.id.Details_HostOptionsButtonGroup);
            hostOptions.setVisibility(View.GONE);
        }

        if(raid.isInterested(enteringUser))
        {
            TextView interestPrompt = (TextView) root.findViewById(R.id.Details_ExpressInterestText);
            interestPrompt.setText("Change Interest Status");
        }
        else
            {
                TextView interestPrompt = (TextView) root.findViewById(R.id.Details_ExpressInterestText);
                interestPrompt.setText("Express Interest");
            }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MyListAdapter2 adapter = new MyListAdapter2(getActivity(), playerNames,playerLevel, playerStatus);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        activeList = 0;

        ImageButton hostOptionsutton = (ImageButton)getView().findViewById(R.id.Details_HostOptionsButton);
        TextView hostOptionsText = (TextView) getView().findViewById(R.id.Details_HostOptionsText);


        listShow = (ImageButton)getView().findViewById(R.id.Details_ShowList);
        listShowText = (TextView)getView().findViewById(R.id.Details_ShowListText);

        listShow2 = (ImageButton)getView().findViewById(R.id.Details_ExpressInterestBtn);
        listShowText2 = (TextView)getView().findViewById(R.id.Details_ExpressInterestText);


        //ArrayList containing all the possible Interestsx
        statues = getResources().getStringArray(R.array.Expression_Options);

        //EXPRESS INTEREST BUTTON INTERACTION ONCLICKS
        //
        listShow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean listChanged = ToggleList(2);
                if(listChanged == true)
                {
                    expressInterestChange();
                }
            }
        });

        listShowText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean listChanged = ToggleList(2);

                if(listChanged = true)
                {
                    expressInterestChange();
                }
            }
        });
        //EXPRESS INTEREST BUTTON INTERACTION ONCLICKS


        //SHOW INTERESTED LIST INTERACTION ONCLICKS
        //
        listShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean listChanged = ToggleList(1);
                if(listChanged == true)
                {
                    showInterestChange();
                }
            }
        });

        listShowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean listChanged = ToggleList(1);
                if(listChanged == true)
                {
                    showInterestChange();
                }
            }
        });
        //SHOW INTERESTED LIST INTERACTION ONCLICKS

        //DELETE POST INTERACTION ONCLICKS
        hostOptionsutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHostOptions();
            }
        });
        hostOptionsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHostOptions();
            }
        });
        //DELETE POST INTERACTION ONCLICKS
    }

    public void openHostOptions()
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("raid", raid);
        Fragment fragment = new HostOptionsFragment();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }

    public void expressInterestChange()
    {
        if(activeList == 2)
        {
            MyListAdapter3 adapter = new MyListAdapter3(getActivity(), statues);
            setListAdapter(adapter);
            getListView().setOnItemClickListener(this);
            //refreshPage();
        }
    }

    public void showInterestChange()
    {
        if(activeList == 1)
        {
            MyListAdapter2 adapter = new MyListAdapter2(getActivity(), playerNames,playerLevel, playerStatus);
            setListAdapter(adapter);
            getListView().setOnItemClickListener(this);
            //refreshPage();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        MainActivity main = (MainActivity)getActivity();

        System.out.println(activeList + " is Clicked!");
        if(activeList == 1)
        {
            String out = raid.getPlayer(i) + " is " + raid.getPlayer(i).getStatus();
            Toast.makeText(getContext(), out,Toast.LENGTH_LONG).show();
        }
        else if (activeList == 2)
        {
            //Send message to all devices about new interest
            //RI~<<TIME>~<LOCATION>~<HOST>~<USER>~<LEVEL>~<TEAM>~<STATUS>~
            main.RaidUserInterest(raid.getRaidTime(), raid.getName(), raid.getHost(), i);

            //
            //if(raid.isInterested(enteringUser))
            //{
            //    raid.changeInterest(enteringUser, i);
            //    Toast.makeText(getContext(), "Raid Count: " + raid.getRaidPlayerCount(), Toast.LENGTH_SHORT).show();
            //}
            //else
            //{
            //    raid.addPlayer(new InterestData(getContext(),enteringUser, i));
            //    Toast.makeText(getContext(), "User Added!", Toast.LENGTH_SHORT).show();
            //}
            //
        }
        refreshFragment();
    }

    private boolean ToggleList(int list)
    {
        System.out.println("active: " +  activeList + " " + "looking: " + list);
        if(activeList == 0 && list == 1)
        {
            getListView().setVisibility(View.VISIBLE);
            activeList = list;
            listShowText.setText("Hide Interested Players");
            return true;
        }
        else if(activeList == 0 && list == 2)
        {
            getListView().setVisibility(View.VISIBLE);
            activeList = list;
            listShowText2.setText("Cancel");
            return true;
        }
        else if(list == 2 && activeList == 2)
        {
            getListView().setVisibility(View.GONE);
            activeList = 0;
            refreshButtons();
            return true;
        }
        else if(list == 1 && activeList == 1)
        {
            getListView().setVisibility(View.GONE);
            activeList = 0;
            listShowText.setText("Show Interested Players");
            return true;
        }
        else return false;
    }

    public void refreshFragment()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    public void setTimeLayout()
    {
        TextView time = (TextView) root.findViewById(R.id.Details_Time);
        //Flag 1 - Pokemon has Hatched
        if (raid.getBattleTime().compareTo("NONE") != 0)
        {
            time.setText("Group finishing at: " + raid.getBattleTime());
        }
        else if(raid.getJumpTime().compareTo("NONE") != 0)
        {
            time.setText("Jump in time: " + raid.getJumpTime());
        }
        else if(raid.getHatchTime().compareTo("NONE") != 0)
        {
            time.setText("Hatching at: " + raid.getRaidTime());
        }
    }
}
