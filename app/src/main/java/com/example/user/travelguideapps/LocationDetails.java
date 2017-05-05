package com.example.user.travelguideapps;

/**
 * Created by User on 2/24/2017.
 */

import com.google.api.client.util.Key;

import java.io.Serializable;

/** Implement this class from "Serializable"
 * So that you can pass this class Object to another using Intents
 * Otherwise you can't pass to another actitivy
 * */
public class LocationDetails implements Serializable {

    @Key
    public String status;

    @Key
    public Location result;

    @Override
    public String toString() {
        if (result!=null) {
            return result.toString();
        }
        return super.toString();
    }
}
