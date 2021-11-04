/** Copyright 2021 Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code ItemViewHolder} class
 */

package com.example.habtrack.ui.manage;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.habtrack.Habit;
import com.example.habtrack.R;

/**
 * This class represents a view holder
 */
public class ItemViewHolder extends RecyclerView.ViewHolder{

    /**
     * This variable contains the ui {@link TextView} that displays a {@link Habit} title
     */
    public TextView title;
//    public ProgressBar progressBar;

    /**
     * Constructs a {@link ItemViewHolder} object
     * @param itemView of type {@link View}
     */
    public ItemViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title_text);
//        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
    }
}
