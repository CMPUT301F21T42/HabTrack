/** Copyright 2021 Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code ItemTouchHelperCallback} class
 */

package com.example.habtrack.ui.manage;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;

    /**
     * Constructs a {@link ItemTouchHelperCallback} object with the given adapter
     * @param adapter a {@link ItemTouchHelperAdapter}
     */
    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Returns true since the drag and drop action is enabled
     * @return true
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * Returns true since the swipe action is enabled
     * @return true
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * Returns the movement flag from user action
     * @param recyclerView the {@link RecyclerView} in which the action is detected
     * @param holder the {@link RecyclerView.ViewHolder} associated with the recyclerView
     * @return the movement flag for user actions
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder holder) {
        // Set movement flags based on the layout manager
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * Returns true when the action is completed
     * @param recyclerView the {@link RecyclerView} in which the action is detected
     * @param holder the {@link RecyclerView.ViewHolder} pre-action
     * @param target the {@link RecyclerView.ViewHolder} post-action
     * @return true
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder holder, RecyclerView.ViewHolder target) {
        adapter.onItemMove(holder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * Does something...
     * @param holder the {@link RecyclerView.ViewHolder}
     * @param direction of the swiping action
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
//        // Notify the adapter of the dismissal
//        adapter.onItemDismiss(holder.getAdapterPosition());
    }
}
