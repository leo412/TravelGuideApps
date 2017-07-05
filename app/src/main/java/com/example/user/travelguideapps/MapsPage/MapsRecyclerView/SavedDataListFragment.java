package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.LoginPage.LoginActivity;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SavedDataListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    public Button SaveButton;
    public Button AddNewButton;
    public Button LoadButton;
    private SelectedLocationListRecyclerViewAdapter Locationadapter;
    private Context mContext;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SavedDataListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static SavedDataListFragment newInstance(int columnCount) {
        SavedDataListFragment fragment = new SavedDataListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();



        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saveddatalist_list, container, false);
        System.out.println("fragment_saveddatalist_list oncreatveiwe ");
        SaveButton = (Button) view.findViewById(R.id.saveData);
        AddNewButton = (Button) view.findViewById(R.id.newData);
        LoadButton = (Button) view.findViewById(R.id.loadData);
        System.out.println("fragment_saveddatalist_list oncreatveiwe "+DataHolderClass.getInstance2());

        if(DataHolderClass.getInstance2().getDistributor_id2().equals("Save")){

    SaveButton.setVisibility(View.VISIBLE);
    AddNewButton.setVisibility(View.VISIBLE);
    LoadButton.setVisibility(View.INVISIBLE);


}else if(DataHolderClass.getInstance2().getDistributor_id2().equals("Load")){

    SaveButton.setVisibility(View.INVISIBLE);
    AddNewButton.setVisibility(View.INVISIBLE);
    LoadButton.setVisibility(View.VISIBLE);


}
        // Set the adapter
        Context context = view.getContext();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dataRecyclerViewList);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        System.out.println("WhatispostbeforeitemsBEFORE ");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //TODO:for whatrever reason if not logged in but managed to enter the syste,.....
        DatabaseReference ref = database.getReference().child("users").child(LoginActivity.getUserID());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    //TODO: check this empty logic more
                    if (!dataSnapshot.getValue().equals("Empty")) {
                        final ArrayList test = new ArrayList();

                        HashMap post = (HashMap) dataSnapshot.getValue();
                        System.out.println("Whatispostbeforeitems1 " + post);
                        System.out.println("Whatispostbeforeitemswhatiskeyterstingsdnffirst" + test);

                        if (post == null) {

                        } else {
                            ArrayList a = new ArrayList();
                            for (Object key : post.keySet()) {
                                System.out.println("Whatispostbeforeitemswhatiskey2 " + key);

                                test.add(key);
                            }
                        }
                        System.out.println("Whatispostbeforeitemswhatiskeyterstingsdnf" + test);

                        recyclerView.setAdapter(new MySavedDataListRecyclerViewAdapter(test, mListener));
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
        System.out.println("Whatispostbeforeitemsfinally 33wtf");




        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("THIS IS thevalue" + MapsActivity
                            .getWaypointwithDateList());

                    BaseActivity.mDatabase.child("users").child(LoginActivity.getUserID()).child(MySavedDataListRecyclerViewAdapter.getItemName())
                            .setValue
                                    (MapsActivity
                                            .getWaypointwithDateList());
                    Toast.makeText(getContext(), "Data is saved successfully!", Toast.LENGTH_SHORT).show();

                    if(getFragmentManager().getBackStackEntryCount() > 0){
                        getFragmentManager().popBackStackImmediate();
                    }

                } catch (Exception e) {

                    Toast.makeText(getContext(), "Please select a file" + e, Toast.LENGTH_SHORT).show();

                }

            }


        });

        AddNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText taskEditText = new EditText(getContext());
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Add a new File")
                        .setMessage("Please enter the File Name")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                //TODO: can add information like times or summary details
                                if (task != "") {
                                    BaseActivity.mDatabase.child("users").child(LoginActivity.getUserID()).child(task).setValue("Empty");

                                } else {
                                    Toast.makeText(getContext(), "Please enter a file name", Toast.LENGTH_SHORT).show();


                                }

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();


            }
        });
LoadButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference ref = database.getReference().child("users").child(LoginActivity.getUserID())
                                                        .child(MySavedDataListRecyclerViewAdapter.getItemName());
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        ArrayList post = (ArrayList) dataSnapshot.getValue();

                                                        System.out.println("THIS IS IT" + post);
                                                      //  System.out.println("THIS IS IT" + LoginActivity.getUserID());
//
//                                                        System.out.println("THIS IS IT" + post.username);
//                                                        System.out.println("THIS IS IT" + post.waypoint);

                                                        //Why should i have made this get 0...
                                                        MapsActivity.addWaypointwithDateList((ArrayList)post.get(0));
                                                        //Cannot directly update, findways to reset details,,,,
                                                     //  Locationadapter = new SelectedLocationListRecyclerViewAdapter(getActivity(), MapsActivity
                                                      //        .getWayPointDetailsList());

                                                   //     Locationadapter.notifyDataSetChanged();
                                                        System.out.println("waypointdetailsList: " + MapsActivity
                                                                .getWayPointDetailsList());
                                                        System.out.println("waypointdetailsList222: " + MapsActivity
                                                                .getWaypointwithDateList());
                                                        Toast.makeText(mContext, "Location Loaded!  ", Toast.LENGTH_SHORT).show();
//TODO changed from normal to support, nope still might have error
                                                        //TODO:Must solved this too random
                                                        if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0){
                                                            getActivity().getSupportFragmentManager().popBackStackImmediate();
                                                        }



                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        System.out.println("The read failed: " + databaseError.getCode());
                                                    }


                                                });



    }
});

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext=context;

//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext=null;
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String item);
    }
}
