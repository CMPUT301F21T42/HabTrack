package com.example.habtrack.ui.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * This class contains functionality for events view model
 */

public class EventsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EventsViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is events fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}