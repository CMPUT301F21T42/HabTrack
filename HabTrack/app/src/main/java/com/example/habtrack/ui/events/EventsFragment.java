package com.example.habtrack.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.habtrack.EventsAdapter;
import com.example.habtrack.Habit;
import com.example.habtrack.HabitEvents;
import com.example.habtrack.OnItemClickListener;
import com.example.habtrack.databinding.FragmentEventsBinding;
import com.example.habtrack.ui.edithabit.EdithabitFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class stores and displays the list of habit events
 */

public class EventsFragment extends Fragment {

    //    private EventsViewModel eventsViewModel;
    private FragmentEventsBinding binding;

    private ListView eventList;
    private ArrayList<HabitEvents> dataList;
    private ArrayAdapter<HabitEvents> eventAdapter;

    FirebaseFirestore db;

    /**
     * This method adds the new habit to the events list view and updates the events adapter
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return root
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        eventsViewModel =
//                new ViewModelProvider(this).get(EventsViewModel.class);

        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        eventList = binding.eventList;
        dataList = new ArrayList<>();

        eventAdapter = new EventsAdapter(getContext(), dataList);
        eventList.setAdapter(eventAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HabitEvents currentHabit = eventAdapter.getItem(position);

                new ViewEditDeleteHabitEvent(currentHabit).show(getActivity().getSupportFragmentManager(), "AddHabitEvent");

            }
        });

        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HabitEvents")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        dataList.clear();
                        for(QueryDocumentSnapshot hevent: value) {
                            HabitEvents hEvent = hevent.toObject(HabitEvents.class);
                            dataList.add(hEvent);
                        }
                        eventAdapter.notifyDataSetChanged();
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
