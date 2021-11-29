/*
 * UserProfileListAdapter
 *
 * UserProfileListAdapter extends the ArrayAdapter class that helps connect the data (stored in a list)
 * to the UI.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * November 29, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class is an ArrayAdapter for a list of habits belonging to a user. It helps connect the
 * data to the UI.
 *
 * @author Jenish
 * @see UserProfileActivity
 * @version 1.0
 * @since 1.0
 */
public class UserProfileListAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habitDataList;
    private UserProfileActivity context;
    private final String TAG = "Sample";


    public UserProfileListAdapter(@NonNull UserProfileActivity context, ArrayList<Habit> habitDataList) {
        super(context, R.layout.content_search_users, habitDataList);
        this.habitDataList = habitDataList;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_user_profile, null);
        }

        Habit habit = habitDataList.get(position);
        TextView userNameText = convertView.findViewById(R.id.habit_title);
        userNameText.setText(habit.getTitle());

        ProgressBar progress = convertView.findViewById(R.id.habit_progress);

        progress.setProgress(habit.getProgress());

        return convertView;
    }
}
