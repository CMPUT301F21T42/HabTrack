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














//package com.example.habtrack.ui.events;
//
//
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//
//import com.example.habtrack.EventsAdapter;
//import com.example.habtrack.HabitEvents;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.example.habtrack.R;
//import com.example.habtrack.ViewEditDeleteHabitEvent;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import java.util.ArrayList;
//
////AddHabitEventFragment.OnFragmentInteractionListener
//public class EventsFragment extends Fragment{
//
//    ListView EventsList;
//    ArrayAdapter<HabitEvents> eventAdapter;
//    ArrayList<HabitEvents> eventsDataList;
//
//    public EventsFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_events, null);
//
//        EventsList = view.findViewById(R.id.eventList); // To display the Habit Events lists
//
//        eventsDataList = new ArrayList<>();
//
//        // TODO: Need to check if it's getContext or getActivity
//        eventAdapter = new EventsAdapter(getContext(), eventsDataList);
//
//        EventsList.setAdapter(eventAdapter);
//
//        EventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                HabitEvents currentEvent = eventAdapter.getItem(i);
//
//                new ViewEditDeleteHabitEvent(currentEvent);
//            }
//        });
//
//        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
//        HabTrackDB.collection("Users")
//                .document("User1")
//                .collection("HabitEvents")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        eventsDataList.clear();
//                        for(QueryDocumentSnapshot hevent: value) {
//                            HabitEvents hEvent = hevent.toObject(HabitEvents.class);
//                        }
//                        eventAdapter.notifyDataSetChanged();
//                    }
//                });
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_events, container, false);
//    }
//
//}
//
//
//
//
////    public void addToHabitEventClass(DataSnapshot snapshot) {
////        HabitEvents newHabitEvent = (HabitEvents) snapshot.getValue();
////        eventAdapter.add(newHabitEvent);
////    }
////
////    @Override
////    public void onOkPressed(String habitEventTitle, String comment, Boolean photo, Boolean location){
////
////        // The Id for this new HabitEvent would be "Workout20210929" if title for habit is "Workout"
////        // and if the date user finished this habit is 2021/09/29
////        String timestamp = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
////        String HabitEventId = habitEventTitle + timestamp;
////
////        DatabaseReference newHabitEvent = HabitEventsTable.child(HabitEventId);
////        newHabitEvent.child("Title").setValue(habitEventTitle);
////        newHabitEvent.child("Comment").setValue(comment);
////        // Temporary place holders for photo and location
////        newHabitEvent.child("Photo").setValue(false);
////        newHabitEvent.child("Location").setValue(false);
////        newHabitEvent.child("TimeStamp").setValue(timestamp);
////        Log.d("Firestore", "new HabitEvent is added to FireStore");
////
////        // TODO: Need to use only one DatabaseReference
////        DatabaseReference ref = FirebaseDatabase.getInstance()
////                .getReference(path+"/HabitEvents/"+habitEventTitle+timestamp);
////
////        ref.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                addToHabitEventClass(snapshot);
////                Log.d("Firestore", "Got data back");
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
////
//////        eventAdapter.add(new HabitEvents(habitEventTitle, comment, photo, location));
////    }
//
////    private void addDataToEventAdapter() {
////        String path = "Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid()+"/HabitEvents";
////
////        DatabaseReference HEref = FirebaseDatabase.getInstance().getReference(path);
////
////        // DATA: [ {HabitEventID1: TITLE, COMMENT, PHOTO, LOCATION, HabitEventID},
//////                  {HabitEventID1: TITLE, COMMENT, PHOTO, LOCATION, HabitEventID}]
////
////        Query HElist = HEref.orderByKey();
////
////        HElist.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot snapshot) {
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
////
////    }


//package com.example.habtrack.ui.events;
//
//import android.os.Bundle;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.example.habtrack.EventsAdapter;
//import com.example.habtrack.HabitEvents;
//import com.example.habtrack.R;
////import com.example.habtrack.ui.events.ViewEditDeleteHabitEvent;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import java.util.ArrayList;
//
////AddHabitEventFragment.OnFragmentInteractionListener
//public class EventsFragment extends Fragment{
//
//    ListView EventsList;
//    ArrayAdapter<HabitEvents> eventAdapter;
//    ArrayList<HabitEvents> eventsDataList;
//
//    public EventsFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_events, null);
//
//        EventsList = view.findViewById(R.id.eventList); // To display the Habit Events lists
//
//        eventsDataList = new ArrayList<>();
//
//
//        // TODO: Need to check if it's getContext or getActivity
//
//        eventAdapter = new EventsAdapter(getContext(), eventsDataList);
//
//        EventsList.setAdapter(eventAdapter);
//
//
//        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
//        HabTrackDB.collection("Users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .collection("HabitEvents")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        eventsDataList.clear();
//                        for(QueryDocumentSnapshot hevent: value) {
//                            HabitEvents hEvent = hevent.toObject(HabitEvents.class);
////                            eventsDataList.add(hEvent);
//                            eventAdapter.add(hEvent);
//                        }
//                        eventAdapter.notifyDataSetChanged();
//                    }
//                });
//
//        EventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                HabitEvents currentEvent = eventAdapter.getItem(i);
//
//                new ViewEditDeleteHabitEvent(currentEvent);
//            }
//        });
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_events, container, false);
//    }
//
//}
//
