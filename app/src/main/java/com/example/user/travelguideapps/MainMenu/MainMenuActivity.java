package com.example.user.travelguideapps.MainMenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.R;

public class MainMenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

             setContentView(R.layout.activity_main_menu);

        Fragment fragment = new MainMenuActivityFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flContent2, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

    }





}
