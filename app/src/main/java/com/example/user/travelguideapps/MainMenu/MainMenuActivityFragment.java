package com.example.user.travelguideapps.MainMenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.user.travelguideapps.EditLocation.EditLocation;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;

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
        getActivity().setTitle("Main Menu");

        View v=inflater.inflate(R.layout.fragment_main_menu, container, false);
        Button searchButton=(Button)v.findViewById(R.id.main_Menu_Search_Button);
        Button editButton=(Button)v.findViewById(R.id.main_Menu_Edit_Button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            //    fragmentTransaction.replace(R.id.flContent2, fragment);
             //   fragmentTransaction.addToBackStack(null);
//
                Log.d("BaserActivity", "hereitisrunning before");

                if(fragment.isAdded())
                {
                    Log.d("BaserActivity", "hereitisrunning previouslyadded");

                    fragmentTransaction.show(fragment);
                }
                else
                {
                    Log.d("BaserActivity", "hereitisrunning added");

                    fragmentTransaction.replace(R.id.flContent2, fragment);
                    fragmentTransaction.addToBackStack(null);
//nfly senvered
                }
                fragmentTransaction.commit();



            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;

                Class fragmentClass=null;

                fragmentClass = EditLocation.class;

                try {
                    if(fragmentClass!=null) {
                        Log.d("Mainmenu", "Gothere");

                        fragment = (Fragment) fragmentClass.newInstance();
                    }
                } catch (Exception e) {
                    Log.d("Mainmenu", "didnotGothere"+e);

                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent2, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }
        });



        return v;
    }
}
