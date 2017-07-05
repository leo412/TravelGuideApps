package com.example.user.travelguideapps.MapsPage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.DataParser;
import com.example.user.travelguideapps.DetectConnection;
import com.example.user.travelguideapps.DirectionsFetcher;
import com.example.user.travelguideapps.FirebaseDatabaseUser;
import com.example.user.travelguideapps.LoginPage.LoginActivity;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.LocationListRecyclerViewAdapter;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.Location_RecyclerView;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.Location_RecyclerView_Selected;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.SelectedLocationListRecyclerViewAdapter;
import com.example.user.travelguideapps.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrules.horizontalnumberpicker.HorizontalNumberPicker;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.user.travelguideapps.R.id.map;

//How should this works
//Obtain Waypoints -> send using "Directions" to google, write new path->
//AVOID Diresctions ?

//Note: put checkbox in the listview to show its selected/change marker colour?
//show 1 -> 2....but how to check which one to start?
//Create an array for ALL waypoints..
//Has current position (Modify to let users find wanted position?
//Make an "Selected places.page?, show all time/ location/ distance/ and arrangable
//https://developer.android.com/guide/topics/ui/drag-drop.html use drag and drop is eariser...?
//if drag and drop not working....

//TODO: that freakin disable View Details from other listview item  problem....
//TODO: marker does not show up for "Selected Location" required (Sometimes only)
//TODO: make different kind of marker for different "maps" (kinda?
//TODO:Design interface
//TODO:Sometimes picture not showing in selected <-wtf?
//TODO: Add all place types
public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient
        .OnConnectionFailedListener, LocationListener {
    private static final String TAG = "tag";
    static ArrayList<LatLng> markerPoints;
    private static final String TAG_HOME = "home";
    public static String CURRENT_TAG = TAG_HOME;
    public static int navItemIndex = 0;
    private FloatingActionButton fab;
    private Handler mHandler;
    private RecyclerView recyclerView;
    private StringBuilder LocationType;
    private View mProgressView;
    private View mMapFormView;
    private View horizontal_scroll_view;
    private View fragmentpoilist;
    private static ViewPager pager;
    private static FragmentPagerAdapter recycleadapter;

    private ExpandableLayout expandableLayout;
    private ExpandableLayout FoodLayout;
    private ExpandableLayout EntertainmentLayout;

    private ExpandableLayout ShopLayout;
    private ExpandableLayout expandablecategories;

    private static TextView mDurationView;
    private static TextView mDistanceView;
    private LocationListRecyclerViewAdapter Locationadapter;
   // private static ArrayList WaypointwithDateList = new ArrayList();

    private static ArrayList<ArrayList> WaypointwithDateList = new ArrayList<ArrayList>();


    private static Boolean newcolor = false;
    private HorizontalNumberPicker numberpicker;
    private static Location CurrentLocation = BaseActivity.getCurrentLocation();
    private int minpriceint = -1;
    private int maxpriceint = -1;

    private static Polyline TempPoly;
    private static List<LinkedHashMap<String, String>> nearbyPlacesList;
    private static ArrayList<LinkedHashMap<String, String>> WayPointDetailsList = new ArrayList<LinkedHashMap<String, String>>();

    private DatabaseReference mDatabase;
    // ...
    private static FragmentActivity mInstance;
    private static Resources mGetResources;

    static ProgressDialog pd;
    GestureDetector gestureDetector;

    int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0;
    private static GoogleMap mMap;
    LocationRequest mLocationRequest = new LocationRequest();
    static ArrayList<Marker> mapMarkers;
    static ArrayList<Marker> mapMarkersForSelected = new ArrayList<Marker>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Get waypoints from others

        String b = DataHolderClass.getInstance().getDistributor_id();
        mInstance = getActivity();
        mGetResources = getResources();

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = (ConstraintLayout) inflater.inflate(R.layout.activity_maps, container, false);
        } catch (InflateException e) {
    /* map is already there, just return view as it is */

        }


        fragmentpoilist = (ConstraintLayout) inflater.inflate(R.layout.fragment_location__recycler_view, container, false);
        boolean isFirstTime = true;

        //Little hack
        if (expandableLayout == null) {
            isFirstTime = true;

        } else {
            isFirstTime = false;

        }
        expandableLayout = (ExpandableLayout) view.findViewById(R.id.expandable_layout);

        //For swipping on the "Selection"
        gestureDetector = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            private static final int SWIPE_MIN_DISTANCE = 150;
            private static final int SWIPE_MAX_OFF_PATH = 100;

            private static final int SWIPE_THRESHOLD_VELOCITY = 100;
            // private final Activity activity;
            protected MotionEvent mLastOnDownEvent = null;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //TODO: Some specific details might be add?(like only bottom, some signal etc

                System.err.println("onFling");
                System.out.println(e1 + " " + e2);
                if (e1 == null)
                    e1 = mLastOnDownEvent;
                if (e1 == null || e2 == null)
                    return false;

                float dX = e2.getX() - e1.getX();
                float dY = e2.getY() - e1.getY();

                if (Math.abs(dX) < SWIPE_MAX_OFF_PATH && Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dY) >= SWIPE_MIN_DISTANCE) {

                    if (dY > 0) {
                        // Toast.makeText(getContext(),"Uh s", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onfling");

                        expandableLayout.expand();

                        // activity.fetchPrevious();
                    } else {
                        // activity.fetchNext();
                        Log.d(TAG, "onflingnot");

                        expandableLayout.collapse();

                    }

                    return true;
                }

                return false;
            }
        });
//Run the aboce code
        expandableLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                return gestureDetector.onTouchEvent(event);

            }
        });

//Show layout,
        if (isFirstTime) {
            Log.d(TAG, "isfirsttime");

            expandableLayout.expand();


        }
        Log.d(TAG, "gogogo");

//Must changed this, but to what?.
        //Is the clicked existing...
        //if b != start here

        String isselected = DataHolderClass.getInstance2().getDistributor_id2();

/////////////////////////////////////////////////
//Will this just run from DetailsActivity?
//            if (isselected == "isselected") {
//                //Check b cus b not run
////When trying to delete waypoint selected
//                WaypointwithDateList.remove(b);
//                Log.d(TAG, "cccccyay waydetails Entered");
//
//                for (int i = 0; i < WayPointDetailsList.size(); i++) {
//                    LinkedHashMap<String, String> s = WayPointDetailsList.get(i);
//                    Log.d(TAG, "cccccyay waydetails removed" + s);
//                    Log.d(TAG, "cccccyay waydetails removedsize" + WayPointDetailsList.size());
//                    Log.d(TAG, "cccccyay waydetails removedsize" + WaypointwithDateList.size());
//
//
//                    if (s.containsValue(b)) {
//                        WayPointDetailsList.remove(i);
//                        Log.d(TAG, "cccccyay waydetails removed");
//
//                    }
//
//
//                }
//            } else {
////////////////////////////////////////
        //Adding waypoints slected, try to add multike??\
//                if(b!=null) {
//                    WaypointwithDateList.add(b);
//                }
        WayPointDetailsList.clear();

        for (int i = 0; i < WaypointwithDateList.size(); i++) {
            Log.d(TAG, "Numberwaypoint" + WaypointwithDateList + "ASDASD" + i);
            Log.d(TAG, "Numberwaypoint" + WaypointwithDateList.size());

            if (WayPointDetailsList != null) {
                Log.d(TAG, "Numberwaypoint" + WayPointDetailsList.size());
            }

//Waypint -5, waypont  detals =0


            String returnedwaypointdetails = "";
//TODO: using clear everything and bedone with it way....
            //Currently solved... any other ways?
//Dataparser-> changejsonobject from downloadurl to readable linked hashmap
            //Download url  Change url to data
            DataParser dataParser = new DataParser();
            try {
                downloadUrl download = new downloadUrl();
                //need to have ifelse..?
                download.execute("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + WaypointwithDateList.get(i).get(0) +
                        "&key" +
                        "=AIzaSyC4IFgnQ2J8xpbC2DmR6fIvrS5JIQV5vkA");


            } catch (Exception e) {
                Log.d(TAG, "Server is busy, Please try agai" + e.toString());

                Toast.makeText(getActivity(), "Server is busy, Please try again!", Toast.LENGTH_SHORT).show();

            }

            Log.d(TAG, "WayPointDetailsListCHeckiiiing" + WayPointDetailsList.toString());
            DataHolderClass.getInstance2().setDistributor_id2(null);
        }
        //}


        //Use these waypoints to change all marker.

        if (mapMarkersForSelected != null) {

            //Is empty first time runt
            Log.d("onPostExecute", "BeforeFirsttimechecked" + mapMarkersForSelected);


        }
        try {
            int j = mapMarkersForSelected.size();
            for (int i = 0; i < j; i++) {
                // i=0, j=0 no run
                //i=0 j=1 run once delete first...
                //i=0 j=2

                //This remove all markers, welll should (Seems to be becausee all is different,
                Marker test = mapMarkersForSelected.get(0);
                test.remove();
                mapMarkersForSelected.remove(0);


            }
        } catch (Exception e) {
            Log.d("onPostExecute", "WeirdOperation6Exception" + e);


        }
        if (mapMarkersForSelected != null) {


            Log.d("onPostExecute", "Firsttimechecked" + mapMarkersForSelected);

        }
        List<LinkedHashMap<String, String>> o = SelectedLocationListRecyclerViewAdapter.getItem();


        if (o != null) {
            Log.d("onPostExecute", "Tried to remove marker Size" + mapMarkersForSelected.size());

            Log.d("onPostExecute", "Tried to remove marker Size What" + mapMarkersForSelected);


            for (int i = 0; i < o.size(); i++) {
                Log.d("onPostExecute", "We have entered here " + o.toString());
                Log.d("onPostExecute", "We have entered here " + o.size());

                String waypointlat = o.get(i).get("lat").toString();

                String waypointlng = o.get(i).get("lng").toString();
                LatLng waypointlatLng = new LatLng(Double.parseDouble(waypointlat), Double.parseDouble(waypointlng));
                MarkerOptions wayPointmarkerOptions = new MarkerOptions();
                wayPointmarkerOptions.position(waypointlatLng);
                wayPointmarkerOptions.title("Clicked!");
                wayPointmarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.yellowmarker, Integer.toString(i + 1)
                )));
                wayPointmarkerOptions.zIndex(10);
                Log.d("onPostExecute", "Tried to remove markeoptions" + wayPointmarkerOptions);

                mapMarkersForSelected.add(mMap.addMarker(wayPointmarkerOptions));

            }
        }
        if (mapMarkersForSelected != null) {


            Log.d("onPostExecute", "secondtimechecked" + mapMarkersForSelected);

        }

        if (!WaypointwithDateList.isEmpty()) {
            //This draws path when returning to page
            LatLng latlng = new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
            String url = getDirectionsUrl(latlng, null);
//ignore downloadtask changed to
            //    DownloadTask downloadTask = new DownloadTask();
            Log.d(TAG, "THE URL is IS 1st" + url);

            // Start downloading json data from Google Directions API
            // downloadTask.execute(url);
            downloadUrl download = new downloadUrl();
            download.execute(url);

        }

        pager = (ViewPager) view.findViewById(R.id.vpPagerpoilist);
        //TODO: whatrever is using this needto be changed
        //TODO:has changed to child, use support if problem persist?
        recycleadapter = new MyPagerAdapter(getChildFragmentManager());
        try {
            pager.setAdapter(recycleadapter);
        } catch (Exception e) {
            //TODO:java.lang.NullPointerException: Attempt to invoke virtual method 'android.os.Handler android.support.v4.app.FragmentHostCallback
            // .getHandler()' on a null object reference
            Toast.makeText(getActivity(), "Uh oh! Some error has occured  ", Toast.LENGTH_SHORT).show();


        }

        pd.setMessage("Loading Path...");
        pd.setCancelable(false);
        //Number picker design needs to be changed
        numberpicker = (HorizontalNumberPicker) view.findViewById(R.id.radiusPicker);
        numberpicker.setMaxValue(10);
        numberpicker.setMinValue(1);
        numberpicker.setValue(5);


        //TODO:Not sure fixed or not
        //    LinearLayout linearLayout = (LinearLayout)  view.findViewById(R.id.linearLayout);
        //    final ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();


        RangeBar pricerangebar = (RangeBar) view.findViewById(R.id.pricerangebar);

        pricerangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                minpriceint = leftPinIndex;
                maxpriceint = rightPinIndex;
                Log.d("checkrange", leftPinValue + rightPinValue);

            }
        });


        //   setContentView(R.layout.activity_maps);
        markerPoints = new ArrayList<LatLng>();


        //    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mHandler = new Handler();

        //     AutoCompleteTextView from = (AutoCompleteTextView) view.findViewById(R.id.from);
        //    AutoCompleteTextView to = (AutoCompleteTextView) view.findViewById(R.id.to);

        // AutoCompleteTextView from = (AutoCompleteTextView) v.findViewById(R.id.from);
        //   AutoCompleteTextView to = (AutoCompleteTextView) v.findViewById(R.id.to);
        Button but = (Button) view.findViewById(R.id.load_directions);

        mDurationView = (TextView) view.findViewById(R.id.duration_label);
        mDistanceView = (TextView) view.findViewById(R.id.distance_label);
        Button SelectTypes = (Button) view.findViewById(R.id.selecttypes);


        Button opencategories = (Button) view.findViewById(R.id.OpenCategoriesView);
        Button openFoodLayout = (Button) view.findViewById(R.id.FoodButton);
        Button openEntertainmentLayout = (Button) view.findViewById(R.id.EntertainmentButton);
        Button openShopLayout = (Button) view.findViewById(R.id.ShopButton);
        Button categories_expandable_layout = (Button) view.findViewById(R.id.opensetting);
        ConstraintLayout Selectedview = (ConstraintLayout) inflater.inflate(R.layout.fragment_location__recycler_view2, container, false);

        Button savefirebase = (Button) Selectedview.findViewById(R.id.save);
        Button loadfirebase = (Button) Selectedview.findViewById(R.id.load);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        savefirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Location Saved!  ", Toast.LENGTH_SHORT).show();


                BaseActivity.mDatabase.child("users").child(LoginActivity.getUserID()).setValue(WaypointwithDateList);

            }
        });

        loadfirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Location Loaded!  ", Toast.LENGTH_SHORT).show();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FirebaseDatabaseUser post = dataSnapshot.getValue(FirebaseDatabaseUser.class);

                        System.out.println("THIS IS IT" + post);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }


                });
            }
        });
        opencategories.setOnClickListener(onClickListener);
        openFoodLayout.setOnClickListener(onClickListener);
        openEntertainmentLayout.setOnClickListener(onClickListener);
        openShopLayout.setOnClickListener(onClickListener);
        categories_expandable_layout.setOnClickListener(onClickListener);
        //  ex.setOnClickListener(onClickListener);

        expandablecategories = (ExpandableLayout) view.findViewById(R.id.categories_expandable_layout);

        FoodLayout = (ExpandableLayout) view.findViewById(R.id.FoodExpandable);

        EntertainmentLayout = (ExpandableLayout) view.findViewById(R.id.EntertainmentExpandable);

        ShopLayout = (ExpandableLayout) view.findViewById(R.id.ShopExpandable);


        final CheckBox barCheckBox = (CheckBox) view.findViewById(R.id.barCheckBox);
        final CheckBox restaurantCheckBox = (CheckBox) view.findViewById(R.id.restaurantCheckBox);
        final CheckBox amusement_parkCheckBox = (CheckBox) view.findViewById(R.id.amusement_parkCheckBox);

        final CheckBox libraryCheckBox = (CheckBox) view.findViewById(R.id.libraryCheckBox);
        final CheckBox shopping_mallCheckBox = (CheckBox) view.findViewById(R.id.shopping_mallCheckBox);


        final CheckBox aquarium_CheckBox = (CheckBox) view.findViewById(R.id.aquariumCheckBox);
        final CheckBox book_Store_CheckBox = (CheckBox) view.findViewById(R.id.bookstoreCheckBox);

        final CheckBox movie_theater_CheckBox = (CheckBox) view.findViewById(R.id.movietheaterCheckBox);
        //  final CheckBox shopping_mallCheckBox=(CheckBox)        view.findViewById(R.id.shopping_mallCheckBox);


//TODO:For dialogbox
//        SelectTypes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                android.app.FragmentManager manager = getActivity().getFragmentManager();
//                android.app.Fragment frag = manager.findFragmentByTag("fragment_edit_name");
//                if (frag != null) {
//                    manager.beginTransaction().remove(frag).commit();
//                }
//                Log.d("viewid",Integer.toString(view.getId()));
//                Toast.makeText(getActivity(), "DUUUHH!"+Integer.toString(view.getId()), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(), "gah!"+Integer.toString(R.id.selecttypes), Toast.LENGTH_SHORT).show();
//
//
//                switch (v.getId()) {
////                    case R.id.selecttypes:
////                        DialogCategoriesFragment editNameDialog = new DialogCategoriesFragment();
////                        editNameDialog.show(manager, "fragment_edit_name");
////                        break;
//                    case R.id.selecttypes:
//                        AlertDialogCategories alertDialogFragment = new AlertDialogCategories();
//                        alertDialogFragment.show(manager, "fragment_edit_name");
//                        break;
//                }
//
//
//
//            }
//        });

        SelectTypes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LocationType = new StringBuilder();
                if (!DetectConnection.checkInternetConnection(getActivity())) {
                    Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();
                } else

                {


                    newcolor = false;
                    mMap.clear();
                    if (barCheckBox.isChecked()) {

                        LocationType.append("bar|");

                    }
                    if (restaurantCheckBox.isChecked()) {

                        LocationType.append("restaurant|");

                    }

                    if (amusement_parkCheckBox.isChecked()) {

                        LocationType.append("amusement_park|");

                    }
                    if (shopping_mallCheckBox.isChecked()) {

                        LocationType.append("shopping_mall|");

                    }
                    if (aquarium_CheckBox.isChecked()) {

                        LocationType.append("aquarium|");

                    }
                    if (movie_theater_CheckBox.isChecked()) {

                        LocationType.append("movie_theater|");

                    }
                    if (libraryCheckBox.isChecked()) {

                        LocationType.append("library|");

                    }
                    if (book_Store_CheckBox.isChecked()) {

                        LocationType.append("library|");

                    }

                    try {
                        if (LocationType.toString() != "") {
                            expandableLayout.collapse();
                            pd.show();
                            setRecyclerView(LocationType);

                        } else {
                            Toast.makeText(getActivity(), "Please Select Types", Toast.LENGTH_SHORT).show();


                        }

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, "Runned hmmm ");


            }
        });

//        SearchBar.setOnClickListener(onClickListener);
//        SearchBank.setOnClickListener(onClickListener);
//        SearchAmusement.setOnClickListener(onClickListener);
//        SearchCafe.setOnClickListener(onClickListener);
//        SearchCasino.setOnClickListener(onClickListener);
//        SearchNightCLub.setOnClickListener(onClickListener);

        //Testing Purpose, delete later
        // List<String> data=new List<String>()      {"abc"};
        //  from.setText("Google is your friend.", TextView.BufferType.EDITABLE);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());


        //    nearbyPlacesList=  new ArrayList<LinkedHashMap<String, String>>();

        //     Locationadapter = new LocationListRecyclerViewAdapter(getActivity(), nearbyPlacesList);

        //   recyclerView.setAdapter(Locationadapter);


        TextView emptyText = (TextView) view.findViewById(android.R.id.empty);
        //   recyclerView.setEmptyView(emptyText);
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // Log.d("checkrange","qqqqqq"+emptyText.getText());


        //TODO:uh what is this.
        //   Vector<View> pages = new Vector<View>();
        // pages.add(recyclerView);

        //     pages.add(recyclerView);


//Todo: Currently doing this.... error try filtering itemss first
//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
//                view.setSelected(true);
//
//
//                Object itemobj =(recyclerView.getItemAtPosition(position).toString());
//                Toast.makeText(getApplicationContext(), itemobj.toString(), Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "abcdef checking for onoitem");
//
//              //  Toast.makeText(getApplicationContext(), lat, Toast.LENGTH_SHORT).show();
//
//            }
//        });

//        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
////TODO:Figure out how to reset others items
//
////                recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
////
////                    @Override
////                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int
//// oldBottom) {
////                        recyclerView.removeOnLayoutChangeListener(this);
////                        Log.e(TAG, "updated");
////                    }
////                });
////                Locationadapter.notifyDataSetChanged();
//
////TODO: Delete others's buttons
////                for (int i = recyclerView.getCount() - 1; i >= 0; i--) {
////
////                    Button b=(Button)    view.findViewById(R.id.details_btn);
////
////                    b.setVisibility(View.GONE);
////
////
////                }
////               Button d=(Button) parent.findViewById(R.id.details_btn);
////
////                d.setVisibility(View.GONE);
//                HashMap<String, Object> obj = (HashMap<String, Object>) recyclerView.getItemAtPosition(position);
//                String lat = (String) obj.get("lat");
//                String lng = (String) obj.get("lng");
//                //TODO: the "Selected" refresh when finish loading / Check if Focused on item how to do "somthing"
//
//
//
//                view.setSelected(true);
//
//
//
//
//                Button b = (Button) view.findViewById(R.id.details_btn);
//                b.setVisibility(View.VISIBLE);
//
//
//
//
//
//
//                Log.d("Yourtag", lat);
//                //  Toast.makeText(getApplicationContext(), lat.toString(), Toast.LENGTH_SHORT).show();
//                LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
//                Marker result = null;
//                //For each marker in the map...where the lat equals to the lat provided by the listview
//                for (Marker c : mapMarkers) {
//                    Log.d("Well", String.valueOf(c.getPosition().latitude));
//
//                    if (lat.equals(String.valueOf(c.getPosition().latitude)) && lng.equals(String.valueOf(c.getPosition().longitude))) {
//                        Log.d("Its done ", lat);
//                        showPath(c);
//                        result = c;
//                        //This should not be here... showpath is already up there.
//                        //       mMap.moveCamera(CameraUpdateFactory.newLatLng(latxlng));
//                        //      mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
//
//                        break;
//                    }
//                }

        //     }
        //     });


        //from.setAdapter(new LocationAutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line));
        //    to.setAdapter(new LocationAutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line));
        getChildFragmentManager();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(map);

        mapFragment.getMapAsync(this);
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing
        // API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
        }


        //   mMapFormView = view.findViewById(R.id.list_form);
        mProgressView = view.findViewById(R.id.maps_progress);

        Log.d(TAG, "Visibibility checking if progess is showing" + mProgressView.getVisibility());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Successfully Logged in");
        pd = new ProgressDialog(getActivity());
        super.onCreate(savedInstanceState);


    }
//TODO: THISIOEN
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        setIntent(intent);
//        //Just show path directly?
//        //Get placeid and sace
//        String place_id = intent.getStringExtra("place_id");
//        if (place_id != null) {
//
//            WaypointwithDateList.add(place_id);
//            Toast.makeText(getApplicationContext(), WaypointwithDateList.toString(), Toast.LENGTH_SHORT).show();
//            //Use these waypoints to change all marker.
//
//            LatLng latlng=new LatLng(CurrentLocation.getLatitude(),CurrentLocation.getLongitude());
//            String url = getDirectionsUrl(latlng, null);
//
//            DownloadTask downloadTask = new DownloadTask();
//            Log.d(TAG, "THE URL is IS " + url);
//
//            // Start downloading json data from Google Directions API
//            downloadTask.execute(url);
//
//
//        }
//
//    }
//STORE
//jewelry_store
//liquor_store
    //pharmacy
    //            electronics_store
//    shoe_store
//            convenience_store
//            clothing_store
//                    store

//                    department_store

    //SERVICES
//beauty_salon
////    doctor

    //FOOD
    ////    cafe
    //bakery


//ENTERTAIN
    //    spa
//            gym

//bowling_alley
//        campground

    //TRAVEL
//    car_rental
    //            parking
//    subway_station
//            bus_station
//    transit_station
//            train_station
//    taxi_stand

    //RELIGIONS
//            church
//    hindu_temple
//            mosque
//


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!DetectConnection.checkInternetConnection(getActivity())) {
                Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();
            } else

            {
                switch (v.getId()) {
                    case R.id.FoodButton:
                        FoodLayout.toggle();
                        //LocationType = "bar";
                        break;
                    case R.id.EntertainmentButton:
                        EntertainmentLayout.toggle();
                        break;
                    case R.id.ShopButton:
                        ShopLayout.toggle();
                        break;
                    case R.id.OpenCategoriesView:
                        expandablecategories.toggle();

                        break;

                    case R.id.opensetting:

                        expandableLayout.toggle();
                        break;

                }

            }
        }

    };


    //todo: check whether to move "List" to another classes (wtf i dont think locationtype is used.
    private void setRecyclerView(StringBuilder locationType) throws IllegalAccessException, java.lang.InstantiationException {
        try {

            StringBuilder sbValue = new StringBuilder(nearbyListBuilder());


            if (sbValue.toString() != null) {

                //  PlacesTask placesTask = new PlacesTask();
                //    placesTask.execute(sbValue.toString());
                downloadUrl download = new downloadUrl();
                download.execute(sbValue.toString());
                recycleadapter.notifyDataSetChanged();

            } else {
// Temporary solution, make sure it does not (to refesth the page) Note:(Probably fixed but not sure)

                Toast.makeText(getActivity(), "Some Error has occured, please try again later 1!", Toast.LENGTH_LONG).show();
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = MapsActivity.class;

                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();


            }
        } catch (Exception e) {

            Toast.makeText(getActivity(), "Exception:" + e.toString(), Toast.LENGTH_LONG).show();
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = MapsActivity.class;

            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();


        }
    }

    //Uses for text sizes in Marker
    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    private static Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(mGetResources, drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);
        bm = Bitmap.createScaledBitmap(bm, 100, 100, false);
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        if (drawableId == R.drawable.yellowmarker) {

            paint.setColor(Color.BLACK);

        } else {
            paint.setColor(Color.WHITE);


        }
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(mInstance, 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(mInstance, 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2));
// - ((paint.descent() + paint.ascent()) / 2)
        canvas.drawText(text, xPos, yPos, paint);

        return bm;
    }

    public static ArrayList<Marker> getMapMarkers() {
        if (mapMarkers != null) {
            Log.d("onPostExecute", "markeristhisnow2");

            return mapMarkers;
        } else {

            Log.d("onPostExecute", "markeristhisnow1");

            return null;
        }
    }

    public static List<LinkedHashMap<String, String>> getNearbyPlacesList() {


        return nearbyPlacesList;
    }

    public static ArrayList<LinkedHashMap<String, String>> getWayPointDetailsList() {


        return WayPointDetailsList;
    }

    public static ArrayList getWaypointwithDateList() {


        return WaypointwithDateList;
    }

    public static void addWaypointwithDateList(ArrayList wayoint) {

        WaypointwithDateList.add(wayoint);
    }

    //TODO: need to make it into... array list?
    public static void addWaypoint(String wayoint) {
       // WaypointwithDateList.add(wayoint);
    }

    public static void removeWaypoint(String wayoint) {
        WaypointwithDateList.remove(wayoint);
    }

    public static void setNearbyPlacesList(List<LinkedHashMap<String, String>> nearbyPlacesListFrom) {

        nearbyPlacesList = nearbyPlacesListFrom;

    }

    //TODO:Currently Doing this, try to do a if else for 1 marker (select list)? or multiple marker
    private static void ShowNearbyPlaces(List<LinkedHashMap<String, String>> nearbyPlacesList) {
        Log.d("onPostExecute", "whatisnearby" + nearbyPlacesList);


        //To let others get it
        setNearbyPlacesList(nearbyPlacesList);
        Log.d("onPostExecute", "whatisnearby" + getNearbyPlacesList());

        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        mapMarkers = new ArrayList<Marker>();
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();


            LinkedHashMap<String, String> googlePlace = nearbyPlacesList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lng = Double.parseDouble(googlePlace.get("lng"));
            double lat = Double.parseDouble(googlePlace.get("lat"));


            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            //Run the provate function above to get special marker

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.marker, Integer.toString(i + 1))));
            markerOptions.zIndex(1);

            // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            //     markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bmp));
            //   Log.d("onPostExecute", "ooooooo" + bmp.toString());


            mapMarkers.add(mMap.addMarker(markerOptions));

            Log.d("onPostExecute", "mapmarkersnumber" + mapMarkers.toString());

        }

        for (Marker marker : mapMarkers) {
            builder.include(marker.getPosition());
        }
        builder.include(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLatitude()));
        LatLngBounds bounds = builder.build();

        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//TODO:For some reason this cause eveything to go ... somewhere
        //      mMap.animateCamera(cu);

        Log.d("onPostExecute", "markeristhisoutside" + getMapMarkers().toString());


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("onMarkerCLick", "ISTHISNOTRUN");

        mMap = googleMap;
        //Well need it to let mMap.setmylocationenabled
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }
        //not useful?

        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);
        //Top right symbol
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.getUiSettings().setMapToolbarEnabled(true);
        //WTF IS THIS marker problem...?
//TODO: must show this or just use intent to show it
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                DetectConnection check = new DetectConnection();
                if (!DetectConnection.checkInternetConnection(getActivity())) {
                    Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();
                    return false;
                } else {

                    recyclerView = (RecyclerView) view.findViewById(R.id.poilistRecyclerView);

                    //When clicked,have got marker lat and lng....how to compare
                    Log.d("onMarkerCLick", marker.toString());
                    ArrayList<View> children = new ArrayList<View>();
//TODO:   java.lang.NullPointerException: Attempt to invoke virtual method 'android.support.v7.widget.RecyclerView$Adapter android.support
// .v7.widget.RecyclerView.getAdapter()' on a null object reference

                    for (int i = recyclerView.getAdapter().getItemCount() - 1; i >= 0; i--) {
                        //     HashMap<String, Object> obj = (HashMap<String, Object>);
                        List<LinkedHashMap<String, String>> o = LocationListRecyclerViewAdapter.getItem();

                        String lat = (String) o.get(i).get("lat").toString();

                        String lng = (String) o.get(i).get("lng").toString();

                        // recyclerView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        //TODO : Highlight item when marker is clicked.
                        if (lat.equals(String.valueOf(marker.getPosition().latitude)) && lng.equals(String.valueOf(marker.getPosition().longitude))) {
                        //if same , then below will moves recyclerview

                            RecyclerView.ViewHolder holder2 = LocationListRecyclerViewAdapter.getHolder2();

                            //   recyclerView.getAdapter().setItemChecked(i, true);
                            //  recyclerView.setSelection(i);
                            Log.d("onBitch", holder2.itemView.toString());
                            //   recyclerView.setSelected(true);
                            holder2.itemView.setSelected(true);

                            recyclerView.smoothScrollToPosition(i);
                            break;
                        }
                        children.add(recyclerView.getChildAt(i));
                    }
                    showPath(marker);
                    //TODO: check whether all markers have this properties....
                    return false;
                }
            }
        });

    }

    private static Marker placeholdermarker;
    private static Marker placeholdermarker2;

    //TODO:How to use : different colour marker/path, retain 2 different path.
    //Done when item in listview is clicked
    //Also when marker is clicked (add a merker and show path)
    public static void showPath(Marker marker) {
//marker is a oldmarker
//When cliekd.show ptah and new marker, then delete if has ecisintg marker
        Log.d(TAG, "Checkinggggggggggggmarker" + marker);
        Log.d(TAG, "Checkinggggggggggggmapmarkwrs" + mapMarkers);


        marker.showInfoWindow();
        //this will only remove the single line ... i guess
        if (TempPoly != null) {
            TempPoly.remove();
        }

        markerPoints.clear();
        Location location = CurrentLocation;
        LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        markerPoints.add(currentLatlng);

        LatLng ClickedMarker = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);


        markerPoints.add(ClickedMarker);

        // Creating MarkerOptions


        LatLng origin = markerPoints.get(0);
        LatLng dest = markerPoints.get(1);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLngBounds bounds;
        for (Marker c : mapMarkers) {
            //builder.include(c.getPosition());

            if (dest.latitude == c.getPosition().latitude && dest.longitude == c.getPosition().longitude) {

                builder.include(marker.getPosition());


                //break causes for to not run completely, need to change that to show all waypotinst/markers
                //    break;
            }
        }

        builder.include(currentLatlng);
        bounds = builder.build();


        int padding = 150; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.animateCamera(cu);


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(dest);
        markerOptions.zIndex(8);
        placeholdermarker2 = mMap.addMarker(markerOptions);


        //Default
        Log.d(TAG, "CheckinggggggggggggmarkerOptions 1" + markerOptions);

        if (placeholdermarker != null) {
            Log.d(TAG, "Checkingggggggggggg" + placeholdermarker.toString());
            placeholdermarker.remove();

            //TODO: Check how to remove the placeholdermarker....
//Wait,,,,,,,, this replace the original?????
        }
        placeholdermarker = placeholdermarker2;


        //move map camera
        //   mMap.moveCamera(CameraUpdateFactory.newLatLng(dest));
        // mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        //Doing this, should... easiest way is to get a variable here to change color...
        //  DownloadTask downloadTask = new DownloadTask();
        Log.d(TAG, "THE URL is  IS ...2nd " + url);
        newcolor = true;
        // Start downloading json data from Google Directions API
        downloadUrl download = new downloadUrl();
        download.execute(url);
//TODO: got direction then?
    }
    //Replaced with progress dialog ....**********
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//            Log.d(TAG, "Progress bar ");
//
//            mMapFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
//            mMapFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mMapFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
//                    Log.d(TAG, "Progress 2");
//
//                }
//            });
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mMapFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
//        }
//    }

    //Pressed twice to quit
//    boolean doubleBackToExitPressedOnce = false;
//    @Override
//    public void onBackPressed() {
//
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//
//    }


    private static String getDirectionsUrl(LatLng origin, LatLng dest) {
        //For getting an url for paths (including click 1 or multiple waypoints


        // Origin of route
        //No changes here
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "";
        if (dest != null) {
            //get desnt latlng for 1 location only (For final location when clicking waypoint/item)
            Log.d(TAG, "whathasbeendone1");
            str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        }

        //TODO: fixed this pile of messy code.. Logic might become easier.
        String waypoint = new String();
        StringBuilder waypointsb = new StringBuilder();
        if (!WaypointwithDateList.isEmpty()) {
            //if WaypointwithDateList is not empty  (which is when nothing is selected?),then add to waypoint
            Log.d(TAG, "whathasbeendone2"+WaypointwithDateList);

            waypointsb.append("&waypoints=");
            waypointsb.append("optimize:true|");

            if (dest != null) {
                Log.d(TAG, "whathasbeendone3");

                for (int i = 0; i < WaypointwithDateList.size(); i++) {
                    //TODO:Optimize =true should not be hard coded, require to change according to users choice,
                    //optimize:true|
                    waypoint = (waypointsb.append("place_id:" + WaypointwithDateList.get(i).get(0) + "|").toString());
                    Log.d(TAG, "whathasbeendone4");

                }


            } else {
                //to let there be a destination.,
                Log.d(TAG, "whathasbeendone5"+WaypointwithDateList);
                Log.d(TAG, "whathasbeendone5"+WaypointwithDateList.get(0));
                Log.d(TAG, "whathasbeendone5"+WaypointwithDateList.get(WaypointwithDateList.size() - 1).get(0).toString());




                str_dest = "destination=place_id:" + WaypointwithDateList.get(WaypointwithDateList.size() - 1).get(0);
                //When waypoint has more than  1 varible?
                if (WaypointwithDateList.size() != 1) {
                    for (int i = 0; i < WaypointwithDateList.size() - 1; i++) {

                        waypoint = (waypointsb.append("place_id:" + WaypointwithDateList.get(i).get(0) + "|").toString());

                    }
                }
            }
//To remove "|"
            if (!waypoint.isEmpty()) {

                waypoint = waypoint.substring(0, waypoint.length() - 1);
            }
        } else {
            Log.d(TAG, "whathasbeendone6");

            waypoint = "";
        }
        // Sensor enabled
        String sensor = "sensor=false";
        String parameters;

        Log.d(TAG, "whathasbeendone7");

        parameters = str_origin + "&" + str_dest + waypoint + "&" + sensor + "&key=AIzaSyAQjMlUIxrybWWkko4AVHmE_Evr1I625cs";

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.d(TAG, "whathasbeendone8" + url);

        return url;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    //Path Searching
    //Download URL ?
    //change to public for access from placedetail activity
//    public static class DownloadTask extends AsyncTask<String, Void, String> {
//        protected void onPreExecute() {
//            //set message of the dialog
//            //show dialog
//
//            pd.show();
//            super.onPreExecute();
//        }
//
//        // Downloading data in non-ui thread
//        @Override
//        protected String doInBackground(String... url) {
//            Log.d(TAG, url[0]);
//            Log.d(TAG, "Do in Back Test");
//
//            // For storing data from web service
//            String data = "";
//
//            try {
//                // Fetching the data from web service
//                data = downloadUrl(url[0]);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//
//
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
////Click Marker and find path? Probably this is
//            Log.d("PTRESULT", result);
//
//            ParserTask parserTask = new ParserTask();
//            // Invokes the thread for parsing the JSON data
//            parserTask.execute(result);
//            Log.d("PTRESULTAFter", result);
//            if (pd.isShowing()) {
//
//                Log.d("PTRESULTAFte3nd", result);
//
//
//            }
//            pd.dismiss();
//            Log.d("PTRESULTAFte2nd", result);
//
//        }
//
//
//    }

    //TODO: Use this in all ?
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            pd.dismiss();
        }
    };
    /**
     * A class to parse the Google Places in JSON format
     */
//    private class ParserTask extends AsyncTask<String, Integer, List<LinkedHashMap<String,String>> >{
//        //List<LinkedHashMap<String, String>> nearbyPlacesList = null;
//
//        // Parsing the data in non-ui thread
//        @Override
//        protected List<LinkedHashMap<String, String>> doInBackground(String... jsonData) {
//            Log.d("TAG", "Parser Task is runnign");
//
//            JSONObject jObject;
//            List<LinkedHashMap<String, String>> routes = null;
//            Place_JSON p=new Place_JSON();
//
//            try{
//                jObject = new JSONObject(jsonData[0]);
//                DirectionsJSONParser parser = new DirectionsJSONParser();
//
//                // Starts parsing data
//                Log.d("TAG", jObject.toString());
//
//                routes = parser.parse(jObject);
//                Log.d("In maps    jobject", routes.toString());
//
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//
//            Log.d("TAG", "lats one"+routes.toString());
//
//
//
//nearbyPlacesList=routes;
//
//
//
//
//
//            return routes;
//        }
//
//        // Executes in UI thread, after the parsing process
////        @Override
////        protected void onPostExecute(List<List<LinkedHashMap<String, String>>> result) {
////            ArrayList<LatLng> points = null;
////            PolylineOptions lineOptions = null;
////            MarkerOptions markerOptions = new MarkerOptions();
////
////            // Traversing through all the routes
////            for(int i=0;i<result.size();i++){
////                points = new ArrayList<LatLng>();
////                lineOptions = new PolylineOptions();
////
////                // Fetching i-th route
////                List<LinkedHashMap<String, String>> path = result.get(i);
////
////                // Fetching all the points in i-th route
////                for(int j=0;j<path.size();j++){
////                    LinkedHashMap<String,String> point = path.get(j);
////
////                    double lat = Double.parseDouble(point.get("lat"));
////                    double lng = Double.parseDouble(point.get("lng"));
////                    LatLng position = new LatLng(lat, lng);
////
////                    points.add(position);
////                }
////
////                // Adding all the points in the route to LineOptions
////                line  Options.addAll(points);
////                lineOptions.width(2);
////                lineOptions.color(Color.RED);
////            }
////
////            // Drawing polyline in the Google Map for the i-th route
////            mMap.addPolyline(lineOptions);
////        }
//    }
    private LocationManager locationManager;
    private String provider;
    private int testing;

    //Click Marker and find path?
    private static class ParserTask extends AsyncTask<String, Integer, List<List<LinkedHashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<LinkedHashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<LinkedHashMap<String, String>>> routes = null;

            try {

                jObject = new JSONObject(jsonData[0]);


                DirectionsFetcher parser = new DirectionsFetcher();

                // Starts parsing data
                routes = parser.parse(jObject);


            } catch (Exception e) {
                Log.d("ParserTaskException", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<LinkedHashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            String distance = "";
            String duration = "";
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<LinkedHashMap<String, String>> path = result.get(i);


                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if (point.containsKey("distance")) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (point.containsKey("duration")) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }
                    //     Log.d("testhahanumber=", j + point.toString());

                    //  Log.d("wtfishahadistance", (String) point.get("distance"));
                    //    Log.d("wtfishahaduration", duration);


                    //getnullfrom point? WTF where does 7 came from
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                //TODO: Create a global variable For result
                //TODO:DOing this , try to have dfifferent colours?
                //Must do this first or else way too confusing when checking......
                if (newcolor) {
                    lineOptions.color(Color.BLUE);

                } else {

                    lineOptions.color(Color.RED);

                }

                //To show distance (need to change to total distance
                mDistanceView.setText("Distance= " + distance);
                mDurationView.setText("Duration= " + duration);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded" + lineOptions);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                TempPoly = mMap.addPolyline(lineOptions);
                Log.d("onPostExecute", lineOptions.toString());


            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    //Originally private, change it to public so LocationDetailsActivity can access it
//Any url can be insert here and will return different kind of data
    private static class downloadUrl extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strUrl) {
            Log.d(TAG, "checkifone");

            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            Log.d(TAG, "Download URL" + strUrl);
            Log.d(TAG, "checkiftwo");

            try {
                int retry = 0;
                URL url = new URL(strUrl[0]);
                Log.d(TAG, "Download URL testing" + strUrl[0]);
                Log.d(TAG, "Download URL testing" + url.toString());

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();
                while (retry == 0) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                    StringBuffer sb = new StringBuffer();

                    String line = "";
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    Log.d(TAG, "Direction Data for wtf is br" + sb);

                    data = sb.toString();
                    Log.d(TAG, "Direction Data for URL Downloa" + strUrl[0]);

                    Log.d(TAG, "Direction Data for URL Download" + data);

                    br.close();
                    if (data.contains("You have exceeded your daily request quota for this API")) {
                        Thread.sleep(2000);
                        Log.d(TAG, "howmanytimesis thread");

                    } else {

                        retry = 1;

                    }
                }
                return data;

            } catch (Exception e) {
                Log.d("Exception url", e.toString());
            } finally {
                if (iStream != null) {
                    try {
                        iStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    urlConnection.disconnect();
                } catch (Exception e) {
                    //NetworkOnMainThreadException
                    Log.d("Exception downLoadUrl", e.toString());

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            Log.d(TAG, "On post execute 1st");
//TODO:This is where different kinds of things should be separated: NearbySearch, Directions, and details....
            //Just check whatrever result return and do if else
            List<LinkedHashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();

//Review is using separate one?
//TODO: {   "html_attributions" : [      for Start searching
//TODO"{   "geocoded_waypoints" :     check waypoints
            //This is where its connected to DataParser and get the List of places wiyh ratings etc.

            if (result.startsWith("{   \"html_attributions\" : [")) {
//THIS iS DETAILS< need to put details in list


                if (result.contains("next_page_token")) {
                    nearbyPlacesList = dataParser.parse(result);
                    ShowNearbyPlaces(nearbyPlacesList);
                } else {
                    //CHeck ths


                    //     WayPointDetailsList.add(dataParser.parse(result).get(0));
                    Log.d("Yeep", "dataparserresult1Details==" + dataParser.parse(result));
                    WayPointDetailsList.add(dataParser.parse(result).get(0));

                }


            } else if (result.startsWith("{   \"geocoded_waypoints\" ")) {
                //Wai... this is for direction...!??
                // WayPointDetailsList.add(dataParser.parse(result));
                Log.d("Yeep", "dataparserresult1==" + dataParser.parse(result));
//DirectionsFetcher a = new DirectionsFetcher();
//a.parse(result);
                parserTask.execute(result);

            }
            Log.d("Yeep", "dataparserresult1==" + result);

            Log.d("Yeep", "dataparserresult2=======" + nearbyPlacesList);

//
//            Log.d("GooglePlacesReadTask", nearbyPlacesList.toString());
//            ListAdapter adapter = new SimpleAdapter(
//
//                    MapsActivity.this, nearbyPlacesList,
//
//                    R.layout.list_item, new String[]{"place_name"}, new int[]{R.id.name}
//
//            );

            //This use locationadapter to put in listview?


//Search list, user choose,
            pager.invalidate();
            pd.dismiss();
            recycleadapter.notifyDataSetChanged();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            // parserTask.execute(result);

            Log.d(TAG, "On post execute ENDNENDNEDNNEND");

        }

        //  return data;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


        Log.d(TAG, "Connection fail" + connectionResult);


    }


    private void loadHomeFragment() {
        // selecting appropriate nav menu item

        // set toolbar title
        getChildFragmentManager();
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getActivity().getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            //  drawer.closeDrawers();

            // show or hide the fab button
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                //  fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        // show or hide the fab button

        //Closing drawer on item click
        //  drawer.closeDrawers();

        // refresh toolbar menu
        //TODO:Check what is this
        //invalidateOptionsMenu();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;


    public StringBuilder nearbyListBuilder() {
        //TODO:probably fixed... location null
        Location location = CurrentLocation;

        try {
            if (location != null) {
                //use your current location here
                double mLatitude = location.getLatitude();
                double mLongitude = location.getLongitude();

                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + mLatitude + "," + mLongitude);
                //TODO: Set radius to be controllable

//Yep not using now.


                int radius = numberpicker.getValue();
                radius = radius * 1000;
                Log.d("CheckBit", numberpicker.toString());
                Log.d("CheckBit", "1111111111111111");
                LocationType.deleteCharAt(LocationType.length() - 1);
                Log.d("CheckBit", "222gagag  2");

                // sb.append("&radius=5000");
                //Todo: figure out how to support multiple types (location)..........
                sb.append("&radius=" + Integer.toString(radius));

                sb.append("&types=" + LocationType);

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
            Log.d("CheckBit", e.toString());

            return sb.append("Test");
        }

    }

    //Loading Locations (URL is the location with things
    private class PlacesTask extends AsyncTask<String, Integer, String> {
        protected void onPreExecute() {
            //set message of the dialog
            //show dialog
            pd.setMessage("Loading Locations...");

            pd.show();
            super.onPreExecute();
        }

        String data = null;

        // Invoked by execute() method of this object
        //TODO:Doing this (if else one place.(solve by other wayse
        @Override
        protected String doInBackground(String... url) {
//
            try {
                Log.d(TAG, "Runnign? PlacesTaskCheckurl" + url[0]);

                Log.d(TAG, "Runnign? PlacesTask" + data);

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }


        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "Inmapsactivity");


        }
    }


    // Executed after the complete execution of doInBackground() method
    public static ConstraintLayout view;
    private FragmentActivity fa;


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.d(TAG, "Dujjjjjjjcalled super");

        }

        @Override
        public Fragment getItem(int pos) {
            Log.d(TAG, "Dujjjjjjjcalled 2nd time....1");
//TODO:well technically should just send the places trough here.
            switch (pos) {

                case 0:
                    return Location_RecyclerView.newInstance("FirstFragment", "Ok yupe");
                case 1:
                    return Location_RecyclerView_Selected.newInstance("SecondFragment", " Instance 1");
                //   case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
                //       case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
                //     case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default:
                    return Location_RecyclerView.newInstance("FirstFragmen", "Ok yupe");


            }
        }


        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = "Available Location";
            } else if (position == 1) {
                title = "Selected Location";
            }

            return title;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object) {

            Log.d(TAG, "DujjjjjjjRefresh....1");

            Log.d(TAG, "DujjjjjjjRefresh....2");

            return POSITION_NONE;
        }


    }


}




