package com.example.user.travelguideapps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by User on 6/5/2017.
 */
        
public class AlertDialogCategories extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
 return new AlertDialog.Builder(getActivity())
            // set dialog icon
            .setIcon(android.R.drawable.stat_notify_error)
    // set Dialog Title
    .setTitle("Alert dialog fragment example")
    // Set Dialog Message
    .setMessage("This is a message")

    // positive button
    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getActivity().getApplicationContext(), "Pressed OK", Toast.LENGTH_SHORT).show();
        }
    })
            // negative button
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(getActivity().getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
        }
    }).create();
}
}