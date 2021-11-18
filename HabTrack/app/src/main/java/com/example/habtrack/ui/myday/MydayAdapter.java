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
 * The {@code MyDayAdapter} class
 * To enable the list view of habits with checkbox
 */

package com.example.habtrack.ui.myday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habtrack.Habit;
import com.example.habtrack.OnItemClickListener;
import com.example.habtrack.R;

import java.util.ArrayList;

public class MydayAdapter extends ArrayAdapter<Habit> {
    private ArrayList<Habit> habits;
    private Context context;
    private OnItemClickListener onClickListener;

    /**
     * Constructs an adapter with
     * @param context the current context
     * @param habits the arrayList of habits {@link Habit}
     * @param onClickListener used to get the position of a list item that is clicked
     */
    public MydayAdapter(Context context, ArrayList<Habit> habits, OnItemClickListener onClickListener) {
        super(context, 0, habits);
        this.habits = habits;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    /**
     * Get a View that displays the checkbox and the habit title at the specified
     * @param position in the data set.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_myday, parent, false);
        }

        Habit habit = habits.get(position);

        CheckBox check = view.findViewById(R.id.myday_checkBox);

        TextView title = view.findViewById(R.id.myday_title_text);
        title.setText(habit.getTitle());

        ProgressBar simpleProgressBar= (ProgressBar) view.findViewById(R.id.myday_progressBar);
        simpleProgressBar.setMax(100); // 100 maximum value for the progress value
        simpleProgressBar.setProgress(habit.getProgress());

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onItemClicked(position);
                    check.setChecked(false);
                }
            }
        });

        return view;
    }
}
