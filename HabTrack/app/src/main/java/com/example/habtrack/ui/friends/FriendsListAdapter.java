/**
 * Referenced to sandipapps on https://github.com/sandipapps/ExpandableListView-Demo
 * Accessed on November 25, 2021
 */

package com.example.habtrack.ui.friends;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habtrack.Habit;
import com.example.habtrack.R;

import java.util.ArrayList;
import java.util.Map;

public class FriendsListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Map<String, ArrayList<Habit>> friendsCollection;
    private ArrayList<String> groupList;

    public FriendsListAdapter(Context context, ArrayList<String> groupList,
                              Map<String, ArrayList<Habit>> collection) {
        this.context = context;
        this.friendsCollection = collection;
        this.groupList = groupList;
    }

    @Override
    public int getGroupCount() {
        return friendsCollection.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return friendsCollection.get(groupList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Habit getChild(int i, int i1) {
        return friendsCollection.get(groupList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String mobileName = getGroup(i).toString();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.group_item, null);
        }
        TextView item = view.findViewById(R.id.friend_name);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(mobileName);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_item, null);
        }
        TextView item = view.findViewById(R.id.friends_habit_title);
        ProgressBar progressBar = view.findViewById(R.id.friends_progressBar);

        Habit habit = getChild(i, i1);
        item.setText(habit.getTitle());
        progressBar.setMax(100);
        progressBar.setProgress(habit.getProgress());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
