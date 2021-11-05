package com.example.kadamm.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConnexioViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ConnexioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment connexió");
    }

    public LiveData<String> getText() {
        return mText;
    }
}