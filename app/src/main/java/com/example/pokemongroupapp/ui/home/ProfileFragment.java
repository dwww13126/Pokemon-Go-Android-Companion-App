package com.example.pokemongroupapp.ui.home;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pokemongroupapp.MainActivity;
import com.example.pokemongroupapp.MyListAdapter4;
import com.example.pokemongroupapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private ProfileViewModel profileViewModel;
    ArrayList<String> fav_gyms;
    MainActivity ma;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //final MainActivity ma = (MainActivity) getActivity();

        final TextView name_textview = (TextView) root.findViewById(R.id.userName_textview);//display username
        final EditText name_input = (EditText) root.findViewById(R.id.userName_editText);
        final TextView level_textview = (TextView) root.findViewById(R.id.user_level_textview);//display userlevel
        final EditText level_input = (EditText) root.findViewById(R.id.user_level_editText);
        final RadioGroup teamgroup = (RadioGroup) root.findViewById(R.id.teamSelection_group);//the team radio group
        final TextView choosed_team = (TextView) root.findViewById(R.id.choosed_team);
        final TextView team_lable_140 = (TextView) root.findViewById(R.id.textview_team_140);
        final TextView team_lable_160 = (TextView) root.findViewById(R.id.textview_team_160);
        final TextView chosed_gyms = (TextView) root.findViewById(R.id.gyms_choosed);
        final TextView gym_label = (TextView) root.findViewById(R.id.textview_gyms);
        ma = (MainActivity) getActivity();

        //when load the interface, the username, level and team is already loaded
        name_textview.setText(ma.user.getName());
        if(name_textview.getText().equals("")){name_textview.setText("Username");}
        level_textview.setText(ma.user.getLevel());
        name_input.setText(ma.user.getName());
        level_input.setText(ma.user.getLevel());
        for (int i = 0; i < teamgroup.getChildCount(); i++) {
            RadioButton r = (RadioButton) teamgroup.getChildAt(i);
            String st = r.getText().toString();
            if(st.equals(ma.user.getTeam())){
                r.setChecked(true);
                choosed_team.setText(st);
            }else r.setChecked(false);
        }


        ImageButton editBtn = (ImageButton) root.findViewById(R.id.edit_profile);
        //Sets to be invisible for current implementation issues with changing
        //username, level and team
        editBtn.setVisibility(View.GONE);
        //Also until favorite gyms is implemented these options will also be set to invisible
        chosed_gyms.setVisibility(View.GONE);
        gym_label.setVisibility(View.GONE);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if Name EditText is not visible, now you cannot edit and want to edit
                if(name_input.getVisibility() == View.INVISIBLE){
                    teamgroup.setVisibility(View.VISIBLE);
                    choosed_team.setVisibility(View.INVISIBLE);
                    name_input.setVisibility(View.VISIBLE);//make edit enable
                    name_textview.setVisibility(View.INVISIBLE);//make textview disappear
                    level_input.setVisibility(View.VISIBLE);
                    level_textview.setVisibility(View.INVISIBLE);
                    for (int i = 0; i < teamgroup.getChildCount(); i++) { //make radio btns enable
                        teamgroup.getChildAt(i).setEnabled(true);
                    }
                    team_lable_140.setVisibility(View.VISIBLE);
                    team_lable_160.setVisibility(View.INVISIBLE);
                    chosed_gyms.setVisibility(View.INVISIBLE);
                    getListView().setVisibility(View.VISIBLE);
                }
                else {//you are editing now, and want to change to confirm what you edited
                    //textEdti to textView
                    name_textview.setText(name_input.getText());
                    level_textview.setText(level_input.getText());
                    name_input.setVisibility(View.INVISIBLE);
                    name_textview.setVisibility(View.VISIBLE);
                    level_input.setVisibility(View.INVISIBLE);
                    level_textview.setVisibility(View.VISIBLE);
                    teamgroup.setEnabled(false);
                    for (int i = 0; i < teamgroup.getChildCount(); i++) {
                        teamgroup.getChildAt(i).setEnabled(false);
                    }
                    int temp_selectedId = teamgroup.getCheckedRadioButtonId();
                    RadioButton team_choosed = (RadioButton) root.findViewById(temp_selectedId);
                    ma.user.setTeam(team_choosed.getText().toString());
                    choosed_team.setText(team_choosed.getText().toString());
                    choosed_team.setVisibility(View.VISIBLE);
                    teamgroup.setVisibility(View.INVISIBLE);
                    team_lable_140.setVisibility(View.INVISIBLE);
                    team_lable_160.setVisibility(View.VISIBLE);
                    ma.user.setName(name_textview.getText().toString());
                    ma.user.setLevel(level_textview.getText().toString());
                    ArrayList<String> userGyms = ma.user.getUserGyms();
                    String userGyms_string="";
                    for(int i=0; i<userGyms.size()-1;i++){
                        userGyms_string = userGyms_string + userGyms.get(i) + ", ";
                    }
                    userGyms_string = userGyms_string + userGyms.get(userGyms.size()-1);
                    chosed_gyms.setText(userGyms_string);
                    chosed_gyms.setVisibility(View.VISIBLE);
                    getListView().setVisibility(View.GONE);
                }
            }
        });



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String[] gyms = getResources().getStringArray(R.array.user_gyms);
        ArrayList<String> usergyms = ma.user.getUserGyms();
        MyListAdapter4 gymadapter = new MyListAdapter4(getActivity(),gyms,usergyms);
        setListAdapter(gymadapter);
        getListView().setOnItemClickListener(this);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String[] gyms = getResources().getStringArray(R.array.user_gyms);
        Toast.makeText(getContext(), gyms[i], Toast.LENGTH_SHORT).show();
        if(ma.user.containGym(gyms[i])){ma.user.removeGym(gyms[i]);}
        else ma.user.addGym(gyms[i]);
        ArrayList<String> usergyms = ma.user.getUserGyms();
        MyListAdapter4 gymadapter = new MyListAdapter4(getActivity(),gyms,usergyms);
        setListAdapter(gymadapter);
    }
}