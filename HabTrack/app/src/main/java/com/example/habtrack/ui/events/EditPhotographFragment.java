package com.example.habtrack.ui.events;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.habtrack.HabitEvents;
import com.example.habtrack.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

import io.reactivex.annotations.NonNull;

public class EditPhotographFragment extends DialogFragment {
    private HabitEvents hEvent;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> newActivityResultLauncher;

    public EditPhotographFragment() { }

    EditPhotographFragment(HabitEvents hEvent) { this.hEvent = hEvent; }

    EditPhotographFragment(HabitEvents hEvent, ImageView imageView) {
        this.hEvent = hEvent;
        this.imageView = imageView;
    }

    EditPhotographFragment(HabitEvents hEvent, ImageView imageView, ActivityResultLauncher<Intent> ARL) {
        this.hEvent = hEvent;
        this.imageView = imageView;
        this.newActivityResultLauncher = ARL;
    }

    private void setNewImage(String newImage, HabitEvents selectedEvent){

        if (selectedEvent.getPhoto() != null) {

            String string_data = selectedEvent.getPhoto();

            byte[] data = android.util.Base64.decode(string_data, 0);

            Bitmap bitImage = BitmapFactory.decodeByteArray(data, 0, data.length);

            if (imageView != null) imageView.setImageBitmap(bitImage);
        }
        else {
            imageView.setImageResource(R.drawable.ic_menu_camera);
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            imageView.setImageBitmap(bitmap);

            Log.d("Testing for null", "NULL");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceData){
        //TODO: check later
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_photograph, null);

        ActivityResultLauncher<Intent> reTakeActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();

                        Bitmap newImage = (Bitmap) data.getExtras().get("data");

                        imageView.setImageBitmap(newImage);

                        ByteArrayOutputStream newstream = new ByteArrayOutputStream();
                        newImage.compress(Bitmap.CompressFormat.PNG, 100, newstream);

                        byte[] Imagearr = newstream.toByteArray();

                        String ImgString = android.util.Base64.encodeToString(Imagearr, 0);
                    }
                }
        );

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
//                        reTakeActivityResultLauncher.launch(open_camera);
                        newActivityResultLauncher.launch(open_camera);

                    }
                }).create();
    }

}
