package com.example.user.travelguideapps.LocationDetails;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.DataParser;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.Location_RecyclerView;
import com.example.user.travelguideapps.R;
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
    private static TextView place_address;
    private static TextView place_addressHere;
    private List<LinkedHashMap<String, String>> placedetails = null;
    private View place_location;
    private LocationDetailsPictureAdapter pictureadapter;
    private RecyclerView recyclerView;
    private ViewPager pager;
    private FragmentPagerAdapter viewpageradapter;
    public static Button setplacebutton;
    public static Button removeplacebutton;



    //CUrrently not using, see if needed anyime
 //   private LocationDetailsPictureAdapter LocationDetailsPictureAdapter;
//View content_place_details = findViewById(R.id.content_place_details);
    private CoordinatorLayout view;
    private ConstraintLayout detailsview;

    BaseActivity b=new BaseActivity();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (CoordinatorLayout) inflater.inflate(R.layout.activity_place_details, container, false);
        detailsview= (ConstraintLayout) inflater.inflate(R.layout.fragment_location_details, container, false);
        //   setSupportActionBar(toolbar);
        place_location=(TextView)view.findViewById(R.id.textViewAdress);
        place_name=(TextView)view.findViewById(R.id.textViewPlaceName);
      //  place_address=(TextView)detailsview.findViewById(R.id.textViewAdress);
      //  place_addressHere=(TextView)view.findViewById(R.id.textViewAdress);
      //  Log.d(TAG, "Dqqqqqqqqq"+place_address.getText());


        //  imageView1=(ImageView)findViewById(R.id.imageView1);
        //imageView2=(ImageView)findViewById(R.id.imageView2);
        recyclerView=(RecyclerView)view.findViewById((R.id.recycleviewphoto));

        //TODO: is this placeid correct??? Seems to have a lot of problem in it?
        //final String place_id=getIntent().getStringExtra("place_id");

     //   Bundle bundle = this.getArguments();

        String _data = DataHolderClass.getInstance().getDistributor_id();
        removeplacebutton=(Button) view.findViewById(R.id.removeLocation);
        DataHolderClass.getInstance().setDistributor_id(null);

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





      setplacebutton=(Button) view.findViewById(R.id.addLocation);
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
        setplacebutton.setEnabled(false);
        removeplacebutton.setEnabled(false);

        setplacebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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


            JSONArray jsonArray = null;
            JSONObject jsonObject;
            DataParser dataParser=new DataParser();
        //Comment for single list to parse result
          placedetails=  dataParser.parse(result);

            //This is needed to insert data into the page it self
            //Name places list ?
            //TODO: content_place_details: Create Content View to insert the data
            //Change List of placedetails back to normal

            try {
                LinkedHashMap<String, String> placedetail = placedetails.get(0);
                Log.d("DetailsResult", placedetail.toString());

                place_name.setText( placedetail.get("place_name"));
                //        place_addressHere.setText( placedetail.get("formatted_address"));




// TODO: Commented and used hardcode, check if able to soft code
//int photoindex=placedetails.indexOf("photo_reference");
                //Doing this
                LinkedHashMap<String,String>test= placedetails.get(0);

                Log.d("Followthisshit", placedetails.toString());

                Log.d("Followthisshit", Integer.toString(2));

                //      String photo=placedetails.get(0).get(2).toString();
                if(test.containsKey("photo_reference"))
                {

                    Log.d("Has photoreference ", test.toString());


                }

                String  photo=test.get("photo_reference");


                List<String> photoList = new ArrayList<String>(Arrays.asList(photo.split(",")));




                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager
                        .HORIZONTAL,false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);



                pictureadapter = new LocationDetailsPictureAdapter(getActivity(), photoList);

                recyclerView.setAdapter(pictureadapter);

                pager = (ViewPager) view.findViewById(R.id.vpDetailsReviews);
                if(!isAdded()) {
                    return;
                }
                //TODO: set adapter somtimes :Android IllegalStateException: Fragment not attached to Activity webview (isadded used to see if problem

                //TODO:has changed to child, use support if problem persist?
                viewpageradapter=new MyPagerAdapter(getChildFragmentManager());
                pager.setAdapter(viewpageradapter);



//Problem caused : empty space in front of something..........



                Picasso.with(getActivity()).setLoggingEnabled(true);

                //place_address.setText( placedetail.get("formatted_address"));
                viewpageradapter.notifyDataSetChanged();

            }catch (Exception e){

                Toast.makeText(getActivity(), "Some Error has occured, Please try again!", Toast.LENGTH_SHORT).show();

            }

   //         Log.d(TAG, "Dqqqqqqqqq"+place_address.getText());

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
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int pos) {

      //      Log.d(TAG, "Dujjjjjjjcalled 2nd time....1"+place_address.getText());

            switch(pos) {

                case 0: return LocationDetails.newInstance(placedetails,"Ok yupe");
                case 1: return LocationDetailsReviews.newInstance(placedetails," Instance 1");
                //   case 2: return ThirdFragment.newInstance("ThirdFragment, Instance 1");
                //       case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
                //     case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default: return Location_RecyclerView.newInstance("FirstFragmen","Ok yupe");


            }



        }


        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0)
            {
                title = "Details";
            }
            else if (position == 1)
            {
                title = "Reviews";
            }

            return title;
        }
        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(Object object){

            Log.d(TAG, "DujjjjjjjRefresh....1");

            Log.d(TAG, "DujjjjjjjRefresh....2");

            return POSITION_NONE;
        }


    }
}
/////


