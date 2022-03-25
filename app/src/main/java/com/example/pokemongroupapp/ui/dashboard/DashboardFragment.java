package com.example.pokemongroupapp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pokemongroupapp.MainActivity;
import com.example.pokemongroupapp.MyListAdapter;
import com.example.pokemongroupapp.PlayerData;
import com.example.pokemongroupapp.R;
import com.example.pokemongroupapp.RaidData;
import com.example.pokemongroupapp.ui.raidDetails.RaidDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private DashboardViewModel dashboardViewModel;
    ArrayList<String> raidName;
    ArrayList<RaidData> raids;
    PlayerData user;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        //System.out.println(raidName.get(1));

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        raids = (ArrayList<RaidData>) getArguments().getSerializable("raids");

        TextView userName = (TextView) root.findViewById(R.id.Dash_userDisplay);
        TextView userLevel = (TextView) root.findViewById(R.id.Dash_userLevel);

        user = (PlayerData) getArguments().getSerializable("user");

        userName.setText(user.getName());
        userLevel.setText(user.getLevel());

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        raidName = new ArrayList<String>();

        for(RaidData r : raids)
        {
            raidName.add(r.getName());
        }

        MainActivity ma = (MainActivity) getActivity();
        MyListAdapter adapter = new MyListAdapter(getActivity(), raidName, raids);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("raid", raids.get(i));
        bundle.putSerializable("user", user);
        Fragment fragment = new RaidDetailsFragment();
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();

    }
}