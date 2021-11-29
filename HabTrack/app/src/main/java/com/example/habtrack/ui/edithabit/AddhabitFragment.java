/** Copyright 2021 Raunak Agarwal, Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code AddhabitFragment} class
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.HabitHandler;
import com.example.habtrack.R;
import com.example.habtrack.databinding.FragmentAddhabitBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is used to create a fragment that will enable the user to
 * add a habit
 */
public class AddhabitFragment extends DialogFragment {
    final String TAG = "Sample";

    private FragmentAddhabitBinding binding;

    private EditText year, month, day;

    private EditText title;
    private EditText reason;
    private Switch privacy;

    private ArrayList<CheckBox> plan;

    private FirestoreManager firestoreManager;

    /**
     * The function returns the calender date for the input date. But if there is
     * parsing error in the date provided by the user then it returns null.
     *
     *
     * @param inputDate string in the form of dat
     * @param inFormat Format to convert date from string format to date format.
     * @return inputDate the date entered by user in calender format.
     */
    private Date parseDate(String inputDate, SimpleDateFormat inFormat) {
        Date date = new Date();
        inFormat.setLenient(false);
        try {
            date = inFormat.parse(inputDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
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

        binding = FragmentAddhabitBinding.inflate(LayoutInflater.from(getContext()));
        View root = binding.getRoot();

        SimpleDateFormat inFormat = new SimpleDateFormat(getString(R.string.date_formatter,
                getString(R.string.year_format), getString(R.string.month_format), getString(R.string.day_format)));
        SimpleDateFormat outYear = new SimpleDateFormat(getString(R.string.year_format));
        SimpleDateFormat outMonth = new SimpleDateFormat(getString(R.string.month_format));
        SimpleDateFormat outDay = new SimpleDateFormat(getString(R.string.day_format));

        year = binding.yearEditText;
        month = binding.monthEditText;
        day = binding.dayEditText;

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

        Date selectedDate = new Date();

        year.setText(outYear.format(selectedDate.getTime()));
        month.setText(outMonth.format(selectedDate.getTime()));
        day.setText(outDay.format(selectedDate.getTime()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(root)
                .setTitle("Add Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HabitHandler hh = new HabitHandler();

                        String t = title.getText().toString();
                        String r = reason.getText().toString();
                        boolean priv = privacy.isChecked();

                        String dateInput = getString(R.string.date_formatter,
                                year.getText().toString(), month.getText().toString(), day.getText().toString());
                        ArrayList<Boolean> p = new ArrayList<>();
                        for (int i = 0; i < plan.size(); i++)
                            p.add(plan.get(i).isChecked() ? true : false);

                        if (!hh.isLegalTitle(t)) {
                            Toast.makeText(getContext(), getString((R.string.error_message), "title"), Toast.LENGTH_SHORT).show();
                        } else if (!hh.isLegalReason(r)) {
                            Toast.makeText(getContext(), getString((R.string.error_message), "reason"), Toast.LENGTH_SHORT).show();
                        } else if (!hh.isLegalDate(dateInput)) {
                            Toast.makeText(getContext(), getString((R.string.error_message), "date"), Toast.LENGTH_SHORT).show();
                        } else {
                            Date startD = parseDate(dateInput, inFormat);
                            onOkPressed(new Habit(t, r, startD, p, priv));

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
     * Adds the new habit to the firestore database
     * @param newHabit of type {@link Habit}
     */
    private void onOkPressed(Habit newHabit) {
        firestoreManager = FirestoreManager.getInstance(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        firestoreManager.addHabit(newHabit);
    }
}
