package com.example.user.travelguideapps;

import java.util.ArrayList;

/**
 * Created by User on 6/21/2017.
 */

public class FirebaseDatabaseUser {
    public String username;
    public ArrayList waypoint;
    public FirebaseDatabaseUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)




    }

    public FirebaseDatabaseUser(String username, ArrayList Waypoint) {

        this.username = username;
        this.waypoint = Waypoint;


    }




}
