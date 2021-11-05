package com.example.kadamm.ui.usuari;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UsuariViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UsuariViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Fragment usuari");
    }

    public LiveData<String> getText() {
        return mText;
    }
}