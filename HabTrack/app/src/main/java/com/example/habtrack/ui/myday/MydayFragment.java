/** Copyright 2021 Raunak Agarwal, Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code MydayFragment} class
 */
package com.example.habtrack.ui.myday;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.OnItemClickListener;
import com.example.habtrack.databinding.FragmentMydayBinding;
import com.example.habtrack.ui.events.AddHabitEventFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * This class enables the user to view their habits for the day
 * and also check them off, once they are completed.
 */
public class MydayFragment extends Fragment {
    final String TAG = "Sample";

//    private MydayViewModel mydayViewModel;
    private FragmentMydayBinding binding;

    private ListView mydayList;
    private ArrayList<Habit> dataList;
    private ArrayAdapter<Habit> mydayAdapter;

    private FirestoreManager firestoreManager;
    
    /**
     *<p>
     *     This method creates a view which has the list of all the habits that the
     *     user is supposed to follow today.
     *</p>
     * <p>
     *     There is an additional checkbox along with the list items. It gets ticked
     *     when the user clicks on it, after completing a habit.
     * </p>
     *
     * @param inflater Layout for the screen.
     * @param container Container to keep the data in.
     * @param savedInstanceState Load the saved data of the user.
     * @return View of the page for managing today's habits.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        mydayViewModel =
//                new ViewModelProvider(this).get(MydayViewModel.class);

        binding = FragmentMydayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mydayList = binding.mydayList;
        dataList = new ArrayList<>();

        OnItemClickListener listener = new OnItemClickListener() {
            public void onItemClicked(int position) {
                Habit habit = mydayAdapter.getItem(position);
              
                new AddHabitEventFragment(habit).show(getActivity().getSupportFragmentManager(), "AddHabitEvent");
//                Toast.makeText(getContext(), "Clicked on Checkbox: " + habit.getTitle(),
//                        Toast.LENGTH_SHORT).show();

            }
        };

        mydayAdapter = new MydayAdapter(getContext(), dataList, listener);
        mydayList.setAdapter(mydayAdapter);

        refreshDataList();
        firestoreManager.setOnDataChangeListener(new FirestoreManager.OnDataChangeListener() {
            @Override
            public void onDataChange() {
                refreshDataList();
            }
        });

//        mydayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//        });

        return root;
    }

    /**
     * Compares two date objects to determine whether they are on the same day
     * @param date1 of type {@link Date}
     * @param date2 of type {@link Date}
     * @return true if they are on the same day; false otherwise
     */
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    /**
     * Updates the {@link MydayFragment#dataList} when creating the view
     * or when the {@link MydayFragment#firestoreManager}'s data changes
     */
    private void refreshDataList() {
        firestoreManager = FirestoreManager.getInstance(
                FirebaseAuth.getInstance().getCurrentUser().getUid());
        Date lastLogon = firestoreManager.getLastLogon();
        Calendar today = Calendar.getInstance();
        LocalDate end = today.getTime().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        dataList.clear();
        for (Habit habit : firestoreManager.getHabits()) {

            if (!habit.getStartDate().after(today.getTime())) {
                LocalDate start = lastLogon.after(habit.getLastScheduled()) ?
                        lastLogon.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() :
                        habit.getLastScheduled().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                // Increment the progress denominator for days without user logon
                for (LocalDate date = start.plusDays(1); date.isBefore(end); date = date.plusDays(1)) {
                    if (habit.getPlan().get(date.get(WeekFields.SUNDAY_START.dayOfWeek()) - 1)) {
                        habit.incrementProgressDenominator();
                        firestoreManager.editHabit(habit);
                    }
                }
                firestoreManager.setLastLogon(today.getTime());

                if (habit.getPlan().get(today.get(Calendar.DAY_OF_WEEK) - 1)) {
                    // Set the last scheduled date to today if not already set
                    if (!isSameDay(today.getTime(), habit.getLastScheduled())) {
                        habit.setLastScheduled(today.getTime());
                        habit.incrementProgressDenominator();
                        firestoreManager.editHabit(habit);
                    }

                    // Add habit to the my day list if it is not completed
                    if (!isSameDay(today.getTime(), habit.getLastCompleted()))
                        dataList.add(habit);
                }
            }
        }
        mydayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}