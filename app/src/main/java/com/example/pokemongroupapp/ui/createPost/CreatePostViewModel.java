package com.example.pokemongroupapp.ui.createPost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreatePostViewModel extends ViewModel
{
    private MutableLiveData<String> mText;

    public CreatePostViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is create post fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
