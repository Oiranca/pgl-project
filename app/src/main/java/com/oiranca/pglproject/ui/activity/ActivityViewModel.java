package com.oiranca.pglproject.ui.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ActivityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ActivityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Poner el nombre del usuario");
    }

    public LiveData<String> getText() {
        return mText;
    }
}