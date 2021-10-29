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
 * The {@code EdithabitFragment} class
 */
package com.example.habtrack.ui.edithabit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.example.habtrack.Habit;
import com.example.habtrack.HabitHandler;
import com.example.habtrack.R;
import com.example.habtrack.databinding.FragmentEdithabitBinding;
import com.example.habtrack.databinding.FragmentEventsBinding;
import com.example.habtrack.ui.manage.ManageViewModel;

/**
 * This class is used to create a fragment that will enable the user to
 * add/delete/edit and view their habit data
 */
public class EdithabitFragment extends DialogFragment {

    private EdithabitViewModel edithabitViewModel;
    private FragmentEdithabitBinding binding;

    private EditText year, month, day;

    private EditText title;
    private EditText reason;

    private ArrayList<CheckBox> plan;

    private Habit selectedHabit;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed(Habit newHabit);
        void onOkPressed(Habit selectedHabit, Habit newHabit);
        void onDeletePressed(Habit selectedHabit);
    }

    /* Boilerplate code */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

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
     * The function returns the calender date for the input date. But if there is
     * parsing error in the date provided by the user then it returns null.
     *
     *
     * @param date string in the form of dat
     * @param inFormat Format to convert date from string format to date format.
     * @return inputDate the date entered by user in calender format.
     */
    private Calendar parseDate(String date, SimpleDateFormat inFormat) {
        Calendar inputDate = Calendar.getInstance();
        inFormat.setLenient(false);
        try {
            inputDate.setTime(inFormat.parse(date));
        } catch (ParseException e) {
            return null;
        }

        return inputDate;
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

        edithabitViewModel =
                new ViewModelProvider(this).get(EdithabitViewModel.class);

        binding = FragmentEdithabitBinding.inflate(LayoutInflater.from(getContext()));
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
            for (int i = 0; i < plan.size(); i++) {
                if (selectedHabit.getPlan(i))
                    plan.get(i).setChecked(true);
            }
        }

        year.setText(outYear.format(selectedDate.getTime()));
        month.setText(outMonth.format(selectedDate.getTime()));
        day.setText(outDay.format(selectedDate.getTime()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setView(root)
                .setTitle("Add/Delete/Edit Habit")
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedHabit == null) {
                            Toast.makeText(getContext(), "No Selected Habit", Toast.LENGTH_SHORT).show();
                        } else {
                            listener.onDeletePressed(selectedHabit);
                        }
                    }
                })
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
                        String dateInput = getString(R.string.date_formatter,
                                year.getText().toString(), month.getText().toString(), day.getText().toString());
                        ArrayList<Boolean> p = new ArrayList<>();
                        for (int i = 0; i < plan.size(); i++)
                            p.add(plan.get(i).isChecked() ? true : false);

                        if (!hh.isLegalTitle(t)) {
                            Toast.makeText(getContext(), getString((R.string.error_message), "title"), Toast.LENGTH_SHORT).show();
                        } else if (!hh.isLegalReason(r)) {
                            Toast.makeText(getContext(), getString((R.string.error_message), "reason"), Toast.LENGTH_SHORT).show();
                        } else if (!hh.isLegalStartDate(dateInput)) {
                            Toast.makeText(getContext(), getString((R.string.error_message), "date"), Toast.LENGTH_SHORT).show();
                        } else {
                            Date startD = parseDate(dateInput, inFormat).getTime();
                            listener.onOkPressed(new Habit(t, r, startD, p));

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

}