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
 * The {@code Habit} class
 */

package com.example.habtrack.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.habtrack.EventsAdapter;
import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.HabitEvents;
import com.example.habtrack.HabitHandler;
import com.example.habtrack.OnItemClickListener;
import com.example.habtrack.R;
import com.example.habtrack.databinding.FragmentEventsBinding;
import com.example.habtrack.ui.edithabit.EdithabitFragment;
import com.example.habtrack.ui.myday.MydayFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class stores and displays the list of habit events
 */

public class EventsFragment extends Fragment {

    //    private EventsViewModel eventsViewModel;
    private FragmentEventsBinding binding;

    private EditText year, month, day;
    private EditText title;

    private ListView eventList;
    private ArrayList<HabitEvents> dataList;
    private ArrayAdapter<HabitEvents> eventAdapter;

    HabitHandler hh = new HabitHandler();

    FirebaseFirestore db;

    /**
     * This method adds the new habit to the events list view and updates the events adapter
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        eventsViewModel =
//                new ViewModelProvider(this).get(EventsViewModel.class);

        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        year = binding.yearEditText;
        month = binding.monthEditText;
        day = binding.dayEditText;
        title = binding.habitEditText;

        eventList = binding.eventList;
        dataList = new ArrayList<>();

        eventAdapter = new EventsAdapter(getContext(), dataList);
        eventList.setAdapter(eventAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HabitEvents currentHabit = eventAdapter.getItem(position);

                new ViewEditDeleteHabitEvent(currentHabit).show(getActivity().getSupportFragmentManager(), "AddHabitEvent");

            }
        });

        final Button searchButton = binding.buttonSearch;
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                binding.fieldSearchEntry.setVisibility(View.VISIBLE);
            }
        });

        db = FirebaseFirestore.getInstance();

        final Button confirmButton = binding.buttonConfirm;
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t = title.getText().toString();
                String dateInput = getString(R.string.date_formatter,
                        year.getText().toString(), month.getText().toString(), day.getText().toString());
                if (hh.isLegalDate(dateInput))
                    dataList.removeIf((HabitEvents hEvent) -> !hEvent.getTimeStamp().equals(dateInput));
                if (hh.isLegalTitle(t))
                    dataList.removeIf((HabitEvents hEvent) -> !hEvent.getTitle().equals(t));
                eventAdapter.notifyDataSetChanged();
            }
        });

        final Button resetButton = binding.buttonReset;
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResetPressed();
            }
        });

        refreshDataList();
        return root;
    }

    /**
     * Updates the {@link EventsFragment#dataList} when creating the view
     */
    private void refreshDataList() {
        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HabitEvents")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        dataList.clear();
                        for(QueryDocumentSnapshot hevent: value) {
                            HabitEvents hEvent = hevent.toObject(HabitEvents.class);
                            dataList.add(hEvent);
                        }
                        eventAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Resets the data list to show all events
     */
    private void onResetPressed() {
        refreshDataList();
        title.setText("");
        year.setText("");
        month.setText("");
        day.setText("");
        binding.fieldSearchEntry.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
