package com.example.user.travelguideapps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */

//TODO: Initial Problem: the "Fragment" from when the loginactivity's intent might not be the same fragment from the later...??
public class MainMenuActivityFragment extends Fragment {

    public MainMenuActivityFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("BaserActivity", "fore main menu ");

    View fragment=inflater.inflate(R.layout.fragment_main_menu, container, false);
        Button searchButton=(Button)fragment.findViewById(R.id.main_Menu_Search_Button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //       DrawerLayout  drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);

        //      FrameLayout  flContent = (FrameLayout) drawer.findViewById(R.id.flContent2);


                Log.d("BaserActivity", "lakslu ");

                Fragment fragment = null;
                Class fragmentClass=null;
                fragmentClass = MapsActivity.class;


                try {
                    if(fragmentClass!=null) {

                        fragment = (Fragment) fragmentClass.newInstance();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();

            //    MainMenuActivityFragment.this.startActivity(new Intent(getContext(), MapsActivity.class));



            }
        });

        return fragment;
    }
}
