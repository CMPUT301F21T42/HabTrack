/**
 * Referenced to sandipapps on https://github.com/sandipapps/ExpandableListView-Demo
 * Accessed on November 25, 2021
 */

package com.example.habtrack.ui.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
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
import com.example.habtrack.FriendsManager;
import com.example.habtrack.Habit;
import com.example.habtrack.UserInfo;
import com.example.habtrack.databinding.FragmentFriendsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    private ExpandableListView expandableListView;
    private ArrayList<String> groupList;
    private ArrayList<String> childList;
    private ArrayList<String> followings = new ArrayList<>();
    private HashMap<String, ArrayList<Habit>> mobileCollection;
    private ExpandableListAdapter friendsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        groupList = new ArrayList<>();
        mobileCollection = new HashMap<>();
        friendsAdapter = new FriendsListAdapter(getContext(), groupList, mobileCollection);
        expandableListView = binding.friendsList;
        expandableListView.setAdapter(friendsAdapter);

        createGroupList();
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

    }

    private void loadChild(String[] mobileModels) {
        childList = new ArrayList<>();
        for(String model : mobileModels){
            childList.add(model);
        }
    }

    private void createGroupList() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                followings = (ArrayList) value.getData().get("following");
                if (followings != null && followings.size() > 0) {
                    for (String userID : followings) {
                        DocumentReference userDocument = FirebaseFirestore.getInstance().collection("Users").document(userID);
                        userDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                UserInfo friend = new UserInfo();
                                friend.setUserName(String.valueOf(value.getData().get("userName")));
                                friend.setEmail(String.valueOf(value.getData().get("email")));
                                friend.setUserID(value.getId());
                                groupList.add(friend.getUserName());
                                FirestoreManager firestoreManager = FirestoreManager.getInstance(userID);
                                mobileCollection.put(friend.getUserName(), firestoreManager.getHabits());
                                ((BaseExpandableListAdapter) friendsAdapter).notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    Log.d("Sample", "No Followings");
                }
            }
        });
    }
}