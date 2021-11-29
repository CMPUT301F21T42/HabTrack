package com.example.habtrack.ui.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.io.ByteArrayOutputStream;
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
    private ImageView imageView;
    private Boolean DeleteFlag = false;
    private TextView latlng;
//    private ViewEditDeleteHabitEvent.OnEditFragmentInteractionListener listener;
    private FirestoreManager firestoreManager;

    public ViewEditDeleteHabitEvent() {

    }

    public ViewEditDeleteHabitEvent(HabitEvents hEvent) {
        this.hEvent = hEvent;
    }

    public Boolean getDeleteFlag() {
        return DeleteFlag;
    }

    public void setDeleteFlag(Boolean deleteFlag) {
        DeleteFlag = deleteFlag;
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_view_edit_delete_habit_event, null);
        title = view.findViewById(R.id.habit_event_title);
        comment = view.findViewById(R.id.habit_event_comment);
        imageView = view.findViewById(R.id.PhotoGraph);
        String HEphoto = hEvent.getPhoto();
        latlng = view.findViewById(R.id.latlng);

        if (hEvent.getPhoto() != null) {
            String string_data = hEvent.getPhoto();

            byte[] data = android.util.Base64.decode(string_data, 0);

            Bitmap bitImage = BitmapFactory.decodeByteArray(data, 0, data.length);

            if (imageView != null) imageView.setImageBitmap(bitImage);
        }

        ActivityResultLauncher<Intent> newActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();

                        Bitmap newImage = (Bitmap) data.getExtras().get("data");

                        imageView.setImageBitmap(newImage);
                    }
                }
        );

        ViewEditDeleteHabitEvent obj1 = this;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hEvent.getPhoto() == null) {
                    Log.d("Access Camera", "To retake a picture");
                    Intent open_Camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    newActivityResultLauncher.launch(open_Camera);
                }
                else {
                    new EditPhotographFragment(newActivityResultLauncher, obj1).show(getActivity().getSupportFragmentManager(), "EditPhotograph");
                    Log.d("Edit-PhotoGraph Fragment", "Edit PhotoGraph Fragment pops up");
                }
                Log.d("editimage", "On image click Working");
            }
        });
//         binding = FragmentViewEditDeleteHabitEventBinding.inflate(LayoutInflater.from(getContext()));
//         View root = binding.getRoot();

//         title = binding.habitEventTitle;
//         comment = binding.habitEventComment;

//         latlng = binding.latlng;

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
        ImageButton ib = view.findViewById(R.id.imageButton);
//        ImageButton ib = binding.imageButton;
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
                .setView(view)
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

                        String user_photo;
                        if (!getDeleteFlag()) {
                            Bitmap bitImg = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream newstream = new ByteArrayOutputStream();
                            bitImg.compress(Bitmap.CompressFormat.PNG, 100, newstream);
                            byte[] Imagearr = newstream.toByteArray();
                            user_photo = android.util.Base64.encodeToString(Imagearr, 0);
                        } else {
                            user_photo = "Default Value";
                        }
                        if (user_comment.length() > 30 || user_title.length() > 20) {
                            if (user_comment.length() > 30 && user_title.length() > 20)
                                Toast.makeText(getContext(), "Title should be less than 20 characters and Comment should be less than 30 characters", Toast.LENGTH_SHORT).show();
                            else if (user_comment.length() > 30)
                                Toast.makeText(getContext(), "Comment should be less than 30 characters", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), "Title should be less than 20 characters", Toast.LENGTH_SHORT).show();
                        } else {
                            if (user_title.equals(HEtitle) && user_comment.equals(HEcomment) && user_photo.equals(HEphoto)) {

                            } else {

//                                hEvent.setTitle(user_title);
                                hEvent.setComment(user_comment);
//                                hEvent.setPhoto(null);
                                if (getDeleteFlag()) {
                                    hEvent.setPhoto(null);
                                    setDeleteFlag(false);
                                } else {
                                    hEvent.setPhoto(user_photo);
                                }

                                // To check
                                hEvent.setComment(user_comment);
                                hEvent.setLocation(location);
                                modifyHabitEventDB();
                            }
//                             FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
//                             HabTrackDB.collection("Users")
//                                     .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                     .collection("HabitEvents")
//                                     .document(hEvent.getHabitEventID())
//                                     .set(hEvent);                        }
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
    }

    private void modifyHabitEventDB(){

        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HabitEvents")
                .document(hEvent.getHabitEventID())
                .set(new HabitEvents(hEvent.getHabitId(), hEvent.getComment(), hEvent.getPhoto(),
                        hEvent.getLocation(), hEvent.getTimeStamp()));
        }
     
}