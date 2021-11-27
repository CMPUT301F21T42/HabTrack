/**
 * Referenced to sandipapps on https://github.com/sandipapps/ExpandableListView-Demo
 * Accessed on November 25, 2021
 */

package com.example.habtrack.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.databinding.FragmentFriendsBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    private ExpandableListView expandableListView;
    private ArrayList<String> groupList;
    private ArrayList<String> childList;
    private HashMap<String, ArrayList<Habit>> mobileCollection;
    private ExpandableListAdapter friendsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        createGroupList();
        createCollection();
        expandableListView = binding.friendsList;
        friendsAdapter = new FriendsListAdapter(getContext(), groupList, mobileCollection);
        expandableListView.setAdapter(friendsAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition != -1 && i != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = i;
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createCollection() {
        // TODO: CHANGE TO ACTUAL FRIENDS' HABITS, NOW USING CURRENT USER
        FirestoreManager firestoreManager = FirestoreManager.getInstance(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mobileCollection = new HashMap<String, ArrayList<Habit>>();
        for(String group : groupList)
            mobileCollection.put(group, firestoreManager.getHabits());
    }

    private void loadChild(String[] mobileModels) {
        childList = new ArrayList<>();
        for(String model : mobileModels){
            childList.add(model);
        }
    }

    private void createGroupList() {
        groupList = new ArrayList<>();
        groupList.add("User1");
        groupList.add("User2");
    }
}