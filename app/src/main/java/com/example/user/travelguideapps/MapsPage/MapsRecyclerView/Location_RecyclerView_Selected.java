package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.LoginPage.LoginActivity;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Location_RecyclerView_Selected.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Location_RecyclerView_Selected#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Location_RecyclerView_Selected extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "HI";
    private static final String ARG_PARAM2 = "First";
    private static final String TAG ="RecyclerVIew2" ;
    private int scrollingtotop=1;

    private SelectedLocationListRecyclerViewAdapter Locationadapter;
    private RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference mDatabase;

    private Location_RecyclerView.OnFragmentInteractionListener mListener;

    public Location_RecyclerView_Selected() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Location_RecyclerView.
     */
    // TODO: Rename and change types and number of parameters
    public static Location_RecyclerView_Selected newInstance(String param1, String param2) {
        Location_RecyclerView_Selected fragment = new Location_RecyclerView_Selected();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location__recycler_view2, container, false);
        //     TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        //  tvLabel.setText(page + " -- " + title);

        ArrayList waypoint= MapsActivity.getWayPointDetailsList();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView)  view.findViewById(R.id.poilistRecyclerView);
        //When button is clicked, recycler view is called and nearbyplaceslist is being inserted into the system
        Button savefirebase = (Button) view.findViewById(R.id.save);
        Button loadfirebase = (Button) view.findViewById(R.id.load);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        savefirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Location Saved!  ", Toast.LENGTH_SHORT).show();

                //   final FirebaseDatabase database = FirebaseDatabase.getInstance();


                BaseActivity.mDatabase.child("users").child(LoginActivity.getUserID()).setValue(MapsActivity.getWaypoint());

            }
        });
        Locationadapter = new SelectedLocationListRecyclerViewAdapter(getActivity(), waypoint);

        loadfirebase.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(getActivity(), "Location Loaded!  ", Toast.LENGTH_SHORT).show();

                                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference ref = database.getReference().child("users").child(LoginActivity.getUserID());
                                                ref.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        ArrayList post = (ArrayList) dataSnapshot.getValue();

                                                        System.out.println("THIS IS IT" + post);
                                                      //  System.out.println("THIS IS IT" + LoginActivity.getUserID());
//
//                                                        System.out.println("THIS IS IT" + post.username);
//                                                        System.out.println("THIS IS IT" + post.waypoint);


                                                        MapsActivity.setWaypoint(post);
                                                        //Cannot directly update, findways to reset details,,,,
                                                       Locationadapter = new SelectedLocationListRecyclerViewAdapter(getActivity(), post);

                                                        Locationadapter.notifyDataSetChanged();

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        System.out.println("The read failed: " + databaseError.getCode());
                                                    }


                                                });
                                            }
                                        });



        Log.d(TAG, "recyclerview2 ssssss"+waypoint);

        
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

        recyclerView.setAdapter(Locationadapter);
        Locationadapter.notifyDataSetChanged();


        //   view = (ConstraintLayout)  inflater.inflate(R.layout.activity_maps, container, false);

        final ExpandableLayout expandableLayout
                = (ExpandableLayout) MapsActivity.view.findViewById(R.id.expandable_layout);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);




            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                int pastVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

          //      Log.d(TAG, "Scrollllllll"+dy);
                System.out.println("Scrollllllll"+dy);


                if(dx>0)
                {


                    System.out.println("Scrolled Right");

                }
                else if(dx < 0)
                {
                    System.out.println("Scrolled Left");

                }
                else {

                    System.out.println("No Horizontal Scrolled");
                }

                if(dy>0)
                {
                    expandableLayout.collapse();

                }
                else if(dy < 0) {
                    if (pastVisibleItems == 0) {

                    //    expandableLayout.expand();

                    }
                }


            }




        });

        //TODO:Little hack.......
        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {


                if(velocityY<-100&&scrollingtotop==1 ) {

                    expandableLayout.expand();
                }
                return false;
            }
        });





        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
