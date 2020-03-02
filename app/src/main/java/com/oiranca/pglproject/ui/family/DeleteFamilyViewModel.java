package com.oiranca.pglproject.ui.family;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DeleteFamilyViewModel extends ViewModel {

    private MutableLiveData<String> fText;

    public DeleteFamilyViewModel() { fText = new MutableLiveData<>();
        fText.setValue("This is new family fragment");
    }

    public LiveData<String> getText() {
        return fText;
    }
}