package com.example.habtrack.ui.myday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.habtrack.Habit;
import com.example.habtrack.OnItemClickListener;
import com.example.habtrack.databinding.FragmentMydayBinding;
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
import java.util.Date;

public class MydayFragment extends Fragment {

    private MydayViewModel mydayViewModel;
    private FragmentMydayBinding binding;

    ListView mydayList;
    ArrayList<Habit> dataList;
    ArrayAdapter<Habit> mydayAdapter;

    FirebaseDatabase db;
    final String TAG = "Sample";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mydayViewModel =
                new ViewModelProvider(this).get(MydayViewModel.class);

        binding = FragmentMydayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mydayList = binding.mydayList;
        dataList = new ArrayList<>();

        OnItemClickListener listener = new OnItemClickListener() {
            public void onItemClicked(int position) {
                Habit habit = mydayAdapter.getItem(position);
                Toast.makeText(getContext(), "Clicked on Checkbox: " + habit.getTitle(),
                        Toast.LENGTH_SHORT).show();
            }
        };

        mydayAdapter = new MydayAdapter(getContext(), dataList, listener);
        mydayList.setAdapter(mydayAdapter);

        db = FirebaseDatabase.getInstance();
        db.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("habit")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Calendar today = Calendar.getInstance();
                            Habit habit = snapshot.getValue(Habit.class);
                            if (!habit.getStartDate().after(today.getTime()) &&
                                    habit.getPlan().get(today.get(Calendar.DAY_OF_WEEK) - 1))
                                dataList.add(habit); // Adding the habit from FireStore
                        }
                        mydayAdapter.notifyDataSetChanged();
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
//                    Calendar today = Calendar.getInstance();
//                    Habit habit = doc.toObject(Habit.class);
//                    if (!habit.getStartDate().after(today.getTime()) &&
//                            habit.getPlan().get(today.get(Calendar.DAY_OF_WEEK) - 1))
//                        dataList.add(habit); // Adding the habit from FireStore
//                }
//                mydayAdapter.notifyDataSetChanged();
//            }
//        });

//        mydayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//        });

        return root;
    }

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}