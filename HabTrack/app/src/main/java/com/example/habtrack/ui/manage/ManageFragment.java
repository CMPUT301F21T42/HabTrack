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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * The purpose of this class is to enable the user to view their habits
 * and click on individual ones to edit them.
 */

public class ManageFragment extends Fragment {

    private ManageViewModel manageViewModel;
    private FragmentManageBinding binding;

    ListView habitList;
    ArrayList<Habit> dataList;
    ArrayAdapter<Habit> habitAdapter;

    FirebaseFirestore db;
    final String TAG = "Sample";

    /**
     *
     * <p>
     *     The purpose of this View is for the user to be able to see the home page
     *     which contains a list of all of their habits.
     * </p>
     * <p>
     *     This method extracts the information of the user from the database to
     *     display it on the home screen.
     * </p>
     * <p>
     *     It also creates the onClickListeners for each item in the list so as
     *     the user can click on it to edit it. This is done using fragments.
     * </p>
     * @param inflater Layout for the entire page
     * @param container Container to keep all the data
     * @param savedInstanceState Saved user data
     * @return View that enables the user to look at all their Habits.
     */
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
                    Habit habit = doc.toObject(Habit.class);
                    dataList.add(habit); // Adding the habit from FireStore
                }
                habitAdapter.notifyDataSetChanged();
            }
        });

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