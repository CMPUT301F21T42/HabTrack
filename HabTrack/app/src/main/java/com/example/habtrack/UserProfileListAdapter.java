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
            convertView = layoutInflater.inflate(R.layout.content_friend_profile, null);
        }

        Habit habit = habitDataList.get(position);
        TextView userNameText = convertView.findViewById(R.id.habit_title);
        userNameText.setText(habit.getTitle());

        ProgressBar progress = convertView.findViewById(R.id.habit_progress);

        progress.setProgress(habit.getProgress());

        return convertView;
    }
}
