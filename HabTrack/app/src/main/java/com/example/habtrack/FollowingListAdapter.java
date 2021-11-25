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

public class FollowingListAdapter extends ArrayAdapter<UserInfo> {
    private ArrayList<UserInfo> followingDataList;
    private FollowingActivity context;
    private final String TAG = "Sample";


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
