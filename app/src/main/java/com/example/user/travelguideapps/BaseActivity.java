package com.example.user.travelguideapps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.user.travelguideapps.MainMenu.MainMenuActivityFragment;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener, NavigationView.OnNavigationItemSelectedListener
        {
            ArrayList place_idarray;

            private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest = new LocationRequest();
            private LocationManager locationManager;
            private String provider;
            private static Location CurrentLocation;
        private    FrameLayout flContent;
            private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", "Baserunning");
    
        setContentView(R.layout.activity_base);

        Fragment fragment = null;
        Class fragmentClass=null;

        fragmentClass = MainMenuActivityFragment.class;

        try {
            if(fragmentClass!=null) {
                fragment = (Fragment) fragmentClass.newInstance();
                Log.d("BaserActivity", "oppssss");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragmentClass!=null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)

                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Log.d("BaserActivity", "ssssss");


    }
            @Override
            public void setContentView(int layoutResID) {
                Log.d("BaserActivity", "setconte");

                drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

                //flComtent 2 is in content_base
                 flContent = (FrameLayout) drawer.findViewById(R.id.flContent2);

                getLayoutInflater().inflate(layoutResID, flContent, true);

                super.setContentView(drawer);

                 toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setTitle("Activity Title");
                navigationView = (NavigationView) findViewById(R.id.nav_view);
                if (navigationView != null) {

                    Log.d("BaseActivity", "navigaterun");

                    setupDrawerContent(navigationView);
                }
                Log.d("BaserActivity", "diditrun");

                final ActionBar actionBar = getSupportActionBar();

                if (actionBar != null)
                {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                    drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer)
                    {

                        public void onDrawerClosed(View view)
                        {
                            supportInvalidateOptionsMenu();
                            //drawerOpened = false;
                        }

                        public void onDrawerOpened(View drawerView)
                        {
                            supportInvalidateOptionsMenu();
                            //drawerOpened = true;
                        }
                    };
                    drawerToggle.setDrawerIndicatorEnabled(true);
                    drawer.setDrawerListener(drawerToggle);
                    drawerToggle.syncState();
                    Log.d("BaserActivity", "diditrundea");

                }



            }
            @Override
            protected void onPostCreate(Bundle savedInstanceState)
            {
                super.onPostCreate(savedInstanceState);
                drawerToggle.syncState();
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig)
            {
                super.onConfigurationChanged(newConfig);
                drawerToggle.onConfigurationChanged(newConfig);
            }



    private void setupDrawerContent(NavigationView navigationView) {

    navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Log.d("BaseActivity","navigaterunsetnavi");

                    selectDrawerItem(menuItem);
                    return true;
                }
            });


    }

             @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                selectDrawerItem(menuItem);
                return true;
            }
            public void selectDrawerItem(MenuItem menuItem) {
                Log.d("BaserActivity", "isentercase1");

                // Create a new fragment and specify the fragment to show based on nav item clicked
                Fragment fragment = null;
                Class fragmentClass=null;
                switch(menuItem.getItemId()) {
                    case R.id.nav_main_menu:
                        Log.d("BaserActivity", "isentercase");

                        fragmentClass = MainMenuActivityFragment.class;
                        break;
                    case R.id.nav_maps:

                  //      BaseActivity.this.startActivity(new Intent(BaseActivity.this, MapsActivity.class));
                  //      android.app.Fragment mFragment = getFragmentManager().findFragmentById(R.layout.activity_maps      );
                  //      if (mFragment instanceof )
                        fragmentClass = MapsActivity.class;


                       // fragmentClass = MainMenuActivityFragment.class;
                        break;
                    case R.id.nav_slideshow:
                        fragmentClass = MainMenuActivityFragment.class;
                        break;
                    default:
                        fragmentClass = MainMenuActivityFragment.class;
                }

                try {
                    if(fragmentClass!=null) {
                        fragment = (Fragment) fragmentClass.newInstance();
                        Log.d("BaserActivity", "oppssss");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
if (fragmentClass!=null) {
    // Insert the fragment by replacing any existing fragment
        //...Its after calling that it will overlap, wtf?
    //TODO: this probably is not really good since it required Views to be restart everytime...?
//Usinf remove all views just destroy  ability to go back
   // flContent.removeAllViews();
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();
}
                // Highlight the selected item has been done by NavigationView
                menuItem.setChecked(true);
                // Set action bar title
                setTitle(menuItem.getTitle());
                // Close the navigation drawer
                drawer.closeDrawers();
            }
            //TODO: uhhhh i dont think these things are being used....
        public void setPlace_id(ArrayList a){
            Log.d("BASE", "uhhhhhhhfirstsetplace"+a+"andalso"+place_idarray);

    place_idarray=a;
        }
            public ArrayList getPlace_id(){
                Log.d("BASE", "uhhhhhhhfirstgetplace"+place_idarray);

            return place_idarray;
            }

//
//    @Override
//    public void setContentView(int layoutResID) {
//        Log.d("BaseActivity", "Baserunningsetcontent"+layoutResID);
//
//        final DrawerLayout fullView = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
//       NavigationView activityContainer = (NavigationView) fullView.findViewById(R.id.nav_view);
////        FrameLayout activityContainer= (FrameLayout) fullView.findViewById(R.id.nav_Frame);
//
//
//        getLayoutInflater().inflate(layoutResID, activityContainer,true);
//        super.setContentView(fullView);
//
//
//
//    }











    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
            public static DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();



            public void writeNewUser(String userId, String name, ArrayList Waypoint) {
                FirebaseDatabaseUser user = new FirebaseDatabaseUser(name, Waypoint);

                mDatabase.child("users").child(userId).setValue(user);
            }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
            public void onConnected(@Nullable Bundle bundle) {
                Log.d("BaseActivity", "diditevenonconnected");
                mLocationRequest = LocationRequest.create();
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setInterval(5000);
                mLocationRequest.setNumUpdates(1);
                mLocationRequest.setFastestInterval(1000);


                // Get the location manager
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

                // Define the criteria how to select the locatioin provider -> use
                // default
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, false);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }
                try {
                    //   CurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    Log.d("BaseActivity", "Yerp. updating... ");

                }catch (Exception e){

                    Log.d("BaseActivity", "requestLocationUpdateError"+e);


                }



                    CurrentLocation=fusedLocationProviderApi.getLastLocation(mGoogleApiClient);
              //   Location location = locationManager.getLastKnownLocation(provider);
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("BaseActivity", "diditevensuspend");

            }

            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.d("BaseActivity", "failsssss"+connectionResult);

            }

            @Override
            public void onLocationChanged(Location location) {
                Log.d("BaseActivity", "OnLocationChanged");

                CurrentLocation=location;

            }
public static Location getCurrentLocation(){

    return CurrentLocation;
}
            @Override
            public void onMapReady(GoogleMap googleMap) {

            }

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
            public void onStart() {

                mGoogleApiClient.connect();
                Log.d("BaseActivity", "diditevenstart");

                super.onStart();


                // ATTENTION: This was auto-generated to implement the App Indexing API.
                // See https://g.co/AppIndexing/AndroidStudio for more information.
          //      AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
            }

            public void onStop() {
                mGoogleApiClient.disconnect();

                super.onStop();
                // ATTENTION: This was auto-generated to implement the App Indexing API.
                // See https://g.co/AppIndexing/AndroidStudio for more information.
            //    AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
            }
        }