/** Copyright 2021 Wendy Zhang
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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.R;
import com.example.habtrack.ui.edithabit.EdithabitFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

public class HabitListAdapter extends RecyclerView.Adapter<ItemViewHolder>
        implements ItemTouchHelperAdapter {
    private ArrayList<Habit> habits;
    private Context context;
    private FragmentManager fragmentManager;
    private FirestoreManager firestoreManager;

    /**
     * Constructs an adapter with
     * @param context the current context
     * @param habits the arrayList of habits {@link Habit}
     * @param fragmentManager the parent fragment manager
     */
    public HabitListAdapter(Context context, ArrayList<Habit> habits, FragmentManager fragmentManager) {
        this.habits = habits;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    /**
     * Constructs a view holder to hold view elements such as TextView and ProgressBar
     * @param parent of type {@link ViewGroup}
     * @param viewType of type int
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_habitlist, parent, false);
        return new ItemViewHolder(view);
    }

    /**
     * Initializes the view for a given view holder at the specified position
     * @param holder of type {@link ItemViewHolder}
     * @param position of type int
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.title.setText(habit.getTitle());
//        holder.progressBar.setMax(100);
//        holder.progressBar.setProgress(habit.getProgress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                EdithabitFragment.newInstance(habits.get(pos))
                        .show(fragmentManager, "EDIT_HABIT");
            }
        });
    }

    /**
     * Returns the number of items in the {@link HabitListAdapter}
     * @return the item count of type integer
     */
    @Override
    public int getItemCount() {
        return habits.size();
    }

//    @Override
//    public void onItemDismiss(int position) {
//        habits.remove(position);
//        notifyItemRemoved(position);
//
//        firestoreManager = FirestoreManager.getInstance(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        firestoreManager.deleteHabit(habits.get(position));
//    }

    /**
     * Reorders the item that was dragged and dropped in the list and
     * updates the changes in firestore
     * @param fromPosition the initial position of the impacted item
     * @param toPosition the final position of the impacted item
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(habits, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        firestoreManager = FirestoreManager.getInstance(FirebaseAuth.getInstance().getCurrentUser().getUid());
        firestoreManager.swapRanking(fromPosition, toPosition);
    }
}
