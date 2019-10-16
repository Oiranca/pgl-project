package com.oiranca.pglproject.ui.myactivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyActivityViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyActivityViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my activity fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}