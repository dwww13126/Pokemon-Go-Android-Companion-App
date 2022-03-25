package com.example.pokemongroupapp.ui.hostOptions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HostOptionsViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public HostOptionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Host Options fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
