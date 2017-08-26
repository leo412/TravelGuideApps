package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;
import com.example.user.travelguideapps.SpacesItemDecoration;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    private static final String TAG = "RecyclerVIew2";
    private int scrollingtotop = 1;

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
        Log.d(TAG, "recyclerview2 newinstance oncrea");

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
        Log.d(TAG, "recyclerview2 newinstance");

        final ArrayList<LinkedHashMap<String, Object>> waypoint = MapsActivity.getWayPointDetailsList();


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.poilistRecyclerView);
        //When button is clicked, recycler view is called and nearbyplaceslist is being inserted into the system
        //      Button savefirebase = (Button) view.findViewById(R.id.save);
        Button loadfirebase = (Button) view.findViewById(R.id.load);
        Button mapdirection = (Button) view.findViewById(R.id.mapdirection);
        Button sorting = (Button) view.findViewById(R.id.sorting);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (MapsActivity.getWaypointwithDateList().isEmpty()) {
            //TODO: might need to change it to call toast or something
            mapdirection.setEnabled(false);
        } else {
            mapdirection.setEnabled(true);

        }


        Locationadapter = new SelectedLocationListRecyclerViewAdapter(getActivity(), waypoint);

        loadfirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.app.FragmentManager manager = getActivity().getFragmentManager();
                android.app.Fragment frag = manager.findFragmentByTag("fragment_edit_name");
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }
                DataHolderClass.getInstance2().setDistributor_id2("Load");
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = SavedDataListFragment.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).addToBackStack(null).commit();


            }
        });


        mapdirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    MapsActivity.downloadUrl download = new MapsActivity.downloadUrl(getContext());

                //download.execute("https://www.google.com/maps/dir/?api=1");

                ArrayList a = new ArrayList();
                StringBuilder b = new StringBuilder();
                //    StringBuilder c= new StringBuilder();
                String c = new String();
                String dest = new String();
                ArrayList<HashMap> array = MapsActivity.getWaypointwithDateList();
                dest = array.get(array.size() - 1).get("place_id").toString();

                for (int i = 0; i < array.size() - 1; i++) {
                    a.add(array.get(i).get("place_id"));
                    b.append(array.get(i).get("place_id") + "|");
                    c = b.toString();
                    c = c.substring(0, b.length() - 1);

                }
                Location CurrentLocation = BaseActivity.getCurrentLocation();
                LatLng latlng = new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
                String lat = Double.toString(CurrentLocation.getLatitude());
                String lng = Double.toString(CurrentLocation.getLongitude());
//TODO: Destination= A is just a placeholder, have no idea whether some will work........(SEEEMS TO work
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + lat + "," + lng +
                        "&destination=A&destination_place_id=" + dest
                        + "&waypoints=" + c + "&waypoint_place_ids=" + c));
                getContext().startActivity(intent);

            }
        });
        Log.d(TAG, "recyclerview2 ssssss" + waypoint.size());


        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.addItemDecoration(new SpacesItemDecoration(50));

        recyclerView.setAdapter(Locationadapter);
        Locationadapter.notifyDataSetChanged();
        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Collections.swap(waypoint, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                Locationadapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());


                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };

        sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO: add saying that                 builder.setMessage("*Sorting is only for viewing purpose only and will not be saved");
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Sorting");
                    builder.setItems(new CharSequence[]
                                    {"By Name", "By rating", "By Time", "button 4"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    switch (which) {
                                        case 0:
                                            System.out.println("debuyaooh 1111" + waypoint);

                                            Collections.sort(waypoint, new Comparator<HashMap>() {
                                                @Override
                                                public int compare(HashMap o1, HashMap o2) {


                                                    return o1.get("place_name").toString().compareTo(o2.get("place_name").toString());
                                                }


                                            });
                                            MapsActivity.setWayPointDetailsList(waypoint);

                                            break;
                                        case 1:
                                            //    Collections.sort(waypoint, new MapComparator("rating"));

                                            Collections.sort(waypoint, new Comparator<HashMap>() {
                                                @Override
                                                public int compare(HashMap o1, HashMap o2) {


                                                    return o2.get("rating").toString().compareTo(o1.get("rating").toString());
                                                }


                                            });
                                            MapsActivity.setWayPointDetailsList(waypoint);


                                            break;
                                        case 2:

                                            for (int i = 0; i < MapsActivity.getWaypointwithDateList().size(); i++) {
                                                if (MapsActivity.getWaypointwithDateList().get(i).get("place_id").equals(waypoint.get(i).get
                                                        ("place_id"))) {
                                                    waypoint.get(i).put("starttime", MapsActivity.getWaypointwithDateList().get(i).get("starttime"));
                                                    System.out.println("waypointaddstarttime" + "  Success");
                                                }
                                                System.out.println("waypointaddstarttime" + MapsActivity.getWaypointwithDateList().get(i).get
                                                        ("starttime"));


                                                System.out.println("waypointaddstarttime" + waypoint);

                                            }

                                            Collections.sort(waypoint, new Comparator<HashMap>() {
                                                @Override
                                                public int compare(HashMap o1, HashMap o2) {
                                                    System.out.println("waypointaddstarttimechecking" + o1.get("starttime"));
                                                    System.out.println("waypointaddstarttimechecking" + o2.get("starttime"));
//TODO: seems to work, need testing
                                                    if (o1.get("starttime").toString().equals("0") ) {
                                                        return 1;
                                                    }
                                                    if (o2.get("starttime").toString().equals("0")) {
                                                        return -1;
                                                    }
//                                                    if (o1.get("starttime").toString().equals("0") || o2.get("starttime").toString().equals("0")) {
//
//                                                        System.out.println("waypointaddstarttimechecking" + " Has not run here");
//
//                                                        return 0;
//
//                                                    } else {

                                                        System.out.println("waypointaddstarttimechecking" + " Has run here");

                                                        return o1.get("starttime").toString().compareTo(o2.get("starttime").toString());
                                              //      }

                                                }


                                            });
                                            break;
                                        case 3:
                                            break;


                                    }
                                    System.out.println("dudeUhhhyeh 2222" + waypoint);

                                    //    MapsActivity.setWayPointDetailsList(waypoint);
                                    Locationadapter.notifyDataSetChanged();

                                }
                            });
                    builder.create().show();

                } catch (Exception e) {
                    Log.d(TAG, "ome error has occured! " + e.toString());


                    Toast.makeText(getActivity(), "Some error has occured! " + e, Toast.LENGTH_SHORT).show();

                }
            }
        });
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(recyclerView);


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
