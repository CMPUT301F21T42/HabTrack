/** Copyright 2021 Raunak Agarwal, Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code ManageFragment} class
 */
package com.example.habtrack.ui.manage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.databinding.FragmentManageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * The purpose of this class is to enable the user to view their habits
 * and click on individual ones to edit them.
 */

public class ManageFragment extends Fragment {
    final String TAG = "Sample";

//    private ManageViewModel manageViewModel;
    private FragmentManageBinding binding;
    private FirestoreManager firestoreManager;

    private RecyclerView habitList;
    private ArrayList<Habit> dataList;
    private RecyclerView.Adapter<ItemViewHolder> habitAdapter;

    private String userId;

    /**
     *
     * <p>
     *     The purpose of this View is for the user to be able to see the home page
     *     which contains a list of all of their habits.
     * </p>
     * <p>
     *     This method extracts the information of the user from the database to
     *     display it on the home screen.
     * </p>
     * <p>
     *     It also creates the onClickListeners for each item in the list so as
     *     the user can click on it to edit it. This is done using fragments.
     * </p>
     * @param inflater Layout for the entire page
     * @param container Container to keep all the data
     * @param savedInstanceState Saved user data
     * @return View that enables the user to look at all their Habits.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        manageViewModel =
//                new ViewModelProvider(this).get(ManageViewModel.class);

        binding = FragmentManageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        habitList = binding.habitList;
        habitList.setLayoutManager(new LinearLayoutManager(getContext()));
        firestoreManager = FirestoreManager.getInstance(FirebaseAuth.getInstance().getCurrentUser().getUid());
        dataList = firestoreManager.getHabits();
        habitAdapter = new HabitListAdapter(getContext(), dataList, getParentFragmentManager());
        habitList.setAdapter(habitAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback((ItemTouchHelperAdapter) habitAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(habitList);

        firestoreManager.setOnDataChangeListener(new FirestoreManager.OnDataChangeListener() {
            @Override
            public void onDataChange() {
                habitAdapter.notifyDataSetChanged();
            }
        });

//        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                EdithabitFragment.newInstance(dataList.get(position))
//                        .show(getParentFragmentManager(), "EDIT_HABIT");
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