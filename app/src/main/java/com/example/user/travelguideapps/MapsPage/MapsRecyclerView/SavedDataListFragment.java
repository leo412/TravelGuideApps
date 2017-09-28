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
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.LoginPage.LoginActivity;
import com.example.user.travelguideapps.R;
import com.example.user.travelguideapps.SpacesItemDecoration;
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
    public Button AddNewButton;
    ImageButton loadimagebutton;
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
        //      mContext = getActivity();


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saveddatalist_list, container, false);
        System.out.println("fragment_saveddatalist_list oncreatveiwe ");
        AddNewButton = (Button) view.findViewById(R.id.newData);
        System.out.println("fragment_saveddatalist_list oncreatveiwe " + DataHolderClass.getInstance2());
//
//        if (DataHolderClass.getInstance2().getDistributor_id2().equals("Save")) {
//
//            AddNewButton.setVisibility(View.VISIBLE);
//
//
//        } else if (DataHolderClass.getInstance2().getDistributor_id2().equals("Load")) {
//
//            AddNewButton.setVisibility(View.INVISIBLE);
//
//
//        }
        // Set the adapter
        final Context context = view.getContext();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dataRecyclerViewList);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.addItemDecoration(new SpacesItemDecoration(50));

//she q
        System.out.println("WhatispostbeforeitemsBEFORE ");
        try {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("users").child(LoginActivity.getUserID());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        ArrayList<ArrayList> arrayarray = new ArrayList<ArrayList>();

                        //TODO: check this empty logic more
                        if (!dataSnapshot.getValue().equals("Empty")) {
                            final ArrayList<ArrayList> test = new ArrayList<ArrayList>();

                            HashMap post = (HashMap) dataSnapshot.getValue();
                            System.out.println("Whatispostbeforeitems1 " + post);
                            System.out.println("Whatispostbeforeitemswhatiskeyterstingsdnffirst" + test);
                            System.out.println("HA!datasnapshot" + dataSnapshot.getValue());

                            if (post == null) {

                            } else {
                                ArrayList a = new ArrayList();
                                ArrayList<HashMap> arrayarrayforloop;

//For every item (4)
                                for (Object key : post.keySet()) {
                                    //  HashMap b = key;
                                    System.out.println("Whatispostbeforeitemswhatiskey2 " + key);

                                    System.out.println("Whatispostbeforeitemswhatiskey2 " + post.get(key));
                                    ArrayList array = new ArrayList();

                                    //          String testing = Collections.max(post.values()).toString();
                                    System.out.println("gahhhhh " + post);

                                    // System.out.println("gahhhhh " + array.get(0).get(1));
                                    array.add(key);
                                    long mindate = 0;
                                    long maxdate = 0;
                                    if (!post.get(key).equals("Empty")) {
                                        arrayarrayforloop = (ArrayList<HashMap>) post.get(key);


                                        if ((long) arrayarrayforloop.get(0).get("starttime") != 0) {
                                            mindate = Long.parseLong(arrayarrayforloop.get(0).get("starttime").toString());
                                        }
                                        if ((long) arrayarrayforloop.get(0).get("duration") != 0) {
                                            maxdate = Long.parseLong(arrayarrayforloop.get(0).get("duration").toString()) + mindate;

                                        }
                                        for (int j = 0; j < arrayarrayforloop.size(); j++) {
                                            System.out.println("enterhere " + arrayarrayforloop);

                                            if ((long) arrayarrayforloop.get(j).get("starttime") != 0) {

                                                System.out.println("enterhere2 " + mindate);

                                                if (Long.parseLong(arrayarrayforloop.get(j).get("starttime").toString()) < mindate) {

                                                    mindate = Long.parseLong(arrayarrayforloop.get(j).get("starttime").toString());
                                                }

                                            }
                                            if ((long) arrayarrayforloop.get(j).get("duration") != 0) {
                                                System.out.println("enterhere3 " + arrayarrayforloop.get(j).get("duration"));

                                                if (Long.parseLong(arrayarrayforloop.get(j).get("starttime").toString()) + Long.parseLong
                                                        (arrayarrayforloop.get(j)

                                                                .get("duration").toString()) > maxdate) {
                                                    System.out.println("enterhere4 " + arrayarrayforloop.get(j).get("duration"));

                                                    maxdate = Long.parseLong(arrayarrayforloop.get(j).get("starttime").toString()) + Long.parseLong
                                                            (arrayarrayforloop.get(j).get("duration").toString());
                                                }

                                            }

                                        }
                                    } else {


                                    }

                                    if (mindate != 0) {
                                        array.add(mindate);
                                        if (maxdate != 0) {
                                            array.add(maxdate);

                                        } else {
                                            array.add(0L);
                                        }
                                    } else {
                                        if (!post.get(key).equals("Empty")) {
//TODO: check problems here
                                            array.add(0L);
                                            array.add(0L);


                                            //       array.add("Time Not Selected");
                                            //         array.add("Time Not Selected");

                                        } else {
                                            array.add(0L);
                                            array.add(0L);

                                            //x    array.add("No location Selected");
                                            //   array.add("No location Selected");


                                        }
                                    }
                                    arrayarray.add(array);
                                    System.out.println("keepaddingarrmaxdate  " + mindate);
//former father
                                    System.out.println("keepaddingarrsmallarrya  " + array);

                                    System.out.println("keepaddingarr " + arrayarray);


                                    //test.add(arrayarray);
                                }
                            }


                            System.out.println("Whatispostbeforeitemswhatiskeyterstingsdnf" + test);
//TODO:No adapter attached; skipping layout error in Fragment...Happen when the apps "restarted or something

                            recyclerView.setAdapter(new SavedDataListAdapter(getFragmentManager(), arrayarray, mListener));
                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }

            });
        } catch (Exception e) {

            System.out.println("SavedDataListFragmentException" + e);

        }
        //   ref.setValue(ServerValue.TIMESTAMP);

        System.out.println("Whatispostbeforeitemsfinally 33wtf");


        final Boolean[] wantToCloseDialog = {false};
        AddNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText taskEditText = new EditText(getContext());
                final AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Add a new File")
                        .setMessage("Please enter the File Name")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task = taskEditText.getText().toString();
                        //TODO: can add information like times or summary details
                        Toast.makeText(getContext(), "Please" + task + "Plase", Toast.LENGTH_SHORT).show();

                        if (task.length() != 0) {
                            BaseActivity.mDatabase.child("users").child(LoginActivity.getUserID()).child(task).setValue("Empty");
                            wantToCloseDialog[0] = true;
                        } else {
                            Toast.makeText(getContext(), "Please enter a file name", Toast.LENGTH_SHORT).show();
                            wantToCloseDialog[0] = false;


                        }
                        //Do stuff, possibly set wantToCloseDialog to true then...
                        if (wantToCloseDialog[0])
                            dialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });

            }
        });

        System.out.println("Before getting saveddata");

        return view;
    }

    //Remember this bitches
//Transfer E1U7sXJxaV chu culain, herc, chamilia abc123
    // tama cat, maria anato PaQnW5zZ6B
    //cu culain, elizi, the green hair arcjer, martha
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext = context;

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
        mContext = null;
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
