/** Copyright 2021 Wendy (Qianyu) Zhang,
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * CMPUT301F21 Project: HabTrack <br>
 * To help someone who wants to track their habits.
 * @author <Wendy Zhang>
 * The {@code OnItemClickListener} interface
 */

package com.example.habtrack;

public interface OnItemClickListener {

    /**
     * Invoked when a checkbox from the adapter is clicked
     * @param position
     */
    void onItemClicked(int position);

}
