/** Copyright 2021 Wendy Zhang
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21T42 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * The {@code ItemTouchHelperAdapter} interface
 */

package com.example.habtrack.ui.manage;

public interface ItemTouchHelperAdapter {

    /**
     * Invoked when a drag and drop action is detected
     * @param fromPosition the initial position of the impacted item
     * @param toPosition the final position of the impacted item
     */
    void onItemMove(int fromPosition, int toPosition);

//    void onItemDismiss(int position);
}
