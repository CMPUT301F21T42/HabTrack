package com.example.habtrack.ui.myday;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MydayViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MydayViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is myday fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}