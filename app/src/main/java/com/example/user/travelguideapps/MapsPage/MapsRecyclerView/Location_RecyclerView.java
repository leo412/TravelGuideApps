package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.IRangeBarFormatter;
import com.appyvet.rangebar.RangeBar;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.DetectConnection;
import com.example.user.travelguideapps.DialogCategoriesFragment;
import com.example.user.travelguideapps.LocationAutoCompleteAdapter;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;
import com.example.user.travelguideapps.SpacesItemDecoration;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Location_RecyclerView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Location_RecyclerView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Location_RecyclerView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "HI";
    private static final String ARG_PARAM2 = "First";
    private View fragmentpoilist;
    private int scrollingtotop = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LocationListRecyclerViewAdapter Locationadapter;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ExpandableLayout expandableLayout;
    private TextView emptyview;
    private static int radius;
    private static boolean switchtoggle = false;
    private static int minpriceint = -1;
    private static int maxpriceint = -1;
    private static String keyword = "";
    private static AutoCompleteTextView auto;
    ImageButton foodbutton;
    List<LinkedHashMap<String, Object>> nearbyPlacesList = new List<LinkedHashMap<String, Object>>() {


        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<LinkedHashMap<String, Object>> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(LinkedHashMap<String, Object> stringObjectLinkedHashMap) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends LinkedHashMap<String, Object>> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends LinkedHashMap<String, Object>> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public LinkedHashMap<String, Object> get(int index) {
            return null;
        }

        @Override
        public LinkedHashMap<String, Object> set(int index, LinkedHashMap<String, Object> element) {
            return null;
        }

        @Override
        public void add(int index, LinkedHashMap<String, Object> element) {

        }

        @Override
        public LinkedHashMap<String, Object> remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<LinkedHashMap<String, Object>> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<LinkedHashMap<String, Object>> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<LinkedHashMap<String, Object>> subList(int fromIndex, int toIndex) {
            return null;
        }
    };
    private boolean recyclerviewvisible = false;

    //TODO: the recyclerview will appear for a while even after reset
    public Location_RecyclerView() {
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
    public static Location_RecyclerView newInstance(String param1, String param2) {
        Location_RecyclerView fragment = new Location_RecyclerView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        System.out.println("Isthistunnableclickedviewolssafternotifydata");


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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location__recycler_view, container, false);
        // final ArrayList waypoint = MapsActivity.getWayPointDetailsList();

        nearbyPlacesList = MapsActivity.getNearbyPlacesList();
        System.out.println("insidefirst" + nearbyPlacesList);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        Button categories_expandable_layout = (Button) view.findViewById(R.id.opensetting);
        Button SelectTypes = (Button) view.findViewById(R.id.selecttypes);
        Button opencategories = (Button) view.findViewById(R.id.OpenCategoriesView);
        Button resetbutton = (Button) view.findViewById(R.id.resetbutton);
        Button sortbutton = (Button) view.findViewById(R.id.sortingbutton);

        DiscreteSeekBar numberpicker = (DiscreteSeekBar) view.findViewById(R.id.radiusPicker);
        final Switch switchopen = (Switch) view.findViewById(R.id.opennowtoggle);
        emptyview = (TextView) view.findViewById(R.id.emptyview);
        foodbutton = (ImageButton) view.findViewById(R.id.foodimagebutton);
        ImageButton entertainmentbutton = (ImageButton) view.findViewById(R.id.entertainmentimagebutton);

        ImageButton shopimagebutton = (ImageButton) view.findViewById(R.id.shopimagebutton);

        RangeBar pricerange = (RangeBar) view.findViewById(R.id.pricerangebar);
        Picasso.with(getActivity()).load(R.drawable.entertainmenticon).fit().into
                (entertainmentbutton);
        Picasso.with(getActivity()).load(R.drawable.shopicon).fit().into
                (shopimagebutton);
        Picasso.with(getActivity()).load(R.drawable.foodsymbol).fit().into
                (foodbutton);

        pricerange.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                minpriceint = leftPinIndex;
                maxpriceint = rightPinIndex;
//Should be ok but check first
            }
        });
        //TODO wtf price range bar cannot really check second one....
        //  pricerange.bringToFront();
        pricerange.setFormatter(new IRangeBarFormatter() {
            @Override
            public String format(String s) {
                // Transform the String s here then return s'
                String returnstring = "0";

                switch (s) {
                    case "0":
                        returnstring = "Free";
                        break;
                    case "1":
                        returnstring = "$";
                        break;
                    case "2":
                        returnstring = "$$";
                        break;
                    case "3":
                        returnstring = "$$$";
                        break;
                    case "4":
                        returnstring = "$$$$";
                        break;

                }


                return returnstring;
            }
        });


        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView) view.findViewById(R.id.autocompletetext);
        autocompleteView.setAdapter(new LocationAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item));
        auto = autocompleteView;
        autocompleteView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Log.e("TAG", "Done pressed");
                }
                return false;
            }
        });
        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                String description = (String) parent.getItemAtPosition(position);
                keyword = autocompleteView.getText().toString();
                InputMethodManager inputMethodManager =
                        (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                //   Toast.makeText(getActivity().getApplicationContext(), description, Toast.LENGTH_SHORT).show();
            }
        });

        switchopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                switchtoggle = isChecked;


            }
        });
        opencategories.setOnClickListener(onClickListener);
        SelectTypes.setOnClickListener(onClickListener);
        shopimagebutton.setOnClickListener(onClickListener);
        foodbutton.setOnClickListener(onClickListener);
        entertainmentbutton.setOnClickListener(onClickListener);
        resetbutton.setOnClickListener(onClickListener);

        sortbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO: add saying that                 builder.setMessage("*Sorting is only for viewing purpose only and will not be saved");

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Sorting");
                builder.setItems(new CharSequence[]
                                {"By Name", "By rating", "button 3", "button 4"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        System.out.println("dudeUhhhyeh 1111" + nearbyPlacesList);
                                        Collections.sort(nearbyPlacesList, new Comparator<LinkedHashMap>() {
                                            @Override
                                            public int compare(LinkedHashMap o1, LinkedHashMap o2) {
                                                //  System.out.println("dudeUhhhyeh 1111" + waypoint);

                                                System.out.println("dudeUhhhyeh 3333" + o1.toString());
                                                System.out.println("dudeUhhhyeh 4444" + o2.toString());
                                                System.out.println("dudeUhhhyeh 5555555" + o2.get("rating").toString().compareTo(o1.get("rating")
                                                        .toString()));

                                                return o1.get("place_name").toString().compareTo(o2.get("place_name").toString());
                                            }
                                        });
                                        //            Collections.sort(nearbyPlacesList, new MapComparator("place_name"));
//
                                        //    MapsActivity.setWayPointDetailsList(waypoint);

                                        break;
                                    case 1:
                                        // System.out.println("dudeUhhhyeh 2222" + waypoint);
                                        Collections.sort(nearbyPlacesList, new Comparator<LinkedHashMap>() {
                                            @Override
                                            public int compare(LinkedHashMap o1, LinkedHashMap o2) {

                                                System.out.println("dudeUhhhyeh 3333" + o1.toString());
                                                System.out.println("dudeUhhhyeh 4444" + o2.toString());
                                                System.out.println("dudeUhhhyeh 5555555" + o2.get("rating").toString().compareTo(o1.get("rating")
                                                        .toString()));

                                                return o2.get("rating").toString().compareTo(o1.get("rating").toString());
                                            }
                                        });
                                        //        Collections.sort(nearbyPlacesList, new MapComparator("rating"));
                                        // MapsActivity.setWayPointDetailsList(waypoint);


                                        break;
                                    case 2:
                                        //     System.out.println("dudeUhhhyeh 3333" + waypoint);

//
                                        break;
                                    case 3:
                                        // System.out.println("dudeUhhhyeh 444" + waypoint);

                                        break;


                                }
                                //     System.out.println("dudeUhhhyeh 2222" + waypoint);

                                //      MapsActivity.setWayPointDetailsList(waypoint);
                                Locationadapter.notifyDataSetChanged();

                            }
                        });
                builder.create().show();


            }
        });
        categories_expandable_layout.setOnClickListener(onClickListener);
        expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);
        expandableLayout.collapse();

        recyclerView = (RecyclerView) view.findViewById(R.id.poilistRecyclerView);

        //When button is clicked, recycler view is called and nearbyplaceslist is being inserted into the system
        Locationadapter = new LocationListRecyclerViewAdapter(nearbyPlacesList);
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
        recyclerView.addItemDecoration(new SpacesItemDecoration(50));

        recyclerView.setAdapter(Locationadapter);
        Locationadapter.notifyDataSetChanged();
        System.out.println("checknearbyPlacesList1 " + nearbyPlacesList);
        System.out.println("checknearbyPlacesList2 " + MapsActivity.getNearbyPlacesList());
        System.out.println("checknearbyPlacesList3  " + recyclerviewvisible);


// Extend the Callback class


        if (recyclerviewvisible) {
            recyclerView.setVisibility(View.VISIBLE);


        } else {

//            if(!nearbyPlacesList.isEmpty()) {
//                System.out.println("checknearbyPlacesList1 "+nearbyPlacesList);
//                System.out.println("checknearbyPlacesList2 "+MapsActivity.getNearbyPlacesList());
//
//
//
//                MapsActivity.clearNearbyPlacesList();
//                nearbyPlacesList.clear();
//            }
            Locationadapter.notifyDataSetChanged();
            //  recyclerView.setVisibility(View.GONE);

        }

        numberpicker.setNumericTransformer(new DiscreteSeekBar.NumericTransformer() {
            @Override
            public int transform(int value) {
                radius = value * 1000;
                return value;
            }
        });


        //  radius = numberpicker.getNumericTransformer().transform(numberpicker.getProgress());


//        final ExpandableLayout expandableLayout
//                = (ExpandableLayout) MapsActivity.view.findViewById(R.id.expandable_layout);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            //
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int pastVisibleItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (dx > 0) {

                    System.out.println("Scrolled Right");

                } else if (dx < 0) {
                    System.out.println("Scrolled Left");

                } else {

                    System.out.println("No Horizontal Scrolled");
                }

                if (dy > 0) {
                    expandableLayout.collapse();

                    System.out.println("Scrolled Downwards");
                } else if (dy < 0) {
                    //  recyclerView.smoothScrollBy(0,10);

                    if (pastVisibleItems == 0) {
                        scrollingtotop = 1;

                    } else {
                        scrollingtotop = 0;
                    }
                    //expandableLayout.expand();

                } else {

                    System.out.println("No Vertical Scrolled");
                }
            }

        });


        //TODO:Little hack.......
        recyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {


                if (velocityY < -100 && scrollingtotop == 1) {

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

    public static int getradius() {

        return radius;
    }

    public static boolean getswitch() {

        return switchtoggle;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!DetectConnection.checkInternetConnection(getActivity())) {
                Toast.makeText(getActivity().getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
            } else

            {
                switch (v.getId()) {

                    case R.id.OpenCategoriesView:

                        //Open dialog,.,,
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        ft.addToBackStack(null);

                        // Create and show the dialog.
                        DialogCategoriesFragment newFragment = DialogCategoriesFragment.newInstance("Name");
                        newFragment.show(getActivity().getFragmentManager(), "dialog");


                        //   expandablecategories.toggle();
                        break;

                    case R.id.opensetting:


                        expandableLayout.toggle();


                        break;
                    case R.id.selecttypes:
                        StringBuilder LocationType = new StringBuilder();
                        if (!DetectConnection.checkInternetConnection(getActivity())) {
                            Toast.makeText(getActivity().getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                        } else

                        {
//                            if(!nearbyPlacesList.isEmpty()) {
//                                MapsActivity.clearNearbyPlacesList();
//                                nearbyPlacesList.clear();
//                            }
                            keyword = auto.getText().toString();

                            ArrayList array = DataHolderClass.getInstance3().getDistributor_id3();
                            //TODO: For what?
                            MapsActivity.setcolor(false);

                            MapsActivity.getMap().clear();
                            if (array != null) {
                                for (int i = 0; i < array.size(); i++) {

                                    LocationType.append(array.get(i) + "|");


                                }
                            }
                            try {


                                if (LocationType.toString() == "" && keyword.equals("")) {

                                    Toast.makeText(getActivity().getApplicationContext(), "Please Select at least one type or keyword", Toast
                                            .LENGTH_SHORT).show();

                                } else {

                                    expandableLayout.collapse();
                                    MapsActivity.pd.show();
                                    StringBuilder a = nearbyListBuilder(LocationType);
                                    //  emptyview.setVisibility(View.GONE);
                                    recyclerviewvisible = true;

                                    //     recyclerView.setVisibility(View.VISIBLE);
                                    MapsActivity.setRecyclerView(a);
                                }

                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        Log.d("Locationrecyclerview", "Runned hmmm ");
                        break;
                    case R.id.entertainmentimagebutton:
                        if (!DetectConnection.checkInternetConnection(getActivity())) {
                            Toast.makeText(getActivity().getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                        } else

                        {

                            //TODO: For what?
                            MapsActivity.setcolor(false);

                            MapsActivity.getMap().clear();

                            try {


                                StringBuilder s = new StringBuilder();
                                s.append("amusement_park|aquarium|bowling_alley|campground|library");
                                expandableLayout.collapse();
                                MapsActivity.pd.show();
                                StringBuilder a = nearbyListBuilder(s);
                                //  emptyview.setVisibility(View.GONE);
                                recyclerviewvisible = true;

                                //       recyclerView.setVisibility(View.VISIBLE);
                                MapsActivity.setRecyclerView(a);
                                //   foodbutton.setVisibility(View.GONE);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.shopimagebutton:
                        if (!DetectConnection.checkInternetConnection(getActivity())) {
                            Toast.makeText(getActivity().getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                        } else

                        {

                            //TODO: For what?
                            MapsActivity.setcolor(false);

                            MapsActivity.getMap().clear();

                            try {


                                StringBuilder s = new StringBuilder();
                                s.append("clothing_store|convenience_store|department_store|shoe_store|store");
                                expandableLayout.collapse();

                                //TODO:
                                //08-25 10:42:05.085 28780-28780/com.example.user.travelguideapps W/art: /data/app/com.example.user
                                //        .travelguideapps-2/split_lib_dependencies_apk.apk has in excess of 100 dex files. Please consider
                                //          coalescing and shrinking the number to  avoid runtime overhead.

                                MapsActivity.pd.show();
                                StringBuilder a = nearbyListBuilder(s);
                                //  emptyview.setVisibility(View.GONE);
                                recyclerviewvisible = true;

                                //     recyclerView.setVisibility(View.VISIBLE);
                                MapsActivity.setRecyclerView(a);
                                //   foodbutton.setVisibility(View.GONE);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.foodimagebutton:
                        if (!DetectConnection.checkInternetConnection(getActivity())) {
                            Toast.makeText(getActivity().getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                        } else

                        {

                            //TODO: For what?
                            MapsActivity.setcolor(false);

                            MapsActivity.getMap().clear();

                            try {


                                StringBuilder s = new StringBuilder();
                                s.append("restaurant|bakery|bar|cafe");
                                expandableLayout.collapse();
                                MapsActivity.pd.show();
                                StringBuilder a = nearbyListBuilder(s);
                                //  emptyview.setVisibility(View.GONE);
                                recyclerviewvisible = true;

                                // recyclerView.setVisibility(View.VISIBLE);
                                MapsActivity.setRecyclerView(a);
                                //   foodbutton.setVisibility(View.GONE);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (java.lang.InstantiationException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.resetbutton:
                        recyclerviewvisible = false;
                        recyclerView.setVisibility(View.GONE);
                        Locationadapter.notifyDataSetChanged();
//                        Fragment frg = null;
//                        frg = getFragmentManager().findFragmentByTag("Your_Fragment_TAG");
//                        ft = getFragmentManager().beginTransaction();
//                        ft.detach(frg);
//                        ft.attach(frg);
//                        ft.commit();
                        break;

                }

            }
        }

    };

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

    public static StringBuilder nearbyListBuilder(StringBuilder locationtype) {
        //TODO:probably fixed... location null
        Location location = MapsActivity.CurrentLocation;

        try {
            if (location != null) {
                //use your current location here
                double mLatitude = location.getLatitude();
                double mLongitude = location.getLongitude();

                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + mLatitude + "," + mLongitude);
                //TODO: Set radius to be controllable
                Log.d("Map", "keyword=location=: " + sb.toString());


                if (locationtype.length() != 0) {
                    locationtype.deleteCharAt(locationtype.length() - 1);
                }
                // sb.append("&radius=5000");
                //Todo: figure out how to support multiple types (location)..........
                //IMPORTANT
                sb.append("&radius=" + Integer.toString(getradius()));

                sb.append("&types=" + locationtype);
                Log.d("Map", "keyword=: " + sb.toString());

                if (keyword != null) {
                    Log.d("Map", "keyword=: " + keyword.toString());
                    keyword = keyword.replace(",", "%2C");

                    keyword = keyword.replace(" ", "%20");


                    sb.append("&keyword=" + keyword);

                }
                Log.d("Map", "afterkeyword=: " + sb.toString());

                if (Location_RecyclerView.getswitch()) {
                    sb.append("&opennow=true");


                }

                //if -1 then anything.....TODO:problem of price cannot detect
                if (minpriceint == 0) {
                    minpriceint = -1;

                }
                if (maxpriceint == 4) {
                    maxpriceint = -1;

                }


                sb.append("&minprice=" + minpriceint + "&maxprice=" + maxpriceint);
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyDkIa12Y9nXORou_xCnwS09K53kbJabKHo");
                ;
                Log.d("Map", "api: " + sb.toString());


                return sb;
            } else {

                return null;
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder("Fail");
            Log.d("Location_RecyclerVIew ", e.toString());

            return sb.append("Test");
        }

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
