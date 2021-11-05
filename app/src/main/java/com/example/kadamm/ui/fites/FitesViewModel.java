package com.example.kadamm.ui.fites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FitesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public FitesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment fites");
    }

    public LiveData<String> getText() {
        return mText;
    }
}