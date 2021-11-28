package com.example.habtrack.ui.events;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.habtrack.HabitEvents;
import com.example.habtrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

import io.reactivex.annotations.NonNull;


public class EditPhotographFragment extends DialogFragment {
    private HabitEvents hEvent;

    EditPhotographFragment() { }

    EditPhotographFragment(HabitEvents hEvent) { this.hEvent = hEvent; }

    private void setNewImage(String newImage,HabitEvents selectedEvent){
        FirebaseFirestore HabTrackDB = FirebaseFirestore.getInstance();

        // TODO: Test this
        HabTrackDB.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("HabitEvents")
                .document(selectedEvent.getHabitEventID())
                .update("photo", newImage);
    }

    ActivityResultLauncher<Intent> reTakeActivityResultLauncher = registerForActivityResult(
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

                    setNewImage(Imgstring, hEvent);
                }
            }
    );

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceData){
        //TODO: check later
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_photograph, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Photograph")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setNewImage(null, hEvent);
                    }
                })
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Re-take", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        reTakeActivityResultLauncher.launch(open_camera);

                    }
                }).create();

    }

}
