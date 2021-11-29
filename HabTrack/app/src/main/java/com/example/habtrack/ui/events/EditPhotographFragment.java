package com.example.habtrack.ui.events;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.DialogFragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.example.habtrack.R;
import io.reactivex.annotations.NonNull;

/**
 * This class implements the functionality of the fragment that allows managing photograph.
 * It provides the option to Re-take or Delete the existing image.
 */
public class EditPhotographFragment extends DialogFragment {
    private ActivityResultLauncher<Intent> newActivityResultLauncher;
    private ViewEditDeleteHabitEvent VEDHabitEvent;

    public EditPhotographFragment() { }

    public EditPhotographFragment(ActivityResultLauncher<Intent> ARL,
                                  ViewEditDeleteHabitEvent VEDHabitEvent) {
        this.newActivityResultLauncher = ARL;
        this.VEDHabitEvent = VEDHabitEvent;
    }

    /**
     * This method creates the view for the edit photograph fragment and allows the user to retake or
     * delete the photograph
     * @param savedInstanceData
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceData){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_photograph, null);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit Photograph")
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Drawable Cameraicon = getResources().getDrawable(R.drawable.ic_menu_camera, getContext().getTheme());

                        (VEDHabitEvent.getImageView()).setImageDrawable(Cameraicon);
//                        imageView.setImageDrawable(Cameraicon);
                        VEDHabitEvent.setDeleteFlag(true);

                        Log.d("Testing for null", "NULL");
                    }
                })
                .setNeutralButton("Cancel", null)
                .setPositiveButton("Re-take", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        newActivityResultLauncher.launch(open_camera);

                    }
                }).create();
    }

}
