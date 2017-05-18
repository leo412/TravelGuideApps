package com.example.user.travelguideapps;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

public class BaseActivity extends AppCompatActivity
        {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", "Baserunning");
    
        setContentView(R.layout.activity_base);
    //   View view = View.inflate(this, R.layout.activity_base, null);

        //*************************************************Toolbar drawer?"
    //     toolbar = (Toolbar) findViewById(R.id.toolbar);
   //     setSupportActionBar(toolbar);
      //   drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.(drawerToggle);
      //  drawerToggle.syncState();


        Log.d("BaserActivity", "ssssss");
//
   //    navigationView.setNavigationItemSelectedListener(this);


        //*************************************************Toolbar drawer?"



    }
            @Override
            public void setContentView(int layoutResID) {
                Log.d("BaserActivity", "setconte");

                drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);



                //flComtent 2 is in content_base
                FrameLayout flContent = (FrameLayout) drawer.findViewById(R.id.flContent2);

                getLayoutInflater().inflate(layoutResID, flContent, true);

                super.setContentView(drawer);

                 toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                setTitle("Activity Title");
                navigationView = (NavigationView) findViewById(R.id.nav_view);
                setupDrawerContent(navigationView);

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
                        Log.d("BaserActivity", "onnaviga");

                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

            public void selectDrawerItem(MenuItem menuItem) {
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


}
