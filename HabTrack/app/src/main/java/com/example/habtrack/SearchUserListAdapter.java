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

import java.util.ArrayList;
import java.util.HashMap;

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

        Button request = convertView.findViewById(R.id.request_to_follow);

        if (context.checkIfUserIsInFollowing(user)) {
            request.setBackgroundColor(Color.RED);
            request.setText("Unfollow");
            request.setTextColor(Color.WHITE);
        } else {
            if (context.checkIfUserInOutgoingFriendRequest(user)) {
                request.setBackgroundColor(Color.WHITE);
                request.setText("Requested");
                request.setTextColor(Color.BLACK);
            } else {
                request.setBackgroundColor(Color.BLUE);
                request.setText("Follow");
                request.setTextColor(Color.WHITE);
            }
        }

//        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUserID())) {
//            request.setVisibility(View.INVISIBLE);
//        }

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (request.getText() == "Follow") {
                    context.sendFriendRequest(user.getUserID());
                } else if (request.getText() == "Requested") {
                    context.unSendFriendRequest(user.getUserID());
                } else if (request.getText() == "Unfollow") {
                    context.unfollow(user.getUserID());
                }
            }
        });

        return convertView;
    }
}
