/** Copyright 2021
 * Raunak Agarwal, Revanth Atmakuri, Mattheas Jamieson,
 * Jenish Patel, Jasmine Wadhwa, Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code HabitListAdapter} class
 * To enable the list view of habits with checkbox
 */

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

import com.example.habtrack.Habit;
import com.example.habtrack.R;

import java.util.ArrayList;

public class HabitListAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habits;
    private Context context;

    /**
     * Constructs an adapter with
     * @param context the current context
     * @param habits the arrayList of habits {@link Habit}
     */
    public HabitListAdapter(Context context, ArrayList<Habit> habits) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * Get a View that displays the habit title and progress at the specified
     * @param position in the data set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_habitlist, parent, false);
        }

        Habit habit = habits.get(position);

        TextView title = view.findViewById(R.id.title_text);
        title.setText(habit.getTitle());

        ProgressBar simpleProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        simpleProgressBar.setMax(100); // 100 maximum value for the progress value
        simpleProgressBar.setProgress(habit.getProgress());

        return view;

    }
}
