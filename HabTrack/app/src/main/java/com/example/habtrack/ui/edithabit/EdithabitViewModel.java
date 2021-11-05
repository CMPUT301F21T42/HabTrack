package com.example.habtrack.ui.edithabit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EdithabitViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EdithabitViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is events fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}