package com.example.pokemongroupapp.ui.raidDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RaidDetailsViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public RaidDetailsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Raid Details fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
