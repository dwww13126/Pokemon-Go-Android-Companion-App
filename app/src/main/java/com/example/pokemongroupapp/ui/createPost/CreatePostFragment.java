package com.example.pokemongroupapp.ui.createPost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pokemongroupapp.InterestData;
import com.example.pokemongroupapp.R;
import com.example.pokemongroupapp.RaidData;
import com.example.pokemongroupapp.ui.dashboard.DashboardViewModel;
import com.google.android.material.chip.ChipGroup;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePostFragment extends Fragment implements AdapterView.OnItemSelectedListener
{
    private CreatePostViewModel createPostViewModel;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_createpost, container, false);

        RadioGroup rg = (RadioGroup) root.findViewById(R.id.CreatePost_eggHatched);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                System.out.println("Clicked!");
                TextView pokemonInput = (TextView) root.findViewById(R.id.CreatePost_pokemonTitle);
                TextView timeDescription = (TextView) root.findViewById(R.id.CreatePost_Time);
                EditText pokemonEdit = (EditText) root.findViewById(R.id.CreatePost_Pokemon_Input);

                if (radioGroup.getCheckedRadioButtonId() == R.id.CreatePost_hatchedYes)
                {
                    timeDescription.setText("Jump In Time");
                    pokemonInput.setVisibility(View.VISIBLE);
                    pokemonEdit.setVisibility(View.VISIBLE);
                }
                else
                    {
                        timeDescription.setText("Hatching Time");
                        pokemonInput.setVisibility(View.GONE);
                        pokemonEdit.setVisibility(View.GONE);
                    }
            }
        });

        List<String> gymNames = Arrays.asList(getContext().getResources().getStringArray(R.array.user_gyms));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinneritems, gymNames);
        Spinner spinner = (Spinner) root.findViewById(R.id.CreatePost_Spinner);
        spinner.setOnItemSelectedListener(this);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
