package com.oiranca.pglproject.ui.family;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewFamilyViewModel extends ViewModel {

    private MutableLiveData<String> fText;

    public NewFamilyViewModel() { fText = new MutableLiveData<>();
        fText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return fText;
    }
}