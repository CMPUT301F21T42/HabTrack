package com.example.habtrack.ui.manage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.habtrack.Habit;
import com.example.habtrack.R;

import java.util.ArrayList;

public class ManageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ManageViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is manage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}