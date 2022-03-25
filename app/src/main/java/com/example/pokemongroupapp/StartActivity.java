package com.example.pokemongroupapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
    }

    public void Submit(View v)
    {
        EditText nameInput = (EditText) findViewById(R.id.Intro_enterNameInput);
        EditText levelInput = (EditText) findViewById(R.id.Intro_levelInput);
        RadioGroup teamSelect = (RadioGroup) findViewById(R.id.Intro_teamSelectionRadioG);

        String userTeam;

        if(teamSelect.getCheckedRadioButtonId() == R.id.Intro_teamSelectionMystic)
        {
            userTeam = "Mystic";
        }
        else if (teamSelect.getCheckedRadioButtonId() == R.id.Intro_teamSelectionInstinct)
        {
            userTeam = "Instinct";
        }
        else
            {
                userTeam = "Valor";
            }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", nameInput.getText().toString());
        intent.putExtra("userLevel", levelInput.getText().toString());
        intent.putExtra("userTeam", userTeam);
        startActivity(intent);
    }
}
