/*
 * FollowingListAdapter
 *
 * FollowingListAdapter extends the ArrayAdapter class that helps connect the data (stored in a list)
 * to the UI.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * November 29, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

/**
 * This class is an ArrayAdapter for the list of users that that are confirmed followers of a
 * specific user.
 *
 * @author Jenish
 * @see UserProfileActivity
 * @version 1.0
 * @since 1.0
 */
public class FollowingListAdapter extends ArrayAdapter<UserInfo> {
    private ArrayList<UserInfo> followingDataList;
    private FollowingActivity context;


    public FollowingListAdapter(@NonNull FollowingActivity context, ArrayList<UserInfo> friendsDataList) {
        super(context, R.layout.content_search_users, friendsDataList);
        this.followingDataList = friendsDataList;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_following, null);
        }

        UserInfo value = followingDataList.get(position);
        TextView userNameText = convertView.findViewById(R.id.following_text);
        userNameText.setText(value.getUserName());

        return convertView;
    }
}
