package com.example.user.travelguideapps.MapsPage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.DataParser;
import com.example.user.travelguideapps.DetectConnection;
import com.example.user.travelguideapps.DirectionsFetcher;
import com.example.user.travelguideapps.FirebaseDatabaseUser;
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

import net.cachapa.expandablelayout.ExpandableLayout;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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
    private static RecyclerView recyclerView;
    private static StringBuilder LocationType;
    private View mProgressView;
    private View mMapFormView;
    private View horizontal_scroll_view;
    private View fragmentpoilist;
    private static ViewPager pager;
    private static FragmentPagerAdapter fragmentPagerAdapter;

    private static RecyclerView recyclerViewSelected;

    private static LocationListRecyclerViewAdapter recycleadapter;
    private static Context context = null;

    //  private ExpandableLayout expandableLayout;
    private ExpandableLayout FoodLayout;
    private ExpandableLayout EntertainmentLayout;

    private ExpandableLayout ShopLayout;
    private ExpandableLayout expandablecategories;

    private static TextView mDurationView;
    private static TextView mDistanceView;
    private static LocationListRecyclerViewAdapter Locationadapter;
    // private static ArrayList WaypointwithDateList = new ArrayList();
    private static SelectedLocationListRecyclerViewAdapter selectedLocationadapter;

    private static ArrayList<HashMap> WaypointwithDateList = new ArrayList<HashMap>();


    private static Boolean newcolor = false;
    private static DiscreteSeekBar numberpicker;
    public static Location CurrentLocation = BaseActivity.getCurrentLocation();
    private static int minpriceint = -1;
    private static int maxpriceint = -1;

    private static Polyline TempPoly;
    private static List<LinkedHashMap<String, Object>> nearbyPlacesList;
    private static ArrayList<LinkedHashMap<String, Object>> WayPointDetailsList = new ArrayList<LinkedHashMap<String, Object>>();

    private DatabaseReference mDatabase;
    // ...
    private static FragmentActivity mInstance;
    private static Resources mGetResources;

    public static ProgressDialog pd;
    GestureDetector gestureDetector;

    int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0;
    private static GoogleMap mMap;
    LocationRequest mLocationRequest = new LocationRequest();
    static ArrayList<Marker> mapMarkers;
    static ArrayList<Marker> mapMarkersForSelected = new ArrayList<Marker>();

    private static int selectedsizeint;
    private static int waypointdatelistint;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Get waypoints from others
        getActivity().setTitle("Map");

        String b = DataHolderClass.getInstance().getDistributor_id();
        mInstance = getActivity();
        mGetResources = getResources();
        context = getActivity();

        //Check if Map is already created
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = (ConstraintLayout) inflater.inflate(R.layout.activity_maps, container, false);
            recyclerviewview = (ConstraintLayout) inflater.inflate(R.layout.fragment_location__recycler_view, container, false);

        } catch (InflateException e) {
    /* map is already there, just return view as it is */

        }


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

                        //expandableLayout.expand();

                        // activity.fetchPrevious();
                    } else {
                        // activity.fetchNext();
                        //   Log.d(TAG, "onflingnot");

                        //  expandableLayout.collapse();

                    }

                    return true;
                }

                return false;
            }
        });

//at first is zero

        //Repeat the datelist until it gets all details (have no distancetonext
        for (int i = 0; i < WaypointwithDateList.size(); i++) {
            Log.d(TAG, "Numberwaypoint" + WaypointwithDateList + "ASDASD" + i);
            Log.d(TAG, "Numberwaypointdafaq" + WaypointwithDateList.size());

            selectedsizeint = i + 1;
            waypointdatelistint = i;
//TODO: using clear everything and bedone with it way....

//Everytime we enter the mainpage::::
            try {
                downloadUrl download = new downloadUrl(getContext());
                //need to have ifelse..?
                Log.d(TAG, "WaypointwithDateListWaypointwithDateList" + WaypointwithDateList);

                HashMap h = WaypointwithDateList.get(i);
                Log.d(TAG, "WaypointwithDateListWaypointwithDateList" + h);

                download.execute("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + h.get("place_id") +
                        "&key" +
                        "=AIzaSyC4IFgnQ2J8xpbC2DmR6fIvrS5JIQV5vkA");
                Log.d("Checkruntimes", "Runningdownloadurl  2");

            } catch (Exception e) {
                Log.d(TAG, "Server is busy, Please try agai" + e.toString());


                Toast.makeText(getActivity().getApplicationContext().getApplicationContext(), "Server is busy, Please try again!", Toast
                        .LENGTH_SHORT).show();

            }


            Log.d(TAG, "WayPointDetailsListCHeckiiiing" + WayPointDetailsList.toString());
            DataHolderClass.getInstance2().setDistributor_id2(null);
        }
        //}
        //REFRESHPAGE

        setDistanceURL(getContext());
        //TODO: first time the distance will not appear
        if (pager != null || fragmentPagerAdapter != null) {
            //    pager.invalidate();
            //     fragmentPagerAdapter.notifyDataSetChanged();
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

        List<LinkedHashMap<String, Object>> o = SelectedLocationListRecyclerViewAdapter.getItem();


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
//
            }
        }
        if (!WaypointwithDateList.isEmpty()) {
            //This draws path when returning to page
            LatLng latlng = new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
            String url = getDirectionsUrl(latlng, null);

            Log.d(TAG, "THE URL is IS 1st" + url);

            // Start downloading json data from Google Directions API
            // downloadTask.execute(url);
            downloadUrl download = new downloadUrl(getContext());
            download.execute(url);
            Log.d("Checkruntimes", "Runningdownloadurl  3");


        }


        recyclerView = (RecyclerView) recyclerviewview.findViewById(R.id.poilistRecyclerView);
        recycleadapter = new LocationListRecyclerViewAdapter(getNearbyPlacesList());


        pager = (ViewPager) view.findViewById(R.id.vpPagerpoilist);
        //TODO: whatrever is using this needto be changed
        //TODO:has changed to child, use support if problem persist?
        fragmentPagerAdapter = new MyPagerAdapter(getChildFragmentManager());


        pd.setMessage("Loading...");
        pd.setCancelable(false);

        markerPoints = new ArrayList<LatLng>();

        mHandler = new Handler();

        mDurationView = (TextView) view.findViewById(R.id.duration_label);
        mDistanceView = (TextView) view.findViewById(R.id.distance_label);


        ConstraintLayout Selectedview = (ConstraintLayout) inflater.inflate(R.layout.fragment_location__recycler_view_selected, container, false);
//
        Button loadfirebase = (Button) Selectedview.findViewById(R.id.load);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        loadfirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext().getApplicationContext().getApplicationContext(), "Location Loaded!  ", Toast
                        .LENGTH_SHORT).show();

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
//TODO:probably put this back where it should be.....?
        //TODO: is there anyway to make it not reload everytime?
        WayPointDetailsList.clear();

        mProgressView = view.findViewById(R.id.maps_progress);
        setTabIcon();

        return view;
    }

    public void setTabIcon() {


        try {
            pager.setAdapter(fragmentPagerAdapter);
            recyclerView.setAdapter(recycleadapter);
            //   PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.pager_header);
            Log.d(TAG, "oncreatviewisruntwice");

            TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(pager);

            tabLayout.getTabAt(0).setIcon(R.drawable.searchicon);
            tabLayout.getTabAt(1).setIcon(R.drawable.filesimpleicon);


            //tabLayout.addTab(tabLayout.newTab().setText("Available Locations"));
            //  tabLayout.addTab(tabLayout.newTab().setText("Selected Locations"));
            // tabs.setViewPager(pager);
        } catch (Exception e) {
            //TODO:java.lang.NullPointerException: Attempt to invoke virtual method 'android.os.Handler android.support.v4.app.FragmentHostCallback
            // .getHandler()' on a null object reference
            Toast.makeText(getActivity().getApplicationContext().getApplicationContext(), "Uh oh! Some error has occured  ", Toast.LENGTH_SHORT)
                    .show();


        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Successfully Logged in");
        pd = new ProgressDialog(getActivity());
        super.onCreate(savedInstanceState);

    }

    //todo: check whether to move "List" to another classes (wtf i dont think locationtype is used.
    public static void setRecyclerView(StringBuilder sbValue) throws IllegalAccessException, java.lang.InstantiationException {
        try {
//This get URL from different places and process it (details, neraby etc
            if (sbValue.toString() != null) {
                Log.d(TAG, "isthisrunningbefore" + sbValue);

                downloadUrl download = new downloadUrl((context));
                download.execute(sbValue.toString());
                Log.d(TAG, "isthisrunning");
                Log.d("Checkruntimes", "Runningdownloadurl  4");

            } else {
// Temporary solution, make sure it does not (to refesth the page) Note:(Probably fixed but not sure)

                Toast.makeText(context, "Some Error has occured, please try again later 1!", Toast.LENGTH_LONG).show();
                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = MapsActivity.class;
                //getFragmentManager();

//                fragment = (Fragment) fragmentClass.newInstance();
//                FragmentManager fragmentManager = getFragmentManagerMaps();
//                fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();


            }


        } catch (Exception e) {

            Toast.makeText(context, "Exception:" + e.toString(), Toast.LENGTH_LONG).show();
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = MapsActivity.class;

//            fragment = (Fragment) fragmentClass.newInstance();
//            FragmentManager fragmentManager = manager();
//            fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();


        }
    }

    //Uses for text sizes in Marker
    public static int convertToPixels(Context context, int nDP) {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f);

    }

    private static Bitmap writeTextOnDrawable(int drawableId, String text) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        //TODO:^^^^just added to reducde size???
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

    public static List<LinkedHashMap<String, Object>> getNearbyPlacesList() {


        return nearbyPlacesList;
    }

    //TODO: Duh
    public static void clearNearbyPlacesList() {
        if (!nearbyPlacesList.isEmpty()) {
            nearbyPlacesList.clear();
        }
    }

    public static ArrayList<LinkedHashMap<String, Object>> getWayPointDetailsList() {


        return WayPointDetailsList;
    }

    public static void setWayPointDetailsList(ArrayList<LinkedHashMap<String, Object>> wayPointDetailsList) {
        WayPointDetailsList = wayPointDetailsList;
        Log.d("WayPointDetailsList", "WayPointDetailsListhahahahha" + WayPointDetailsList);

        //      return WayPointDetailsList;
    }

    public static LinkedHashMap<String, Object> getWayPointDetails(String id) {
        LinkedHashMap<String, Object> details = null;
        for (int i = 0; i < WayPointDetailsList.size(); i++) {

            if ((WayPointDetailsList.get(i).containsValue(id))) {

                details = WayPointDetailsList.get(i);


            }

        }
        Log.d("mapsactivityfunction", "getwaypointdetails" + details.get("opening_hours"));

        return details;
    }

    public static ArrayList<HashMap> getWaypointwithDateList() {

        Log.d("mapsactivityfunction", "getwaypointwithdate" + WaypointwithDateList);

        return WaypointwithDateList;
    }

    public static void addWaypointwithDateList(LinkedHashMap waypoint) {

        Log.d("mapsactivityfunction", "addWaypointwithDateList" + waypoint);

        WaypointwithDateList.add(waypoint);

    }

    public static void setcolor(Boolean b) {

        newcolor = b;

    }

    public static GoogleMap getMap() {

        return mMap;
    }

    public static void setWaypointwithDateList(ArrayList<HashMap> waypoint) {
        Log.d("mapsactivityfunction", "setWaypointwithDateList" + waypoint);

        WaypointwithDateList = waypoint;
    }

    public static void changeWaypointwithDateList(LinkedHashMap waypoint) {
        try {
            for (int i = 0; i < WaypointwithDateList.size(); i++) {
                Log.d("mapsactivityfunction", "changeWaypointwithDateList" + waypoint);

                if (WaypointwithDateList.get(i).get("place_id").equals(waypoint.get("place_id"))) {

                    WaypointwithDateList.set(i, waypoint);
                }

            }
        } catch (Exception e) {

            Log.d("Exception", "changeWaypointwithDateList" + e);

        }
    }

    //TODO: need to make it into... array list?
    public static void addWaypoint(String wayoint) {
        // WaypointwithDateList.add(wayoint);
    }

    public static void removeWaypoint(String waypoint) {
        for (int i = 0; i < WaypointwithDateList.size(); i++) {
            if (WaypointwithDateList.get(i).get("place_id").equals(waypoint)) {
                WaypointwithDateList.remove(i);
                Log.d("mapsactivityfunction", "setWaypointwithDateList" + waypoint);

            }
        }
    }

    public static void removeDate(String waypoint) {
        for (int i = 0; i < WaypointwithDateList.size(); i++) {
            if (WaypointwithDateList.get(i).get("place_id").equals(waypoint)) {
                WaypointwithDateList.get(i).put("starttime", 0L);
                WaypointwithDateList.get(i).put("duration", 0L);

                Log.d("mapsactivityfunction", "setWaypointwithDateList" + waypoint);

            }
        }
    }

    public static void setNearbyPlacesList(List<LinkedHashMap<String, Object>> nearbyPlacesListFrom) {

        nearbyPlacesList = nearbyPlacesListFrom;

    }

    //TODO:Currently Doing this, try to do a if else for 1 marker (select list)? or multiple marker
    private static void ShowNearbyPlaces(List<LinkedHashMap<String, Object>> nearbyPlacesList) {
        Log.d("onPostExecute", "whatisnearby" + nearbyPlacesList);


        //To let others get it
        setNearbyPlacesList(nearbyPlacesList);
        Log.d("onPostExecute", "whatisnearby" + getNearbyPlacesList());

        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        mapMarkers = new ArrayList<Marker>();
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();


            LinkedHashMap<String, Object> googlePlace = nearbyPlacesList.get(i);

            String placeName = googlePlace.get("place_name").toString();
            String vicinity = googlePlace.get("vicinity").toString();
            double lng = Double.parseDouble(googlePlace.get("lng").toString());
            double lat = Double.parseDouble(googlePlace.get("lat").toString());


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

        //  mMap.setBuildingsEnabled(true);
        //TODO:Ignored this if dont want....
        //mMap.setTrafficEnabled(true);
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
                    Toast.makeText(getActivity().getApplicationContext().getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
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
                        List<LinkedHashMap<String, Object>> o = LocationListRecyclerViewAdapter.getItem();

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
                            //well wtf this is working
                            recyclerView.smoothScrollToPosition(i);
                            break;
                        }
                        children.add(recyclerView.getChildAt(i));
                    }
                    String s = showPath(marker);
                    downloadUrl download = new downloadUrl(getContext());
                    download.execute(s);
                    Log.d("Checkruntimes", "Runningdownloadurl  5");

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
    public static String showPath(Marker marker) {
//marker is a oldmarker
//When cliekd.show ptah and new marker, then delete if has ecisintg marker
        ;

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
        return url;
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
            Log.d(TAG, "whathasbeendone2" + WaypointwithDateList);

            waypointsb.append("&waypoints=");
            waypointsb.append("optimize:true|");

            if (dest != null) {
                Log.d(TAG, "whathasbeendone3");

                for (int i = 0; i < WaypointwithDateList.size(); i++) {
                    //TODO:Optimize =true should not be hard coded, require to change according to users choice,
                    //optimize:true|
                    waypoint = (waypointsb.append("place_id:" + WaypointwithDateList.get(i).get("place_id") + "|").toString());
                    Log.d(TAG, "whathasbeendone4");

                }


            } else {
                //to let there be a destination.,
                Log.d(TAG, "whathasbeendone5" + WaypointwithDateList);
                Log.d(TAG, "whathasbeendone5" + WaypointwithDateList.get(0));
                Log.d(TAG, "whathasbeendone5" + WaypointwithDateList.get(WaypointwithDateList.size() - 1));

                HashMap lastwaypoint = WaypointwithDateList.get(WaypointwithDateList.size() - 1);

                //    Log.d(TAG, "whathasbeendone5 linkedhashmapo" + a);

//it's psy
                str_dest = "destination=place_id:" + lastwaypoint.get("place_id");
                //When waypoint has more than  1 varible?
                if (WaypointwithDateList.size() != 1) {
                    for (int i = 0; i < WaypointwithDateList.size() - 1; i++) {
                        HashMap iwaypoint = WaypointwithDateList.get(i);

                        waypoint = (waypointsb.append("place_id:" + iwaypoint.get("place_id") + "|").toString());

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
    public static class downloadUrl extends AsyncTask<String, Void, String> {

        private Context mContext;

        public downloadUrl(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... strUrl) {
            Log.d(TAG, "checkifone");

            String data = "";
            InputStream iStream = null;
            HttpsURLConnection urlConnection = null;
            Log.d(TAG, "Download URL" + strUrl);
            Log.d(TAG, "checkiftwo");

            try {
                int retry = 0;
                URL url = new URL(strUrl[0]);
                Log.d(TAG, "Download URL testing" + strUrl[0]);
                Log.d(TAG, "Download URL testing" + url.toString());

                // Creating an http connection to communicate with url
                urlConnection = (HttpsURLConnection) url.openConnection();

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
            Log.d(TAG, "On post execute 1st" + result);
//TODO:This is where different kinds of things should be separated: NearbySearch, Directions, and details....
            //Just check whatrever result return and do if else
            List<LinkedHashMap<String, Object>> nearbyPlacesList = null;
            Log.d(TAG, "runningdataparser " + " before mapsac");

            DataParser dataParser = new DataParser();
//the same who could ,e
//Review is using separate one?
//TODO: {   "html_attributions" : [      for Start searching
//TODO"{   "geocoded_waypoints" :     check waypoints
            //This is where its connected to DataParser and get the List of places wiyh ratings etc.
            try {
                if (!result.contains("ZERO_RESULTS")) {
                    if (result.startsWith("{   \"html_attributions\" : [")) {
//THIS iS DETAILS< need to put details in list


                        if (result.contains("results")) {
                            nearbyPlacesList = dataParser.parse(result);
                            ShowNearbyPlaces(nearbyPlacesList);

                            pager.invalidate();
                            fragmentPagerAdapter.notifyDataSetChanged();

                        } else {
                            //Problem with next_page token not 100%(?solved?)
                            //CHeck ths
                            //     WayPointDetailsList.add(dataParser.parse(result).get(0));
                            //Log.d("Yeep", "dataparserresult1Details==" + dataParser.parse(result));
                            if (!dataParser.parse(result).isEmpty()) {
                                WayPointDetailsList.add(dataParser.parse(result).get(0));
                            } else {
//TODO:Note: this won't reset the thing because waypoint details list is changed..??? or something

                                showDialog(mContext, "No Location can be found using these criteria!!");
                            }
                            Log.d("Yeep", "checkselectedsizeint==" + selectedsizeint);
                            Log.d("Yeep", "checkselectedsizeint==" + getWayPointDetailsList());

                            if (selectedsizeint == getWayPointDetailsList().size()) {
                                pager.invalidate();
                                fragmentPagerAdapter.notifyDataSetChanged();
                            }
                        }


                    } else if (result.startsWith("{   \"geocoded_waypoints\" ")) {
                        //Wai... this is for direction...!??
                        // WayPointDetailsList.add(dataParser.parse(result));
                        //   Log.d("Yeep", "dataparserresult1==" + dataParser.parse(result));
//DirectionsFetcher a = new DirectionsFetcher();
//a.parse(result);
                        parserTask.execute(result);

                    }
                    Log.d("Yeep", "dataparserresult1==" + result);

                    Log.d("Yeep", "dataparserresult2=======" + nearbyPlacesList);


//Search list, user choose,
                } else {


                    //Temporaty holder TODO
                    showDialog(mContext, "No Location can be found using these criteria!!");
                }
                //   MapsActivity.pd.dismiss();

                //This must be used or else the viewopager will not update
                Log.d("MapsAcitivity", "Setdistance first");


                Log.d("MapsAcitivity", "Setdistance second ");

//TODO: must fix this shit
                //    recycleadapter.setSelectedItem();
            } catch (Exception e) {

                Log.d(TAG, "download URL Exception" + e);

            }
            try {

                TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);
                tabLayout.setupWithViewPager(pager);

                tabLayout.getTabAt(0).setIcon(R.drawable.searchicon);
                tabLayout.getTabAt(1).setIcon(R.drawable.filesimpleicon);


            } catch (Exception e) {
                //TODO:java.lang.NullPointerException: Attempt to invoke virtual method 'android.os.Handler android.support.v4.app

                Toast.makeText(context, "Uh oh! Some error has occured  ", Toast.LENGTH_SHORT).show();


            }

        }
    }

    public static void setDistanceURL(Context mContext) {
        Log.d("MapsAcitivity", "Setdistance inside" + WaypointwithDateList);
        Log.d("MapsAcitivity", "Setdistance inside" + WayPointDetailsList);

        try {
            for (int i = -1; i < WaypointwithDateList.size(); i++) {
                StringBuilder url = new StringBuilder();
                HashMap h = new HashMap();
                if (i >= 0) {
                    h = WaypointwithDateList.get(i);
                }
                HashMap end = new HashMap();
                if (i + 1 < WaypointwithDateList.size()) {
                    end = WaypointwithDateList.get(i + 1);
                } else {
                    end.put("place_id", "null");
                }


                //start 0 1
                // 1 1
                Log.d("A", "WaypointwithDateLigethash 1 " + WaypointwithDateList);
                Log.d("A", "WaypointwithDateLigethash 2 " + i);
                Log.d("A", "WaypointwithDateLigethash 3 " + h);

                Log.d("A", "WaypointwithDateLigethash 4 " + BaseActivity.getCurrentLocation());
                Log.d(TAG, "WaypointwithDateLigethash 5" + url.toString());
                ArrayList array = DataHolderClass.getInstance4().getDistributor_id4();
//TODO: why didnt i put everything in one url?
                url.append("https://maps.googleapis.com/maps/api/distancematrix/json?");
//                url.append("origins=place_id:" + h.get("place_id")+"|" + BaseActivity.getCurrentLocation().getLatitude() + "," +
//                        BaseActivity.getCurrentLocation().getLongitude()+ "&destinations=place_id:" + end.get("place_id"));
//
                if (i != -1) {
                    Log.d(TAG, "WaypointwithDateLigethash 6    first" + i);

                    url.append("origins=" + BaseActivity.getCurrentLocation().getLatitude() + "," +
                            BaseActivity.getCurrentLocation().getLongitude() + URLEncoder.encode("|", "UTF-8") + "place_id:" + h.get("place_id") +
                            "&destinations=place_id:" + end
                            .get("place_id"));
                } else {
                    Log.d(TAG, "WaypointwithDateLigethash 6    second" + i);

                    url.append("origins=" + BaseActivity.getCurrentLocation().getLatitude() + "," + BaseActivity.getCurrentLocation().getLongitude() +
                            "&destinations=place_id:" + end.get
                            ("place_id"));

                }


//Oh, because need this mode........
//TODO: try to see if need to make all into 1 url only.
                if (array != null && !array.isEmpty()) {
                    url.append("&mode=" + array.get(0));
                    url.append("&avoid=" + array.get(1));
                    url.append("&language=en");

                }
                //TODO:wtf? for some reason language solve the "" problem....?
                // url.append("&language=en");


                url.append("&key=AIzaSyD5XXsvbPu_ZMHr6D_nLfRmcIj7bESfzYk");
                Log.d(TAG, "WaypointwithDateLigethash 6  " + url.toString());
                if (!end.get("place_id").equals(null)) {
//String newurl= URLEncoder.encode(url.toString(),"UTF-8");
                    downloadDistanceurl download = new downloadDistanceurl(mContext);
                    download.execute(url.toString());

                }
            }

        } catch (Exception e) {
            Log.d(TAG, "Server is busy, Please try agai 22222" + e.toString());

            Toast.makeText(mContext.getApplicationContext().getApplicationContext(), "Server is busy, Please try again!", Toast
                    .LENGTH_SHORT).show();

        }
    }

    static ArrayList distance = new ArrayList();
    static ArrayList duration = new ArrayList();
    static ArrayList timetostart = new ArrayList();
    static ArrayList distancetonext = new ArrayList();
    static ArrayList durationtonext = new ArrayList();

    public static class downloadDistanceurl extends AsyncTask<String, Void, String> {

        private Context mContext;

        public downloadDistanceurl(Context context) {
            mContext = context;

        }

        @Override
        protected String doInBackground(String... strUrl) {

            String data = "";
            InputStream iStream = null;
            HttpsURLConnection urlConnection = null;


            try {
                int retry = 0;
                URL url = new URL(strUrl[0]);
                Log.d("A", "tryingtofindout doinbackground" + url);
                Log.d("check", "checkreturndatahell  123 " + url);


                // Creating an http connection to communicate with url
                urlConnection = (HttpsURLConnection) url.openConnection();

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

                    data = sb.toString();

                    Log.d("check", "checkreturndatahell 456  " + data);

                    br.close();
                    if (data.contains("You have exceeded your daily request quota for this API")) {
                        Thread.sleep(2000);

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

        //
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String jsonArrayduration = new String();
            String jsonArraydistance = new String();

            String jsonArraydistancetonext = new String();
            String jsonArraydurationtonext = new String();


            Long jsonArraytimetostart = new Long(0);

            Log.d("A", "tryingtofindout onPostExecute" + result);

            Log.d("A", "dafaqisresult" + result);
            JSONObject jsonObject;
            if (result != null) {
                try {
                    jsonObject = new JSONObject(result);

                    Log.d("A", "dafaqisresultjsonObjecttheobject " + jsonObject);
                    if (!jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getString("status")
                            .equals("NOT_FOUND")) {


                        Log.d("A", "tryingtofindouthaveurunthis 1");

                        jsonArraydistance = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject
                                ("distance").getString("text");
                        Log.d("A", "tryingtofindouthaveurunthis 2");

                        jsonArrayduration = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject
                                ("duration").getString("text");

                        if (jsonObject.getJSONArray("rows").length() > 1) {
                            Log.d("A", "tryingtofindout haveurunthis inside");

                            jsonArraydistancetonext = jsonObject.getJSONArray("rows").getJSONObject(1).getJSONArray("elements").getJSONObject(0)
                                    .getJSONObject
                                            ("distance").getString("text");

                            jsonArraydurationtonext = jsonObject.getJSONArray("rows").getJSONObject(1).getJSONArray("elements").getJSONObject(0)
                                    .getJSONObject
                                            ("duration").getString("text");


                            jsonArraytimetostart = jsonObject.getJSONArray("rows").getJSONObject(1).getJSONArray("elements").getJSONObject(0)
                                    .getJSONObject
                                            ("duration").getLong("value");
                        }
                        Log.d("A", "tryingtofindout haveurunthis outside");

                        Log.d("A", "tryingtofindout jsonArraydistance" + jsonArraydistance);
//^^^Correct

                        Log.d("A", "dafaqisresultjsonObjectjsonArraydistance" + jsonArraydistance);
                        Log.d("A", "dafaqisresultjsonObjectjsonArraytostart" + jsonArraytimetostart);

//duration=new ArrayList();
//                distance=new ArrayList();
                        ArrayList<LinkedHashMap<String, Object>> a = MapsActivity.getWayPointDetailsList();

                        Log.d("A", "distancedurationeasiertochec sizes  distance" + distance.size());

                        Log.d("A", "distancedurationeasiertochec sizes  " + a.size());
                        Log.d("A", "distancedurationeasiertochec l distance  " + distance);
                        //SHIThonr etong
                        Log.d("A", "distancedurationeasiertochec l duration  " + duration);


                        //   if (distance.size() < a.size()) {
                        //TODO: need to remove durations
                        if (distance.size() != a.size()) {

                            timetostart.add(jsonArraytimetostart);

                            distancetonext.add(jsonArraydistancetonext);
                            durationtonext.add(jsonArraydurationtonext);


                        }
                        distance.add(jsonArraydistance);
                        duration.add(jsonArrayduration);
                        Log.d("A", "distancedurationeasiertochec l distance  after add" + distance);


                        Log.d("A", "haverunhere  1size" + duration.size());
                        Log.d("A", "haverunhere  1size" + a.size());
                        if (duration.size() == a.size()) {
                            Log.d("A", "haverunhere  2");
                            Log.d("A", "haverunheretest outside");

                            for (int i = 0; i < duration.size(); i++) {
                                Log.d("A", "haverunhere  3");


                                Log.d("A", "distancedurationonly  1" + duration + distance);

                                Log.d("A", "distancedurationonly  2" + i);
                                Log.d("A", "distancedurationonly  3" + a);

                                a.get(i).put("duration", duration.get(i));


                                a.get(i).put("distance", distance.get(i));
                                if (i + 1 != duration.size()) {
                                    a.get(i).put("durationtonext", durationtonext.get(i + 1));

                                    //       a.get(i).put("dafaqistimeto ", timetostart);

                                    a.get(i).put("distancetonext", distancetonext.get(i + 1));
                                }
                                Long travelstarttime = null;

                                if (timetostart.get(i).toString().equals("")) {
                                    Log.d("A", "haveloadthis 1st" + a);

                                    Long durationtoreach = Long.parseLong(timetostart.get(i).toString());
                                    Long starttime = Long.parseLong(getWaypointwithDateList().get(i).get("starttime").toString());
                                    travelstarttime = starttime - durationtoreach;
                                    String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm").format(new java.util.Date(travelstarttime *
                                            1000));
                                    a.get(i).put("timetostart", date);
                                } else {
                                    Log.d("A", "haveloadthis 2nd" + a);

                                    a.get(i).put("timetostart", 0);

                                }


                            }
                            Log.d("A", "haverunhere  4");

                        }
                        Log.d("A", "checkingwtfisaasdasd" + duration.size());
                        Log.d("A", "checkingwtfisaasdasd" + a.size());

                        if (duration.size() == a.size()) {

                            duration.clear();
                            distance.clear();
                            durationtonext.clear();
                            distancetonext.clear();
                        }
//TODO:PLS work pls work

                        Log.d("A", "checkingwtfisa" + a);

                        Log.d("A", "checkingwtfisa this is way" + getWayPointDetailsList());
                    } else {

                        Log.d("A", "Yephaveranhgere" + getWayPointDetailsList());
                        duration.clear();
                        distance.clear();
                        durationtonext.clear();
                        distancetonext.clear();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException", "exceptionherejsonplscheck" + e);

                }
                //    if (!completedloop) {

                Log.d("A", "testingforposition" + "NOTIFYHASRUNNED" + distance);

                //    notifyDataSetChanged();

            }
        }


    }

    public static void showDialog(Context context, String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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
                .setUrl(Uri.parse("https://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;


    // Executed after the complete execution of doInBackground() method
    public static ConstraintLayout view;
    public static ConstraintLayout recyclerviewview;

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
            Log.d(TAG, "isthiscall....1");

            if (position == 0) {
                title = "Searches";
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

            return POSITION_NONE;
        }


    }


}




