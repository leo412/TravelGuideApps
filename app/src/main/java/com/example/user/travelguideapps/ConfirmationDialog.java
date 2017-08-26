package com.example.user.travelguideapps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by User on 8/8/2017.
 */
public class ConfirmationDialog extends DialogFragment {
    // Do nothing by default
    private Runnable mConfirm = new Runnable() {
        @Override
        public void run() {
        }
    };
    // Do nothing by default
    private Runnable mCancel = new Runnable() {
        @Override
        public void run() {
        }
    };

    public void setArgs(String message) {
        setArgs("" , message);
    }

    public void setArgs(String title, String message) {
        Bundle args = new Bundle();
        args.putString("message", message);
        args.putString("title", title);
        setArguments(args);
    }

    public void setConfirm(Runnable confirm) {
        mConfirm = confirm;
    }

    public void setCancel(Runnable cancel) {
        mCancel = cancel;
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources res = getActivity().getResources();
        String title = getArguments().getString("title");
        return new AlertDialog.Builder(getActivity())
                .setTitle(title.equals("") ? res.getString(R.string.app_name) : title)
                .setMessage(getArguments().getString("message"))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mConfirm.run();
                     //   answerTrue.run();
                    }
                })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
mCancel.run();
               // answerFalse.run();
            }
        })
                .show();
    }
}