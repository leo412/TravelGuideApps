package com.example.user.travelguideapps.EditLocation;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.travelguideapps.R;

public class EditLocationActivity extends Fragment {
    public static ConstraintLayout view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = (ConstraintLayout) inflater.inflate(R.layout.fragment_edit_location, container, false);
        } catch (InflateException e) {
    /* map is already there, just return view as it is */

        }

        return view;
    }
        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



}
