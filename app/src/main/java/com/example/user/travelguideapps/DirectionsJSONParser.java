package com.example.user.travelguideapps;

/**
 * Created by User on 2/23/2017.
 */

import android.util.Log;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;



//Todo: probably duplicate of DirectionsFetcher

public class DirectionsJSONParser {

    /** Receives a JSONObject and returns a list of lists containing latitude and longitude */
    public List<LinkedHashMap<String,String>> parse(JSONObject jObject){
        Log.d("TAG", "Directions JSON PAerser Before whole things");

        Log.d("TAG", "Running Up"+jObject.toString());

        List<Result> list = new ArrayList<>();
        LinkedHashMap<String, String> location =    new LinkedHashMap<>();
        List<LinkedHashMap<String, String>> routes = new ArrayList<LinkedHashMap<String,String>>() ;
        JSONArray jRoutes = null;
        JSONArray jid = null;
        JSONArray jname = null;


        StringBuffer mBuffer = new StringBuffer();

        String jsonResult = mBuffer.toString();

        if (jsonResult != null) {
            try {
                Log.d("TAG", "Running Up here");








               // JSONObject jsonObj = new JSONObject(jsonResult);




                JSONArray jsonArray = jObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    //Creating LocationList model class object to store details


                    JSONObject object = jsonArray.getJSONObject(i);
                    String name = object.optString("name").toString();

                    Log.d("TAG", "This object is  ="+object.toString());



                    if (object.has("name")) {



                        //jid = ( (JSONObject)jsonArray.get(i)).getJSONArray("id");

                        Log.d("TAG", "Enter here and ID ="+object.getString("id"));
                        location.put("name",object.getString("name") );
                       // result.setid(object.getString("id"));

                       // jRoutes.(object.getString("id"));
                    }
//                    if (object.has("vicinity")) {
//                        result.setName(object.getString("vicinity"));
//                    } else {
//                        result.setVicinity("");
//                    }
//                    if (object.has("opening_hours")) {
//                        if (object.getJSONObject("opening_hours").has("open_now")) {
//                            result.setOpening_now(object.getJSONObject("opening_hours").getString("open_now"));
//                        } else {
//                            result.setOpening_now("");
//                        }
//                    }
                    routes.add(location);
                }
                for (int i = 0; i < list.size(); i++) {
                   // CustomLog.d("List = " + list.get(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        Log.d("TAG", "The routes is ="+routes);

        return routes ;
        // return list;















//        try {
//            Log.d("TAG", jObject.toString());
//
//            jRoutes = jObject.getJSONArray("results");
//            Log.d("TAG", jRoutes.toString());
//
//            for (int i = 0; i < jRoutes.length(); i++) {

            /** Traversing all routes */
           // for(int i=0;i<jRoutes.length();i++){
              //  jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
               // List path = new ArrayList<LinkedHashMap<String, String>>();

                /** Traversing all legs */
//                for(int j=0;j<jLegs.length();j++){
//                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");
//
//                    /** Traversing all steps */
//                    for(int k=0;k<jSteps.length();k++){
//                        String polyline = "";
//                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
//                        List<LatLng> list = decodePoly(polyline);
//
//                        /** Traversing all points */
//                        for(int l=0;l<list.size();l++){
//                            LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
//                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
//                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
//                            path.add(hm);
//                        }
//                    }
                   // routes.add(path);
               // }
           // }

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }catch (Exception e){
//        }
//
//        return routes;
    }
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        Log.d("TAG", "Directions JSON PAerser Before while");

        while (index < len) {
            Log.d("TAG", "Directions JSON PAerser");

            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        Log.d("TAG", poly.toString());

        return poly;
    }
}

