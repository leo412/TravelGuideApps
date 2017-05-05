package com.example.user.travelguideapps;

/**
 * Created by User on 2/24/2017.
 */
import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

/** Implement this class from "Serializable"
 * So that you can pass this class Object to another using Intents
 * Otherwise you can't pass to another actitivy
 * */
public class LocationListList implements Serializable {

    @Key
    public String status;

    @Key
    public List<Location> results;

}