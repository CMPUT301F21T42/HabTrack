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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.Calendar;

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

    FirebaseFirestore db;
    
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
                Toast.makeText(getContext(), "Clicked on Checkbox: " + habit.getTitle(),
                        Toast.LENGTH_SHORT).show();
            }
        };

        mydayAdapter = new MydayAdapter(getContext(), dataList, listener);
        mydayList.setAdapter(mydayAdapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference
                = db.collection("Users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Habits");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException error) {
                dataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    //Log.d(TAG, String.valueOf(doc.getData().get("Habit")));
                    Calendar today = Calendar.getInstance();
                    Habit habit = doc.toObject(Habit.class);
                    if (!habit.getStartDate().after(today.getTime()) &&
                            habit.getPlan().get(today.get(Calendar.DAY_OF_WEEK) - 1))
                        dataList.add(habit); // Adding the habit from FireStore
                }
                mydayAdapter.notifyDataSetChanged();
            }
        });

//        mydayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}