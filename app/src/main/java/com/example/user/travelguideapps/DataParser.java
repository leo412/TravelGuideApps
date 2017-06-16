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
    //TODO:seems to run twice when clicking view deatails and set places, possible to made it directly obtain previous data?
//This is where Data is obtain from the internet
    //False.... pretty sure its used to parse String to json (LInked hashmap
    public List<LinkedHashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;
        //placenumber 0 = many 1= single

        int placenumber=0;
        try {
            jsonObject = new JSONObject((String) jsonData);
            Log.d("Places", "checkjsonobject"+jsonObject);

            if(jsonObject.has("results")){
                //For Places List
                jsonArray= jsonObject.getJSONArray("results");
                placenumber=0;
            }else {
                //For single location(Details
                jsonArray.put(jsonObject.getJSONObject("result"));


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

    //TODO: separate details (1) and summaries(0)
//Isn't this overcomplicated things...?
    private LinkedHashMap<String, String> getPlace(JSONObject jPlace,int placenumber) {

        LinkedHashMap<String, String> place = new LinkedHashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        ArrayList<String>photo_reference = new ArrayList<String>();

        ArrayList<String>review_author_name = new ArrayList<String>();
        ArrayList<String>review_author_photo = new ArrayList<String>();
        ArrayList<String>review_rating = new ArrayList<String>();
        ArrayList<String>review_time_description = new ArrayList<String>();
        ArrayList<String>review_text = new ArrayList<String>();

        String rating = "";
        String place_id="";

        //Below Single location only
        String formatted_address="";
        String formatted_phone_number="";
        String website="";

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
                }else{
                    formatted_phone_number="This location has no phone number";

                }

                if (!jPlace.isNull("website")) {
                    website = jPlace.getString("website");
                }else{
                    website="This location has no Website";

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



            try {
                if (jPlace.getJSONArray("reviews").getJSONObject(0).getString("author_name") != null) {
                    Log.d(TAG, "Well it did run here.... is ");

                    int i=jPlace.getJSONArray("reviews").length();
                    for (int j=0;j<i;j++) {
                        Log.d(TAG, "Well it did run here.... again 1st "+j);

                        //Trying to get multipole photo checking
                        review_author_name.add(jPlace.getJSONArray("reviews").getJSONObject(j).getString("author_name"));

                        Log.d(TAG, "Well it did run here.... again after"+j);

                    }
                } else {
                    review_author_name.add("Empty");

                }


            }catch (Exception e){
                Log.d(TAG, "Exception is "+e );


            }

            try {
                if (jPlace.getJSONArray("reviews").getJSONObject(0).getString("relative_time_description") != null) {

                    int i=jPlace.getJSONArray("reviews").length();
                    for (int j=0;j<i;j++) {

                        //Trying to get multipole photo checking
                        review_time_description.add(jPlace.getJSONArray("reviews").getJSONObject(j).getString("relative_time_description"));


                    }
                } else {
            //        review_time_description
                }


            }catch (Exception e){
                Log.d(TAG, "Exception is "+e );


            }

            try {
                if (jPlace.getJSONArray("reviews").getJSONObject(0).getString("rating") != null) {

                    int i=jPlace.getJSONArray("reviews").length();
                    for (int j=0;j<i;j++) {

                        //Trying to get multipole photo checking
                        review_rating.add(jPlace.getJSONArray("reviews").getJSONObject(j).getString("rating"));


                    }
                } else {
                    review_rating.add("Empty");

                }


            }catch (Exception e){
                Log.d(TAG, "Exception is "+e );


            }

            try {
                if (jPlace.getJSONArray("reviews").getJSONObject(0).getString("profile_photo_url") != null) {

                    int i=jPlace.getJSONArray("reviews").length();
                    for (int j=0;j<i;j++) {

                        //Trying to get multipole photo checking
                        review_author_photo.add(jPlace.getJSONArray("reviews").getJSONObject(j).getString("profile_photo_url"));


                    }
                } else {
                    review_author_photo.add("Empty");

                }


            }catch (Exception e){
                Log.d(TAG, "Exception is "+e );


            }
            try {
                if (jPlace.getJSONArray("reviews").getJSONObject(0).getString("text") != null) {
                    int i=jPlace.getJSONArray("reviews").length();
                    for (int j=0;j<i;j++) {
                        //Trying to get multipole photo checking
                        Log.d(TAG, "letscheckthis is "+jPlace.getJSONArray("reviews").getJSONObject(j).getString("text") );

                        Log.d(TAG, "letscheckthis is "+jPlace );



                        if(!jPlace.getJSONArray("reviews").getJSONObject(j).getString("text").equals("")) {
                            review_text.add(jPlace.getJSONArray("reviews").getJSONObject(j).getString("text") + "Separator");
                        }else{
                            Log.d(TAG, "letscheckthis is nothing b" );

                            review_text.add("This user wrote nothing Separator");
                        }
                        Log.d(TAG, "letscheckthis is "+review_text );

                    }
                } else {
                    review_text.add("Empty");

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
            place.put("review_author_name",review_author_name.toString());
            place.put("review_text",review_text.toString());
            place.put("review_author_photo",review_author_photo.toString());
            place.put("review_time_description",review_time_description.toString());
            place.put("review_rating",review_rating.toString());
            place.put("website",website.toString());


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
