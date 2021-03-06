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
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.UserInfo;
import com.example.habtrack.databinding.FragmentFriendsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;

    private ExpandableListView expandableListView;
    private ArrayList<String> groupList;
    private ArrayList<String> followings = new ArrayList<>();
    private HashMap<String, ArrayList<Habit>> groupCollection;
    private ExpandableListAdapter friendsAdapter;
    private TextView noFriends;
    private FirestoreManager currentUserFirestoreManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        currentUserFirestoreManager = FirestoreManager.getInstance(FirebaseAuth.getInstance().getCurrentUser().getUid());

        noFriends = binding.noFriends;
        groupList = new ArrayList<>();
        groupCollection = new HashMap<>();
        friendsAdapter = new FriendsListAdapter(getContext(), groupList, groupCollection);
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
    public void onStop() {
        FirestoreManager.setInstance(FirebaseAuth.getInstance().getCurrentUser().getUid(), currentUserFirestoreManager);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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

                                DocumentReference uDoc = FirebaseFirestore.getInstance().collection("Users").document(friend.getUserID());
                                CollectionReference uHabit = uDoc.collection("Habits");
                                uHabit.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        ArrayList<Habit> publicHabits = new ArrayList<>();
                                        for (QueryDocumentSnapshot doc : value) {
                                            Habit habit = new Habit();
                                            habit.setTitle(String.valueOf(doc.getData().get("title")));
                                            habit.setProgressNumerator(Integer.parseInt(doc.getData().get("progressNumerator").toString()));
                                            habit.setProgressDenominator(Integer.parseInt(doc.getData().get("progressDenominator").toString()));
                                            habit.setPublic((boolean) doc.getData().get("public"));
                                            // Only public habits are visible
                                            if (habit.isPublic()) {
                                                publicHabits.add(habit);
                                            }
                                        }
                                        groupCollection.put(friend.getUserName(), publicHabits);
                                        ((BaseExpandableListAdapter) friendsAdapter).notifyDataSetChanged();
                                    }
                                });
                            }
                        });
                    }
                    noFriends.setVisibility(View.INVISIBLE);
                } else {
                    noFriends.setVisibility(View.VISIBLE);
                    Log.d("Sample", "No Followings");
                }
            }
        });
    }
}