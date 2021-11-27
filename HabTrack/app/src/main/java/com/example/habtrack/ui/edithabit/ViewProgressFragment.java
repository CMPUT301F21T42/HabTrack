/** Copyright 2021 Raunak Agarwal, Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code ViewProgressFragment} class
 */
package com.example.habtrack.ui.edithabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.HabitHandler;
import com.example.habtrack.R;
import com.example.habtrack.databinding.FragmentEdithabitBinding;
import com.example.habtrack.databinding.FragmentViewprogressBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class is used to create a fragment that will enable the user to
 * view their habit progress
 */
public class ViewProgressFragment extends DialogFragment {
    final String TAG = "Sample";

//    private EdithabitViewModel edithabitViewModel;
    private FragmentViewprogressBinding binding;
    private TextView habit_name;

    private TextView completed;
    private TextView scheduled;

    private PieChart pc;

    private Habit selectedHabit;
    private FirestoreManager firestoreManager;

    /**
     * This function creates a new instance of the habit selected and displays it in the fragment.
     * @param habit instance of the Habit class object selected by user.
     * @return fragment with its data.
     */
    public static ViewProgressFragment newInstance(Habit habit) {
        Bundle args = new Bundle();
        args.putSerializable("habit", habit);

        ViewProgressFragment fragment = new ViewProgressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *  Returns a dialog object when user swipes right on an object of Habit,
     *  the clicks on the "PROGRESS" button.
     *  <p>
     *  This function opens up a fragment, extracts the progress information of the specific habit
     *  of the respective user and displays it to the user.
     *
     * @param savedInstanceState instance of the habit bundle.
     *
     * @return Dialog object
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentViewprogressBinding.inflate(LayoutInflater.from(getContext()));
        View root = binding.getRoot();

        completed = binding.completedTextView;
        scheduled = binding.scheduledTextView;

        pc = binding.piechart;
        habit_name = binding.habitName;


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selectedHabit = (Habit) bundle.getSerializable("habit");
            habit_name.setText(String.valueOf(selectedHabit.getTitle()));
            completed.setText(String.valueOf(selectedHabit.getProgressNumerator()));
            scheduled.setText(String.valueOf(selectedHabit.getProgressDenominator()));
            if(selectedHabit.getProgressDenominator()!=0){
                int comp_ratio = 100*selectedHabit.getProgressNumerator()/selectedHabit.getProgressDenominator();
                int incomp_ratio = 100 -comp_ratio;


                pc.addPieSlice(
                        new PieModel(
                                "Completed",
                                comp_ratio,
                                Color.parseColor("#14DF69")));

                pc.addPieSlice(
                        new PieModel(
                                "Incompleted",
                                incomp_ratio,
                                Color.parseColor("#000000")));
            }
            else{
                pc.addPieSlice(
                        new PieModel(
                                "Incompleted",
                                100,
                                Color.parseColor("#000000")));
            }


        }

        AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(root)
                .setTitle("View Habit Progress")
                .setPositiveButton("OK", null)
                .create();

        return dialog;
    }

}
