package com.example.user.travelguideapps;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class LocationDetailsActivity extends Fragment{
static String TAG="tag";
    private static TextView place_name;
    private View place_location;
    private LocationDetailsAdapter pictureadapter;
    private RecyclerView recyclerView;
    //CUrrently not using, see if needed anyime
 //   private LocationDetailsAdapter LocationDetailsAdapter;
//View content_place_details = findViewById(R.id.content_place_details);
    private CoordinatorLayout view;
    BaseActivity b=new BaseActivity();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (CoordinatorLayout) inflater.inflate(R.layout.activity_place_details, container, false);

        //   setSupportActionBar(toolbar);
        place_location=(TextView)view.findViewById(R.id.textViewAdress);
        place_name=(TextView)view.findViewById(R.id.textViewPlaceName);
        //  imageView1=(ImageView)findViewById(R.id.imageView1);
        //imageView2=(ImageView)findViewById(R.id.imageView2);
        recyclerView=(RecyclerView)view.findViewById((R.id.recycleviewphoto));

        //TODO: is this placeid correct??? Seems to have a lot of problem in it?
        //final String place_id=getIntent().getStringExtra("place_id");

     //   Bundle bundle = this.getArguments();

        String _data = DataHolderClass.getInstance().getDistributor_id();
        Button removeplacebutton=(Button) view.findViewById(R.id.removeLocation);

        final String place_id = _data;


        //Toast.makeText(getApplicationContext(),place_id, Toast.LENGTH_SHORT).show();
        //How to use this url?
        //"https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key=AIzaSyBSjyuGzRqI8WZ3Vdil99Dp2sTfwQI2-to"
        String data="ASD";
        MapsActivity a=new MapsActivity();
        String takethistotest="https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key" +
                "=AIzaSyC4IFgnQ2J8xpbC2DmR6fIvrS5JIQV5vkA";
        Log.d(TAG, "fortestingurl"+takethistotest);

        try {

            new DownloadTask().execute(("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key" +
                    "=AIzaSyC4IFgnQ2J8xpbC2DmR6fIvrS5JIQV5vkA"));
        } catch (Exception e) {
            e.printStackTrace();
        }





        Button setplacebutton=(Button) view.findViewById(R.id.addLocation);
        ArrayList waypoint=MapsActivity.getWaypoint();
        if(waypoint.contains(place_id)){

            removeplacebutton.setVisibility(View.VISIBLE);
            setplacebutton.setVisibility(View.GONE);

        }


        removeplacebutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DataHolderClass.getInstance2().setDistributor_id2("isselected");

                DataHolderClass.getInstance().setDistributor_id(place_id);
                FragmentManager fragmentManager = LocationDetailsActivity.this.getActivity().getSupportFragmentManager();
                if(fragmentManager.getBackStackEntryCount()>0) {
                    Log.d(TAG, "popbackrunning");

                    fragmentManager.popBackStack();
                    Log.d(TAG, "popbackrunning2");


                }
                Toast.makeText(getActivity(), "Location removed....!", Toast.LENGTH_SHORT).show();



            }
        });

        setplacebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     Log.d(TAG, "uhhhhhhhzero"+b.getPlace_id());

             //   ArrayList place_idarray=null;
//if(b.getPlace_id()!=null) {
//    //Getplaceid contain an array,
//    place_idarray = b.getPlace_id();
//    Log.d(TAG, "uhhhhhhhfirst"+place_idarray);
//
//    place_idarray.add(place_id);
//    b.setPlace_id(place_idarray);
//    Log.d(TAG, "uhhhhhhhfirst"+place_idarray);
//
//}else{
//    ArrayList test=new ArrayList();
//    test.add(place_id);
//b.setPlace_id(test);
//    Log.d(TAG, "uhhhhhhhsecond");
//
//
//
//}
////2 choice, check if selected or in waypoint?
////TODO: if selected, changed the button...(VIsibility )
//
//                Log.d(TAG, "uhhhhhhh"+b.getPlace_id());


//                Fragment newFragment = null;
//
//                Class fragmentClass=null;
//                fragmentClass = MapsActivity.class;

//                try {
//                    Log.d(TAG, "sendatafromsuccess");
//
//                    newFragment = (Fragment) fragmentClass.newInstance();
//                } catch (java.lang.InstantiationException e) {
//                    Log.d(TAG, "sendatafrom"+e);
//
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    Log.d(TAG, "sendatafrom"+e);
//
//                    e.printStackTrace();
//                }
       //         newFragment.setArguments(args);
                DataHolderClass.getInstance().setDistributor_id(place_id);
                DataHolderClass.getInstance2().setDistributor_id2("unselected");

                FragmentManager fragmentManager = LocationDetailsActivity.this.getActivity().getSupportFragmentManager();
                if(fragmentManager.getBackStackEntryCount()>0) {
                    Log.d(TAG, "popbackrunning");

                    fragmentManager.popBackStack();
                    Log.d(TAG, "popbackrunning2");


                }
                Toast.makeText(getActivity(), "Location set....!", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), place_id.toString(), Toast.LENGTH_SHORT).show();







            }
        });
return view;
    }

//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_place_details);
//      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//
//    }
    public class DownloadTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            //set message of the dialog
            //show dialog

            super.onPreExecute();
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            Log.d("qqqqqqqqqqqq", data);

            return data;
        }


 //This is commented because its not known whether its needed
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject jsonObj=null;
            JSONArray jsonResult=null;
            JSONObject jsonObjectItem=null;
            String placename="ERROR PLACE NOT FOUND";
            //Click Marker and find path? Probably this is
            Log.d("DetailsResult", result);

            //NOTE: Is using "List" eventhough placedetails is singular to allow dataparser to work without needing another method
            List<LinkedHashMap<String, String>> placedetails = null;

            JSONArray jsonArray = null;
            JSONObject jsonObject;
            DataParser dataParser=new DataParser();
        //Comment for single list to parse result
          placedetails=  dataParser.parse(result);

            //This is needed to insert data into the page it self
            //Name places list ?
            //TODO: content_place_details: Create Content View to insert the data
            //Change List of placedetails back to normal
            LinkedHashMap<String,String> placedetail=placedetails.get(0);

            place_name.setText( placedetail.get("place_name"));




// TODO: Commented and used hardcode, check if able to soft code
//int photoindex=placedetails.indexOf("photo_reference");
            //Doing this
            LinkedHashMap<String,String>test= placedetails.get(0);

            Log.d("Followthisshit", placedetails.toString());

            Log.d("Followthisshit", Integer.toString(2));

      //      String photo=placedetails.get(0).get(2).toString();
if(test.containsKey("photo_reference"))
{

    Log.d("YesBitches", test.toString());


}

          String  photo=test.get("photo_reference");


            List<String> photoList = new ArrayList<String>(Arrays.asList(photo.split(",")));

for(int i=0;i<photoList.size();i++){



}


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager
                    .HORIZONTAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);

            pictureadapter = new LocationDetailsAdapter(getActivity(), photoList);

            recyclerView.setAdapter(pictureadapter);





//Problem caused : empty space in front of something..........



            Picasso.with(getActivity()).setLoggingEnabled(true);



        }



}
    public static LinkedHashMap<String, String> toMap(JSONObject object) throws JSONException {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value.toString());
        }
        return map;
    }
    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
    //TODO: Maps activity has this too, try and see if it can be use from there.
    private static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        Log.d(TAG, "Newdirectionurl" + strUrl);


        try {

            URL url = new URL(strUrl);

            Log.d(TAG, "Download URL");
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d(TAG, "Newdirectionurl" + data);

            br.close();

        } catch (Exception e) {
            Log.d("newexceptionurl", e.toString());
        } finally {
           // iStream.close();
//            urlConnection.disconnect();
        }
        return data;
    }
}
/////


