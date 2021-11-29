/*
 * SearchUserListAdapter
 *
 * SearchUserListAdapter extends the ArrayAdapter class that helps connect the data (stored in a list)
 * to the UI.
 *
 * No known outstanding issues.
 *
 * Version 1.0
 *
 * November 28, 2021
 *
 * Copyright notice
 */

package com.example.habtrack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is an ArrayAdapter for the list of users that results from the SearchUsersActivity
 * and helps connect this data to the UI.
 *
 * @author Jenish
 * @see SearchUsersActivity
 * @version 1.0
 * @since 1.0
 */
public class SearchUserListAdapter extends ArrayAdapter<UserInfo> {
    private ArrayList<UserInfo> userDataList;
    private SearchUsersActivity context;
    private final String TAG = "Sample";

    public SearchUserListAdapter(@NonNull SearchUsersActivity context, ArrayList<UserInfo> userDataList) {
        super(context, R.layout.content_search_users, userDataList);
        this.userDataList = userDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_search_users, null);
        }

        UserInfo user = userDataList.get(position);
        TextView userNameText = convertView.findViewById(R.id.user_name_text);
        userNameText.setText(user.getUserName());
        userNameText.setTextSize(20);

        return convertView;
    }
}
