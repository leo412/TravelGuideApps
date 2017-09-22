package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.ConfirmationDialog;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.LoginPage.LoginActivity;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.SavedDataListFragment.OnListFragmentInteractionListener;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.dummy.DummyContent.DummyItem;
import com.example.user.travelguideapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SavedDataListAdapter extends RecyclerView.Adapter<SavedDataListAdapter.ViewHolder> {
    private static String ItemName;

    private final ArrayList<ArrayList> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private FragmentManager fragmentManager;

    public SavedDataListAdapter(FragmentManager con, ArrayList<ArrayList> items, OnListFragmentInteractionListener listener) {
        System.out.println("Whatispostbeforeitems THISITMES" + items);

        fragmentManager = con;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_saveddatalist, parent, false);

        return new ViewHolder(view);
    }

    //Global positiong sismple hack
    int globalPosition = -1;
    private View viewforselected;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Context mContext = holder.itemView.getContext();
        MapsActivity.pd.dismiss();

        holder.mItem = mValues.get(position).toString();
        holder.mIdView.setText(mValues.get(position).get(0).toString());
        String date;
        String time;
        //  System.out.println("onBindViewsaveddatalistadapter" + mValues.get(position).get(1));

        if (Long.parseLong(mValues.get(position).get(1).toString()) != 0 && Long.parseLong(mValues.get(position).get(1)
                .toString()) != 0) {

            date = new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date(Long.parseLong(mValues.get(position).get(1)
                    .toString()) * 1000)) + "->" + new java.text.SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date(Long.parseLong(mValues.get
                    (position).get(2)
                    .toString()) * 1000));
        } else {
            date = "N/A";

        }


        if (Long.parseLong(mValues.get(position).get(2)
                .toString()) != 0 && Long.parseLong(mValues.get(position).get(2)
                .toString()) != 0) {
            time = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(Long.parseLong(mValues.get(position).get(1)
                    .toString()) * 1000)) + "->" + new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date(Long.parseLong(mValues.get
                    (position).get(2)
                    .toString()) * 1000));

        } else {
            time = "N/A";

        }
//
//
        Picasso.with(mContext).load(R.drawable.fileicon).fit().into
                (holder.fileimage);
        Picasso.with(mContext).load(R.drawable.calendaricon).fit().into
                (holder.calendarimage);
        Picasso.with(mContext).load(R.drawable.timeicon).fit().into
                (holder.timeimage);

        Picasso.with(mContext).load(R.drawable.deleteicon).fit().into
                (holder.deleteimagebutton);
        Picasso.with(mContext).load(R.drawable.loadicon).fit().into
                (holder.loadimagebutton);
        Picasso.with(mContext).load(R.drawable.saveicon).fit().into
                (holder.saveimagebutton);
        holder.mTimeView.setText(time);
        holder.mContentView.setText(date);
        holder.deleteimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmationDialog dialog = new ConfirmationDialog();
                dialog.setArgs("Delete?", "Delete the file " + holder.mIdView.getText() + "? \nThe data is non recoverable.");
                Runnable confirm = new Runnable() {
                    @Override
                    public void run() {

                        try {


                            BaseActivity.mDatabase.child("users").child(LoginActivity.getUserID()).child(holder.mIdView.getText().toString())
                                    .setValue
                                            (null);
                            Toast.makeText(mContext, "Deleted successfully", Toast.LENGTH_SHORT).show();

                            if (fragmentManager.getBackStackEntryCount() > 0) {
                                fragmentManager.popBackStackImmediate();
                            }

                        } catch (Exception e) {

                            Toast.makeText(mContext, "Please select a file" + e, Toast.LENGTH_SHORT).show();

                        }

                    }
                };
                dialog.setConfirm(confirm);
                showDialogFragment(dialog);


            }
        });
        holder.saveimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!MapsActivity
                        .getWaypointwithDateList().isEmpty()) {

                    ConfirmationDialog dialog = new ConfirmationDialog();
                    dialog.setArgs("Save", "Save in '" + holder.mIdView.getText() + "' folder?");
                    Runnable confirm = new Runnable() {
                        @Override
                        public void run() {

                            try {


                                BaseActivity.mDatabase.child("users").child(LoginActivity.getUserID()).child(holder.mIdView.getText().toString())
                                        .setValue
                                                (MapsActivity
                                                        .getWaypointwithDateList());
                                Toast.makeText(mContext, "Data is saved successfully!", Toast.LENGTH_SHORT).show();

                                if (fragmentManager.getBackStackEntryCount() > 0) {
                                    fragmentManager.popBackStackImmediate();
                                }

                            } catch (Exception e) {

                                Toast.makeText(mContext, "Please select a file" + e, Toast.LENGTH_SHORT).show();

                            }

                        }
                    };
                    dialog.setConfirm(confirm);
                    showDialogFragment(dialog);


                } else {

                    Toast.makeText(mContext, "Please add locations first", Toast.LENGTH_SHORT).show();

                }
            }
        });
        holder.loadimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                 if (MapsActivity.pd.isShowing()) {                     MapsActivity.pd.dismiss();                 }                 MapsActivity.pd.show();


                final FirebaseDatabase database = FirebaseDatabase.getInstance();


                DatabaseReference ref = database.getReference().child("users").child(LoginActivity.getUserID())
                        .child(holder.mIdView.getText().toString());
                Query query = ref.orderByChild("starttime");
//TODO:input channel is not initialized Exception (Seems to be random bugs.
                //todo:addValueEventListener can auto save?
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("THIS IS IT" + dataSnapshot.getValue());
                   //     Toast.makeText(mContext, "Location Loaded ",  Toast.LENGTH_SHORT).show();
                                         if (MapsActivity.pd.isShowing()) {                     MapsActivity.pd.dismiss();                 }                 MapsActivity.pd.show();
                        ArrayList<HashMap> post = new ArrayList<HashMap>();
                        ArrayList<HashMap> nostarttime = new ArrayList<HashMap>();

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            HashMap Hash = new HashMap<>();
                            Hash.putAll((HashMap) childSnapshot.getValue());

                            if (Long.parseLong(Hash.get("starttime").toString()) != 0) {

                                post.add(Hash);
                            } else {
                                nostarttime.add(Hash);

                            }
                        }

                        if (!nostarttime.isEmpty()) {

                            post.addAll(nostarttime);


                        }


                        if (!dataSnapshot.getValue().equals("Empty")) {
                            System.out.println("THIS IS IT" + dataSnapshot.getValue());

                            //   ArrayList<HashMap> post = (ArrayList<HashMap>) dataSnapshot.getValue();
                            ArrayList array = new ArrayList();
                            array.add(post.get(0));
                            System.out.println("THISisthepostthat" + post);


                            System.out.println("THIS IS IT array" + array);


                            //Why should i have made this get 0...
                            MapsActivity.setWaypointwithDateList(post);
                            //Cannot directly update, findways to reset details

                            System.out.println("waypointdetailsList: " + MapsActivity
                                    .getWayPointDetailsList());
                            System.out.println("waypointdetailsList222: " + MapsActivity
                                    .getWaypointwithDateList());

                            //TODO changed from normal to support,( nope still might have error
                            //TODO:Must solved this too random

                            DataHolderClass.getBooleanhaschanges().setdistributor_idbooleanhaschanges(true);

                            if (fragmentManager.getBackStackEntryCount() > 0) {

                                fragmentManager.popBackStackImmediate();
                            }

                        } else {
MapsActivity.pd.dismiss();
                            Toast.makeText(mContext, "This file is empty, please choose another one!", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }


                });

                //      ref.removeEventListener(listener);

            }
        });

        System.out.println("Whatispostbeforeitems THISITMESName" + ItemName);
        if (getItemName() != null) {
            if (getItemName().equals(mValues.get(position).toString())) {
                holder.itemView.setSelected(true);


            }
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewforselected != null) {
                    viewforselected.setSelected(false);
                }
                viewforselected = v;
                v.setSelected(true);
                ItemName = mValues.get(position).get(0).toString();

                if (null != mListener) {


                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }


            }
        });


    }

    public void showDialogFragment(DialogFragment newFragment) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction. We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        // save transaction to the back stack
        ft.addToBackStack("dialog");
        newFragment.show(ft, "dialog");
    }

    public static String getItemName() {
        return ItemName;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mTimeView;
        public final ImageView fileimage;
        public final ImageView calendarimage;
        public final ImageView timeimage;


        public String mItem;
        public final ImageButton loadimagebutton;
        public final ImageButton saveimagebutton;
        public final ImageButton deleteimagebutton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.calendartext);
            mTimeView = (TextView) view.findViewById(R.id.overlapdate);
            fileimage = (ImageView) view.findViewById(R.id.fileimage);
            calendarimage = (ImageView) view.findViewById(R.id.calendarimage);
            timeimage = (ImageView) view.findViewById(R.id.timeimage);

            loadimagebutton = (ImageButton) view.findViewById(R.id.loadimagebutton);
            saveimagebutton = (ImageButton) view.findViewById(R.id.saveimagebutton);
            deleteimagebutton = (ImageButton) view.findViewById(R.id.deleteimagebutton);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }



}
