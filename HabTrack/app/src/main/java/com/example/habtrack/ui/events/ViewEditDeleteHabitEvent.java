package com.example.habtrack.ui.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.habtrack.FirestoreManager;
import com.example.habtrack.Habit;
import com.example.habtrack.HabitEvents;
import com.example.habtrack.MapsActivity;
import com.example.habtrack.R;
import com.example.habtrack.databinding.FragmentAddHabitEventBinding;
import com.example.habtrack.databinding.FragmentViewEditDeleteHabitEventBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the functionality for the view edit delete fragment
 */
public class ViewEditDeleteHabitEvent extends DialogFragment {
    private FragmentViewEditDeleteHabitEventBinding binding;

    private ArrayList<Double> location = new ArrayList<>();

    private TextView title;     // TODO: Need to set this to the current habit title
    private EditText comment;   // TODO:  Need to set this to the current comment
    private HabitEvents hEvent;

    private TextView latlng;
//    private ViewEditDeleteHabitEvent.OnEditFragmentInteractionListener listener;

    private FirestoreManager firestoreManager;

    public ViewEditDeleteHabitEvent() {

    }

    public ViewEditDeleteHabitEvent(HabitEvents hEvent) {
        this.hEvent = hEvent;
    }

//    public interface OnEditFragmentInteractionListener {
//        void onDeletePressed(HabitEvents selectedEvent);
//    }

//    @Override
//    public void onAttach(Context context){
//        super.onAttach(context);
//        if(context instanceof ViewEditDeleteHabitEvent.OnEditFragmentInteractionListener){
//            listener = (ViewEditDeleteHabitEvent.OnEditFragmentInteractionListener) context;
//        }
//        else{
//            throw new RuntimeException(context.toString()+ "must implement OnEditFragmentInteractionListener");
//        }
//    }

    /**
     * This method converts the latitude and longitude values into an address
     * @param location of type {@link ArrayList<Double>}
     * @return the parsed address of type {@link String}
     */
    private String latLngToAddress(ArrayList<Double> location) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(getContext());
        try {
            addresses = geocoder.getFromLocation(location.get(0), location.get(1), 1);
        } catch (IOException e) {
            return "(" + location.get(0) + ", " + location.get(1) + ")";
        }
        return addresses.get(0).getAddressLine(0);
    }

    /**
     * This method creates the view for the view edit delete fragment and allows the user to delete
     * and edit the habit events
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        binding = FragmentViewEditDeleteHabitEventBinding.inflate(LayoutInflater.from(getContext()));
        View root = binding.getRoot();

        title = binding.habitEventTitle;
        comment = binding.habitEventComment;

        latlng = binding.latlng;

        String HEtitle = hEvent.getTitle();
        String HEcomment = hEvent.getComment();
        location = hEvent.getLocation();

        title.setText(HEtitle);
        comment.setText(HEcomment);
        if (location.size() != 0) {
            latlng.setVisibility(View.VISIBLE);
            latlng.setText(latLngToAddress(location));
        }

        ActivityResultLauncher<Intent> locationGetContent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            location.clear();
                            location.add(data.getDoubleExtra("lat", 53.526681512750336));
                            location.add(data.getDoubleExtra("lng", -113.52975698826533));
                            latlng.setVisibility(View.VISIBLE);
                            latlng.setText(latLngToAddress(location));
                        }
                    }
                });

        // Sets the onClickListener for the map
        ImageButton ib = binding.imageButton;
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                if (location.size() != 0) {
                    intent.putExtra("lat", location.get(0));
                    intent.putExtra("lng", location.get(1));
                }
                locationGetContent.launch(intent);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(root)
                .setTitle("View/Edit/Delete")
                // Delete button in case a user want to delete a habit event
                // TODO: Need to add the implementation for Delete
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDeletePressed(hEvent);
                    }
                })

                .setNeutralButton("Cancel", null)

                // Save button for user to save the changes for editing the details of HabitEvent
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // To get the user input for Comment
                        String user_title = title.getText().toString();
                        String user_comment = comment.getText().toString();

                        // TODO: Need to modify the the attributes in HabitEvent
                        // TODO: Need to check if the user entered or modified the habitevent title, comment

                        // TODO: CHECK THE USER STORIES FOR HABIT EVENT, REQUIREMENTS NOT MET
                        if (user_comment.length() > 30 || user_title.length() > 20) {
                            if ( user_comment.length() > 30 && user_title.length() > 20)
                                Toast.makeText(getContext(), "Title should be less than 20 characters and Comment should be less than 30 characters", Toast.LENGTH_SHORT).show();
                            else
                            if (user_comment.length() > 30)
                                Toast.makeText(getContext(), "Comment should be less than 30 characters", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Title should be less than 20 characters", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            hEvent.setComment(user_comment);
                            hEvent.setLocation(location);
                            FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
                            HabTrackDB.collection("Users")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection("HabitEvents")
                                    .document(hEvent.getHabitEventID())
                                    .set(hEvent);
                        }
                    }
                }).create();
    }

    /**
     * This method deletes the habit event from the firebase.
     * @param selectedEvent
     */
    public void onDeletePressed(HabitEvents selectedEvent) {

        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
        firestoreManager = FirestoreManager.getInstance(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HabitEvents")
                .document(selectedEvent.getHabitEventID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Habit habit = firestoreManager.getHabit(selectedEvent.getHabitId());
                        habit.resetLastCompleted();
                        firestoreManager.editHabit(habit);
                    }
                });
//        eventAdapter.remove(selectedEvent);
    }

}