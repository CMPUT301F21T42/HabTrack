/** Copyright 2021 Raunak Agarwal, Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code EdithabitFragment} class
 */
package com.example.habtrack.ui.edithabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import androidx.lifecycle.ViewModelProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.HabitHandler;
import com.example.habtrack.R;
import com.example.habtrack.databinding.FragmentEdithabitBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class is used to create a fragment that will enable the user to
 * edit/delete and view their habit data
 */
public class EdithabitFragment extends DialogFragment {
    final String TAG = "Sample";

//    private EdithabitViewModel edithabitViewModel;
    private FragmentEdithabitBinding binding;

    private TextView date;

    private EditText title;
    private EditText reason;
    private Switch privacy;

    private ArrayList<CheckBox> plan;

    private Habit selectedHabit;
    private FirestoreManager firestoreManager;

    /**
     * This function creates a new instance of the habit selected and displays it in the fragment.
     * @param habit instance of the Habit class object selected by user.
     * @return fragment with its data.
     */
    public static EdithabitFragment newInstance(Habit habit) {
        Bundle args = new Bundle();
        args.putSerializable("habit", habit);

        EdithabitFragment fragment = new EdithabitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *  Returns a dialog object when user clicks on an object of Habit.
     *  <p>
     *  This function opens up a fragment, extracts the information of the specific habit
     *  of the respective user and displays it to the user for editing, deleting or just viewing
     *  the habit and its attributes.
     *  </p>
     *  <p>
     *      The user can edit the edit all the attributes of the habit on the same fragment
     *      by clicking on the specific field to edit.
     *  </p>
     *
     * @param savedInstanceState instance of the habit bundle.
     *
     * @return Dialog object
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

//        edithabitViewModel =
//                new ViewModelProvider(this).get(EdithabitViewModel.class);

        binding = FragmentEdithabitBinding.inflate(LayoutInflater.from(getContext()));
        View root = binding.getRoot();

        date = binding.dateTextView;

        title = binding.titleEditText;
        reason = binding.reasonEditText;
        privacy = binding.ispublicSwitch;

        plan = new ArrayList<>();
        plan.add(binding.sundayCheckBox);
        plan.add(binding.mondayCheckBox);
        plan.add(binding.tuesdayCheckBox);
        plan.add(binding.wednesdayCheckBox);
        plan.add(binding.thursdayCheckBox);
        plan.add(binding.fridayCheckBox);
        plan.add(binding.saturdayCheckBox);

        Date selectedDate = Calendar.getInstance().getTime();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selectedHabit = (Habit) bundle.getSerializable("habit");
            selectedDate = selectedHabit.getStartDate();
            title.setText(selectedHabit.getTitle());
            reason.setText(selectedHabit.getReason());
            if (selectedHabit.isPublic())
                privacy.setChecked(true);
            for (int i = 0; i < plan.size(); i++) {
                if (selectedHabit.getPlan(i))
                    plan.get(i).setChecked(true);
            }
        }

        SimpleDateFormat outDate = new SimpleDateFormat(getString(R.string.date_formatter,
                getString(R.string.year_format),
                getString(R.string.month_format),
                getString(R.string.day_format)));

        date.setText(getString(R.string.date_display, outDate.format(selectedDate.getTime())));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(root)
                .setTitle("Edit/Delete Habit")
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedHabit == null) {
                            Toast.makeText(getContext(), "No Selected Habit", Toast.LENGTH_SHORT).show();
                        } else {
                            onDeletePressed(selectedHabit);
                        }
                    }
                })
                .setPositiveButton("OK", null).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HabitHandler hh = new HabitHandler();

                        String t = title.getText().toString();
                        String r = reason.getText().toString();
                        boolean priv = privacy.isChecked();

                        ArrayList<Boolean> p = new ArrayList<>();
                        for (int i = 0; i < plan.size(); i++)
                            p.add(plan.get(i).isChecked() ? true : false);

                        if (!hh.isLegalTitle(t)) {
                            Toast.makeText(getContext(), getString((R.string.error_message), "title"), Toast.LENGTH_SHORT).show();
                        } else if (!hh.isLegalReason(r)) {
                            Toast.makeText(getContext(), getString((R.string.error_message), "reason"), Toast.LENGTH_SHORT).show();
                        } else {
                            selectedHabit.setTitle(t);
                            selectedHabit.setReason(r);
                            selectedHabit.setPublic(priv);
                            selectedHabit.setPlan(p);
                            onOkPressed(selectedHabit);

//                            if (selectedHabit == null) {
//                                listener.onOkPressed(new Habit(t, r, startD));
//                            } else {
//                                // need to modify to save progress
//                                listener.onOkPressed(selectedHabit, new Habit(t, r, startD));
//                            }

                            //Dismiss once everything is OK.
                            dialog.dismiss();
                        }

                    }
                });

            }
        });

        return dialog;
    }

    /**
     * Updates the selected habit in the firestore database
     * @param updatedHabit of type {@link Habit}
     */
    public void onOkPressed(Habit updatedHabit) {
        firestoreManager = FirestoreManager.getInstance(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        firestoreManager.editHabit(updatedHabit);
    }

    /**
     * Removes the selected habit in the firestore database
     * @param selectedHabit of type {@link Habit}
     */
    public void onDeletePressed(Habit selectedHabit) {
        firestoreManager = FirestoreManager.getInstance(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        firestoreManager.deleteHabit(selectedHabit);
    }

}
