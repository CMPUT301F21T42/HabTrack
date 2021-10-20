package com.example.habtrack.ui.myday;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.habtrack.Habit;
import com.example.habtrack.MydayAdapter;
import com.example.habtrack.OnItemClickListener;
import com.example.habtrack.databinding.FragmentMydayBinding;
import com.example.habtrack.ui.edithabit.EdithabitFragment;
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

    FirebaseFirestore db;
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

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Habits");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                dataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    //Log.d(TAG, String.valueOf(doc.getData().get("Habit")));
                    Calendar today = Calendar.getInstance();
                    Habit habit = doc.toObject(Habit.class);
                    if (habit.getPlan().get(today.get(Calendar.DAY_OF_WEEK) - 1))
                        dataList.add(habit); // Adding the habit from FireStore
                }
                mydayAdapter.notifyDataSetChanged();
            }
        });

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