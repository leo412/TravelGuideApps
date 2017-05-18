package com.example.user.travelguideapps;

/**
 * Created by User on 3/1/2017.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.content.ContentValues.TAG;
//Get Places (Why name Datapasers

public class DataParser {
//TODO: Make it obtain all data available?
//This is where Data is obtain from the internet
    public List<LinkedHashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        //placenumber 0 = single 1= many

        int placenumber=0;
        try {
            jsonObject = new JSONObject((String) jsonData);
            Log.d("Places", "checkjsonobject"+jsonObject);

            if(jsonObject.has("results")){
                //For Places List
                Log.d("Places", "EnterResultSSSSSSS");

                jsonArray= jsonObject.getJSONArray("results");
                Log.d("Places", "AFterEnterResultSSSSSSS"+jsonArray);
                placenumber=0;
            }else {
                //For single location(Details
                Log.d("Places", "EnterResult2"+jsonObject.getJSONObject("result"));

             //   JSONObject jsonPlaceDetails = jsonObject.getJSONObject("result");

                //check what is in json array
                jsonArray.put(jsonObject.getJSONObject("result"));
             //   jsonArray[0]=jsonPlaceDetails.getJSONObject("result");

                Log.d("Places", "Afternormalenterresult"+jsonArray);

                placenumber=1;
            }
        } catch (JSONException e) {
            Log.d("Places", "parse error"+e);
            e.printStackTrace();
        }
        return getPlaces(jsonArray,placenumber);
    }
//Add a new variable to see if it is "Single or morwe"

    private List<LinkedHashMap<String, String>> getPlaces(JSONArray jsonArray,int placenumber) {
        int placesCount = jsonArray.length();
        List<LinkedHashMap<String, String>> placesList = new ArrayList<>();
        LinkedHashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i),placenumber);
                placesList.add(placeMap);
                Log.d("Places", "Adding places");
                Log.d("Places", placesList.toString());

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private LinkedHashMap<String, String> getPlace(JSONObject jPlace,int placenumber) {

        LinkedHashMap<String, String> place = new LinkedHashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        List<String>photo_reference = new ArrayList<String>();
        String rating = "";
        String place_id="";

        //Below Single location only
        String formatted_address="";
        String formatted_phone_number="";
        String icon ="";
        String opening_hours="";
        String multiple_photo_references="";
        String review="";

        //Single Location Details
        //formatted_address
        //formatted_phone
        //icon??
        //OPENING HOUSRS
        //View deltails oopening hours
        //photos (multiple photos)
        //reviews
        //type




        try {
            if (placenumber==1){
                if (!jPlace.isNull("formatted_address")) {
                    formatted_address = jPlace.getString("formatted_address");
                }
                if (!jPlace.isNull("formatted_phone_number")) {
                    formatted_phone_number = jPlace.getString("formatted_phone_number");
                }


            }


            Log.d(TAG, "Extracting  Location " );

            // Extracting Location name, if available
            if (!jPlace.isNull("name")) {
                placeName = jPlace.getString("name");
            }



            // Extracting Location Vicinity, if available
            if (!jPlace.isNull("vicinity")) {
                vicinity = jPlace.getString("vicinity");
            }
            Log.d(TAG, "Testing " );
try {
    if (jPlace.getJSONArray("photos").getJSONObject(0).getString("photo_reference") != null) {
        int i=jPlace.getJSONArray("photos").length();
        for (int j=0;j<i;j++) {
            //Trying to get multipole photo checking
            photo_reference.add(jPlace.getJSONArray("photos").getJSONObject(j).getString("photo_reference"));
            Log.d(TAG, "Howmanytimesa"+ photo_reference );


        }
    } else {
        photo_reference.add("Empty");

    }


}catch (Exception e){
    Log.d(TAG, "Exception is "+e );


}
            if (!jPlace.isNull("rating")) {
                rating = jPlace.getString("rating");
            }else{
                rating = "No Rating";


            }
            place_id = jPlace.getString("place_id");
            Log.d(TAG, "Placeiiid is "+placeName+place_id );
            longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");

            latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");

            reference = jPlace.getString("reference");
            place.put("place_name", placeName);
            place.put("vicinity", vicinity);
            place.put("photo_reference", photo_reference.toString());
            place.put("rating", rating);
            place.put("lat", latitude);
            place.put("lng", longitude);
            place.put("reference", reference);
            place.put("place_id", place_id);



            Log.d(TAG, "placeeee "+place.toString() );

            if(placenumber==1){
                place.put("formatted_address", formatted_address);

                place.put("formatted_phone_number", formatted_phone_number);
            }



            Log.d(TAG, "Placeid is "+place_id+"lat is"+latitude );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return place;
    }
}
