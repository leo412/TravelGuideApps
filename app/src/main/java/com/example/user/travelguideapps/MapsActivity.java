package com.example.user.travelguideapps;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "tag";
    ArrayList<LatLng> markerPoints;
    private static final String TAG_HOME = "home";
    public static String CURRENT_TAG = TAG_HOME;
    public static int navItemIndex = 0;
    private FloatingActionButton fab;
    private Handler mHandler;
    private ListView listView;
    private String LocationType = "bar";
    private View mProgressView;
    private View mMapFormView;
    private TextView mDurationView;
    private TextView mDistanceView;
    private LocationListViewAdapter Locationadapter;
    private ArrayList Waypoint = new ArrayList();

    private Polyline TempPoly;
    List<LinkedHashMap<String, String>> nearbyPlacesList = null;
    ProgressDialog pd;

    int MY_PERMISSION_ACCESS_COURSE_LOCATION = 0;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest = new LocationRequest();
    ArrayList<Marker> mapMarkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Successfully Logged in");

        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(MapsActivity.this);
        pd.setMessage("Loading Path...");
        pd.setCancelable(false);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        (GoogleApiClient.OnConnectionFailedListener) this /*
                        OnConnectionFailedListener */)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();


        }


        setContentView(R.layout.activity_maps);
        markerPoints = new ArrayList<LatLng>();
//        LayoutInflater inflater = (LayoutInflater) this
//                .getSystemService(this.LAYOUT_INFLATER_SERVICE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mHandler = new Handler();

        AutoCompleteTextView from = (AutoCompleteTextView) findViewById(R.id.from);
        AutoCompleteTextView to = (AutoCompleteTextView) findViewById(R.id.to);

        // AutoCompleteTextView from = (AutoCompleteTextView) v.findViewById(R.id.from);
        //   AutoCompleteTextView to = (AutoCompleteTextView) v.findViewById(R.id.to);
        Button but = (Button) findViewById(R.id.load_directions);

        Button SearchBar = (Button) findViewById(R.id.Bar);
        Button SearchBank = (Button) findViewById(R.id.Bank);
        Button SearchAmusement = (Button) findViewById(R.id.amusement_park);
        Button SearchCafe = (Button) findViewById(R.id.cafe);
        Button SearchCasino = (Button) findViewById(R.id.casino);
        Button SearchNightCLub = (Button) findViewById(R.id.night_club);
        mDurationView = (TextView) findViewById(R.id.duration_label);
        mDistanceView = (TextView) findViewById(R.id.distance_label);


        SearchBar.setOnClickListener(onClickListener);
        SearchBank.setOnClickListener(onClickListener);
        SearchAmusement.setOnClickListener(onClickListener);
        SearchCafe.setOnClickListener(onClickListener);
        SearchCasino.setOnClickListener(onClickListener);
        SearchNightCLub.setOnClickListener(onClickListener);

        //Testing Purpose, delete later
        // List<String> data=new List<String>()      {"abc"};
        //  from.setText("Google is your friend.", TextView.BufferType.EDITABLE);


        listView = (ListView) findViewById(R.id.poilist);

        final TextView emptyText = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);
        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        //Todo: Currently doing this
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
//                view.setSelected(true);
//
//
//                Object itemobj =(listView.getItemAtPosition(position).toString());
//                Toast.makeText(getApplicationContext(), itemobj.toString(), Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "abcdef checking for onoitem");
//
//              //  Toast.makeText(getApplicationContext(), lat, Toast.LENGTH_SHORT).show();
//
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//TODO:Figure out how to reset others items


//                listView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//
//                    @Override
//                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int
// oldBottom) {
//                        listView.removeOnLayoutChangeListener(this);
//                        Log.e(TAG, "updated");
//                    }
//                });
//                Locationadapter.notifyDataSetChanged();

//TODO: Delete others's buttons
//                for (int i = listView.getCount() - 1; i >= 0; i--) {
//
//                    Button b=(Button)    view.findViewById(R.id.details_btn);
//
//                    b.setVisibility(View.GONE);
//
//
//                }
//               Button d=(Button) parent.findViewById(R.id.details_btn);
//
//                d.setVisibility(View.GONE);
//TODO:Figure out how to
                HashMap<String, Object> obj = (HashMap<String, Object>) listView.getItemAtPosition(position);
                String lat = (String) obj.get("lat");
                String lng = (String) obj.get("lng");
                //TODO: the "Selected" refresh when finish loading / Check if Focused on item how to do "somthing"


                Button b = (Button) view.findViewById(R.id.details_btn);
                b.setVisibility(View.VISIBLE);
                view.setSelected(true);


                Log.d("Yourtag", lat);
                //  Toast.makeText(getApplicationContext(), lat.toString(), Toast.LENGTH_SHORT).show();
                LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                Marker result = null;
                for (Marker c : mapMarkers) {
                    Log.d("Well", String.valueOf(c.getPosition().latitude));

                    if (lat.equals(String.valueOf(c.getPosition().latitude)) && lng.equals(String.valueOf(c.getPosition().longitude))) {
                        Log.d("Its done ", lat);
                        showPath(c);
                        result = c;

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

                        break;
                    }
                }

            }
        });


        from.setAdapter(new LocationAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
        to.setAdapter(new LocationAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing
        // API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
        }


        mMapFormView = findViewById(R.id.list_form);
        mProgressView = findViewById(R.id.maps_progress);

        Log.d(TAG, "Visibibility checking if progess is showing" + mProgressView.getVisibility());

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        //Get placeid and sace
        String place_id = intent.getStringExtra("place_id");
        if (place_id != null) {
            Waypoint.add(place_id);
            Toast.makeText(getApplicationContext(), Waypoint.toString(), Toast.LENGTH_SHORT).show();

        }

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!DetectConnection.checkInternetConnection(MapsActivity.this)) {
                Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
            } else

            {

                mMap.clear();
                switch (v.getId()) {
                    case R.id.Bar:
                        LocationType = "bar";
                        break;
                    case R.id.Bank:
                        LocationType = "bank";
                        break;
                    case R.id.amusement_park:
                        LocationType = "amusement_park";
                        break;
                    case R.id.cafe:
                        LocationType = "cafe";
                        break;
                    case R.id.casino:
                        LocationType = "casino";
                        break;
                    case R.id.night_club:
                        LocationType = "night_club";
                        break;
                }
                setListView(LocationType);
            }
        }

    };


    //todo: check whether to move "List" to another classes
    private void setListView(String locationType) {

        StringBuilder sbValue = new StringBuilder(sbMethod());
        PlacesTask placesTask = new PlacesTask();
        placesTask.execute(sbValue.toString());

        TextView emptyText = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);

        if (listView == null) {

            Log.d(TAG, "Listview is null");

        }


    }


    //TODO:Currently Doing this, try to do a if else for 1 marker (select list)? or multiple marker
    private void ShowNearbyPlaces(List<LinkedHashMap<String, String>> nearbyPlacesList) {

        mapMarkers = new ArrayList<Marker>();
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            LinkedHashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            Log.d("onPostExecute", "Entered into showing locations" + googlePlace.toString());

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lng = Double.parseDouble(googlePlace.get("lng"));

            double lat = Double.parseDouble(googlePlace.get("lat"));

            String photo_reference = googlePlace.get("photo_reference");

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            mapMarkers.add(mMap.addMarker(markerOptions));
            mMap.addMarker(markerOptions);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
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
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                DetectConnection check = new DetectConnection();
                if (!DetectConnection.checkInternetConnection(MapsActivity.this)) {
                    Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                    return false;
                } else {

                    Log.d("onMarkerCLick", marker.toString());
                    ArrayList<View> children = new ArrayList<View>();
                    Log.d("ListChile", String.valueOf(listView.getCount()));

                    for (int i = listView.getCount() - 1; i >= 0; i--) {
                        HashMap<String, Object> obj = (HashMap<String, Object>) listView.getItemAtPosition(i);

                        String lat = (String) obj.get("lat");
                        String lng = (String) obj.get("lng");
                        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        //TODO : Highlight item when marker is clicked.
                        if (lat.equals(String.valueOf(marker.getPosition().latitude)) && lng.equals(String.valueOf(marker.getPosition().longitude))) {

                            listView.setItemChecked(i, true);
                            //  listView.setSelection(i);
                            listView.smoothScrollToPosition(i);
                            break;
                        }
                        children.add(listView.getChildAt(i));
                    }
                    showPath(marker);
                    //
                    return true;
                }
            }
        });

    }

    //TODO:How to use : different colour marker/path, retain 2 different path.
    //Done when item in listview is clicked
    //Also when marker is clicked
    private void showPath(Marker marker) {

        marker.showInfoWindow();

        if (TempPoly != null) {
            TempPoly.remove();
        }

        markerPoints.clear();
        Location location = CurrentLocation();
        LatLng currentLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        markerPoints.add(currentLatlng);


        LatLng ClickedMarker = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

        Log.d(TAG, "Current Location Lat is " + ClickedMarker);

        markerPoints.add(ClickedMarker);
        Log.d(TAG, "Check if system workign got not,  " + currentLatlng.longitude);

        // Creating MarkerOptions

        MarkerOptions options = new MarkerOptions();
        Log.d(TAG, "Current Checking " + currentLatlng.longitude);

        // Setting the position of the marker
//For the stating part, this is for non sensational problems
        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
//                if (markerPoints.size() == 1) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//                } else if (markerPoints.size() == 2) {
//                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                }

        // Add new marker to the Google Map Android API V2
        // mMap.addMarker(options);

        // Checks, whether start and end locations are captured
//                if (markerPoints.size() >= 2) {
        LatLng origin = markerPoints.get(0);
        LatLng dest = markerPoints.get(1);

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();
        Log.d(TAG, "THE URL is  IS " + url);

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);


    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            Log.d(TAG, "Progress bar ");

            mMapFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            mMapFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mMapFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                    Log.d(TAG, "Progress 2");

                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mMapFormView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        }
    }

    //Pressed twice to quit
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String waypoint=new String();
        StringBuilder waypointsb=new StringBuilder();
        if (!Waypoint.isEmpty()) {
             waypointsb.append("&waypoints=");

            for (int i = 0; i < Waypoint.size(); i++) {
                 waypoint =(waypointsb.append("place_id:"+Waypoint.get(i)+"|").toString());

            }


//To remove "|"
            waypoint=waypoint.substring(0,waypoint.length()-1);

        }else{

            waypoint="";
        }
        // Sensor enabled
        String sensor = "sensor=false";
        String parameters;
   //     if (Waypoint == null) {
            // Building the parameters to the web service
            parameters = str_origin + "&" + str_dest +waypoint+"&" + sensor + "&key=AIzaSyAQjMlUIxrybWWkko4AVHmE_Evr1I625cs";
    //    } else {

      //      parameters = str_origin + "&" + str_dest + "&" + sensor + "key=AIzaSyAQjMlUIxrybWWkko4AVHmE_Evr1I625cs";


      //  }
        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

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


    //Path Searching
    //Download URL ?
    //change to public for access from placedetail activity
    public class DownloadTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            //set message of the dialog
            //show dialog

            pd.show();
            super.onPreExecute();
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            Log.d(TAG, url[0]);
            Log.d(TAG, "Do in Back Test");

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }


            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//Click Marker and find path? Probably this is
            Log.d("PTRESULT", result);

            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
            pd.dismiss();
        }


    }

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
    private class ParserTask extends AsyncTask<String, Integer, List<List<LinkedHashMap<String, String>>>> {

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
                    Log.d("testhahanumber=",j+ point.toString());

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

                //TODO:DOing this , try to have dfifferent colours?
                lineOptions.color(Color.RED);

                mDistanceView.setText("Distance= " + distance);
                mDurationView.setText("Duration= " + duration);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");
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

    private String downloadUrl(String strUrl) throws IOException {
        Log.d(TAG, "checkifone");

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        Log.d(TAG, "Download URL" + strUrl);
        Log.d(TAG, "checkiftwo");

        try {
            int retry = 0;
            URL url = new URL(strUrl);

            Log.d(TAG, "Download URL testing" + strUrl);
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
                Log.d(TAG, "Direction Data for URL Download" + data);
                br.close();
                if (data.contains("You have exceeded your daily request quota for this API")) {
                    Thread.sleep(2000);
                    Log.d(TAG, "howmanytimesis thread");

                } else {

                    retry = 1;

                }
            }

        } catch (Exception e) {
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


        Log.d(TAG, "Connection fail");


    }


    private void loadHomeFragment() {
        // selecting appropriate nav menu item

        // set toolbar title

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
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
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
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
        invalidateOptionsMenu();
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

    public Location CurrentLocation() {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setNumUpdates(1);
        mLocationRequest.setFastestInterval(1000);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "This is Currentlocationcheckinginsideif");

        }
        Log.d(TAG, "This is mGoogleApiClient" + mGoogleApiClient);

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // Location location = locationManager.getLastKnownLocation(provider);
        Log.d(TAG, "This is Currentlocationchecking" + location);

        // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        return location;
    }


    public StringBuilder sbMethod() {

        Location location = CurrentLocation();

        Log.d("Map", "Locationnow=" + location);

        try {
            //use your current location here
            double mLatitude = location.getLatitude();
            double mLongitude = location.getLongitude();

            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb.append("location=" + mLatitude + "," + mLongitude);
            sb.append("&radius=5000");
            sb.append("&types=" + LocationType);
            sb.append("&sensor=true");
            sb.append("&key=AIzaSyDkIa12Y9nXORou_xCnwS09K53kbJabKHo");
            ;
            Log.d("Map", "api: " + sb.toString());

            return sb;
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder("Fail");

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

                data = downloadUrl(url[0]);
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

            ParserTask parserTask = new ParserTask();
            Log.d(TAG, "On post execute 1st");

            Log.d("GooglePlacesReadTask", "onPostExecute Entered");
            List<LinkedHashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();


            //This is where its connected to DataParser and get the List of places wiyh ratings etc.
            nearbyPlacesList = dataParser.parse(result);

//
//            Log.d("GooglePlacesReadTask", nearbyPlacesList.toString());
//            ListAdapter adapter = new SimpleAdapter(
//
//                    MapsActivity.this, nearbyPlacesList,
//
//                    R.layout.list_item, new String[]{"place_name"}, new int[]{R.id.name}
//
//            );
            Log.d("GooglePlacesReadTask", "theheckis" + nearbyPlacesList);

            //This use locationadapter to put in listview?
            Locationadapter = new LocationListViewAdapter(MapsActivity.this, nearbyPlacesList);
            listView.setAdapter(Locationadapter);


            Log.d("GooglePlacesReadTask", "Well SHIT" + listView.toString());

            ShowNearbyPlaces(nearbyPlacesList);
            Log.d("GooglePlacesReadTask", "onPostExecute Exit");


            pd.dismiss();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);

            Log.d(TAG, "On post execute ENDNENDNEDNNEND");
        }
    }


    // Executed after the complete execution of doInBackground() method

}







