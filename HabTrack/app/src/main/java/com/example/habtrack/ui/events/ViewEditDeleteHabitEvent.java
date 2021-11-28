package com.example.habtrack.ui.events;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.habtrack.HabitEvents;
import com.example.habtrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

/**
 * This class implements the functionality for the view edit delete fragment
 */
public class ViewEditDeleteHabitEvent extends DialogFragment {
    private EditText title;     // TODO: Need to set this to the current habit title
    private EditText comment;   // TODO:  Need to set this to the current comment
    private HabitEvents hEvent;
    private ImageView imageView;
//    private ViewEditDeleteHabitEvent.OnEditFragmentInteractionListener listener;

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
//                findViewById(R.id.PhotoGraph1);

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

                        ByteArrayOutputStream newstream = new ByteArrayOutputStream();
                        newImage.compress(Bitmap.CompressFormat.PNG, 100, newstream);

                        byte[] Imagearr = newstream.toByteArray();

                        String Imgstring = android.util.Base64.encodeToString(Imagearr, 0);

                        setNewImagetoDB(Imgstring, hEvent);
                    }
                }
        );

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hEvent.getPhoto() == null) {
                    Log.d("Access Camera", "To retake a picture");
                    Intent open_Camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    newActivityResultLauncher.launch(open_Camera);
                }
                else {
                    new EditPhotographFragment(hEvent).show(getActivity().getSupportFragmentManager(), "EditPhotograph");
                    Log.d("Edit-PhotoGraph Fragment", "Edit PhotoGraph Fragment pops up");
                }
                Log.d("editimage", "On image click Working");
            }
        });



        String HEtitle = hEvent.getTitle();
        String HEcomment = hEvent.getComment();

        title.setText(HEtitle);
        comment.setText(HEcomment);

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
                            if (user_title.equals(HEtitle) && user_comment.equals(HEcomment)){

                            }
                            else{

                                hEvent.setTitle(user_title);
                                hEvent.setComment(user_comment);
                                FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();
                                HabTrackDB.collection("Users")
                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .collection("HabitEvents")
                                        .document(hEvent.getHabitEventID())
                                        .set(new HabitEvents(hEvent.getTitle(), hEvent.getComment(), hEvent.getPhoto(),
                                                hEvent.getLocation(), hEvent.getTimeStamp()));
                            }
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


        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HabitEvents")
                .document(selectedEvent.getHabitEventID())
                .delete();
//        eventAdapter.remove(selectedEvent);
    }

    private void setNewImagetoDB(String newImage, HabitEvents selectedEvent) {
        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();

        // TODO: Test this
        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HabitEvents")
                .document(selectedEvent.getHabitEventID())
                .update("photo", newImage);
    }


}