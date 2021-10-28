package com.example.habtrack.ui.manage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.habtrack.Habit;
import com.example.habtrack.databinding.FragmentManageBinding;
import com.example.habtrack.ui.edithabit.EdithabitFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class ManageFragment extends Fragment {

    private ManageViewModel manageViewModel;
    private FragmentManageBinding binding;

    ListView habitList;
    ArrayList<Habit> dataList;
    ArrayAdapter<Habit> habitAdapter;

    FirebaseDatabase db;
    final String TAG = "Sample";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        manageViewModel =
                new ViewModelProvider(this).get(ManageViewModel.class);

        binding = FragmentManageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        habitList = binding.habitList;
        dataList = new ArrayList<>();
        habitAdapter = new HabitListAdapter(getContext(), dataList);
        habitList.setAdapter(habitAdapter);


        db = FirebaseDatabase.getInstance();
        db.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("habit")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Habit habit = snapshot.getValue(Habit.class);
                            dataList.add(habit); // Adding the habit from FireStore
                        }
                        habitAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//        db = FirebaseFirestore.getInstance();
//        final CollectionReference collectionReference = db.collection("Habits");
//
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//                dataList.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    //Log.d(TAG, String.valueOf(doc.getData().get("Habit")));
//                    Habit habit = doc.toObject(Habit.class);
//                    dataList.add(habit); // Adding the habit from FireStore
//                }
//                habitAdapter.notifyDataSetChanged();
//            }
//        });

        habitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EdithabitFragment.newInstance(dataList.get(position))
                        .show(getParentFragmentManager(), "EDIT_HABIT");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}