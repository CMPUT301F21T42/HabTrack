/*
 * NotificationListAdapter
 *
 * NotificationListAdapter acts in a similar way to the SearchUserListAdapter. It extends the
 * ArrayAdapter class and connects the data, which in this case is a list of follow requests
 * (notifications) and the UI.
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class is an ArrayAdapter for the list of users that that have all sent a yet approved/ unapproved
 * follow request to a particular user.
 *
 * @author Jenish
 * @see NotificationActivity
 * @version 1.0
 * @since 1.0
 */
public class NotificationListAdapter extends ArrayAdapter<UserInfo> {
    private ArrayList<UserInfo> notificationsDataList;
    private NotificationActivity context;
    private final String TAG = "Sample";


    public NotificationListAdapter(@NonNull NotificationActivity context, ArrayList<UserInfo> notificationsDataList) {
        super(context, R.layout.content_search_users, notificationsDataList);
        this.notificationsDataList = notificationsDataList;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_notifications, null);
        }

        UserInfo value = notificationsDataList.get(position);
        TextView userNameText = convertView.findViewById(R.id.user_name_text);
        userNameText.setText(value.getUserName());

        Button accept = (Button) convertView.findViewById(R.id.accept_request);
        Button deny = (Button) convertView.findViewById(R.id.deny_request);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.acceptFriendRequest(notificationsDataList.get(position));
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.denyFriendRequest(notificationsDataList.get(position));
            }
        });

        return convertView;
    }
}
