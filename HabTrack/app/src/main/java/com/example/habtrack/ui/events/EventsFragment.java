package com.example.habtrack.ui.events;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.habtrack.EventsAdapter;
import com.example.habtrack.HabitEvents;
import com.example.habtrack.R;
//import com.example.habtrack.ui.events.ViewEditDeleteHabitEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

//AddHabitEventFragment.OnFragmentInteractionListener
public class EventsFragment extends Fragment{

    ListView EventsList;
    ArrayAdapter<HabitEvents> eventAdapter;
    ArrayList<HabitEvents> eventsDataList;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_events, null);

        EventsList = view.findViewById(R.id.eventList); // To display the Habit Events lists

        eventsDataList = new ArrayList<>();


        // TODO: Need to check if it's getContext or getActivity

        eventAdapter = new EventsAdapter(getContext(), eventsDataList);

        EventsList.setAdapter(eventAdapter);


        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HabitEvents")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        eventsDataList.clear();
                        for(QueryDocumentSnapshot hevent: value) {
                            HabitEvents hEvent = hevent.toObject(HabitEvents.class);
//                            eventsDataList.add(hEvent);
                            eventAdapter.add(hEvent);
                        }
                        eventAdapter.notifyDataSetChanged();
                    }
                });

        EventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                HabitEvents currentEvent = eventAdapter.getItem(i);

                new ViewEditDeleteHabitEvent(currentEvent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

}

