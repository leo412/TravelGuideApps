package com.example.user.travelguideapps.LocationDetails;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LocationDetailsActivity extends Fragment{
static String TAG="LocationDetailsActivity";
    private static TextView place_name;
    private static TextView place_address;
    private static TextView place_addressHere;

    PickerAdapter adapter;



    private ArrayList<HashMap<String, Object>> placedetails = null;
    private View place_location;
    private LocationDetailsPictureAdapter pictureadapter;
    private RecyclerView recyclerView;
    private ViewPager pager;
    private FragmentStatePagerAdapter viewpageradapter;
    public static Button setplacebutton;
    public static Button removeplacebutton;

    public static  TextView selectedStartTimeText;
    public static  TextView selectedStartTime;
    public static  TextView selectedEndTimeText;
    public static  TextView selectedEndTime;
    public static  TextView timetext;
    public static  Button selectdatebutton;



    //CUrrently not using, see if needed anyime
 //   private LocationDetailsPictureAdapter LocationDetailsPictureAdapter;
//View content_place_details = findViewById(R.id.content_place_details);
    private CoordinatorLayout view;
  //  private ConstraintLayout detailsview;
    private ConstraintLayout timeview;

    BaseActivity b=new BaseActivity();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (CoordinatorLayout) inflater.inflate(R.layout.activity_place_details, container, false);
        timeview= (ConstraintLayout) inflater.inflate(R.layout.fragment_location_time_selected, container, false);

        place_location=(TextView)view.findViewById(R.id.textViewAdress);
        place_name=(TextView)view.findViewById(R.id.textViewPlaceName);



        recyclerView=(RecyclerView)view.findViewById((R.id.recycleviewphoto));

//everytime get here will use this place id hmmmm.
        //TODO: problem here cus it keeps using the same id........ when trying to go back to other popbackstack
        String _data = DataHolderClass.getInstance().getDistributor_id();
        String place_id_array = DataHolderClass.getInstancearray().getDistributor_idarray();

        removeplacebutton=(Button) view.findViewById(R.id.removeLocation);
        DataHolderClass.getInstance().setDistributor_id(null);
//Changes here...
        final String place_id = place_id_array;


        //Toast.makeText(getApplicationContext(),place_id, Toast.LENGTH_SHORT).show();
        //How to use this url?
        //"https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key=AIzaSyBSjyuGzRqI8WZ3Vdil99Dp2sTfwQI2-to"
        MapsActivity a=new MapsActivity();
        String takethistotest="https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key" +
                "=AIzaSyC4IFgnQ2J8xpbC2DmR6fIvrS5JIQV5vkA";
        Log.d(TAG, "fortestingurl"+takethistotest);
        selectedStartTime=(TextView) timeview.findViewById(R.id.SelectedDateText);
        selectedEndTime=(TextView) timeview.findViewById(R.id.timeduration);



        setplacebutton=(Button) view.findViewById(R.id.addLocation);
        ArrayList<HashMap> waypoint=MapsActivity.getWaypointwithDateList();
        Log.d(TAG, "ahahahshcn"+waypoint);
        for(int i=0;i<waypoint.size();i++){
            if (waypoint.get(i).get("place_id").equals(place_id)) {

                removeplacebutton.setVisibility(View.VISIBLE);

                if(waypoint.get(i).size()!=1) {
                    Log.d(TAG, "has this run????"+waypoint);

                  //  selectedStartTimeText.setVisibility(View.VISIBLE);
                    selectedStartTime.setVisibility(View.VISIBLE);
                    //selectedEndTimeText.setVisibility(View.VISIBLE);
                    selectedEndTime.setVisibility(View.VISIBLE);

                    //selectedStartTime.setText(waypoint.get(i).get(1).toString());
                   // selectedduration.setText(waypoint.get(i).get(2).toString());
                   // Log.d(TAG, "has this run????"+selectedduration.getText());
                  //  Log.d(TAG, "has this run????"+selectedduration.getVisibility());

                //    timetext.setVisibility(View.GONE);

                }



                setplacebutton.setVisibility(View.GONE);
                break;
            }
        }
        try {
            DataHolderClass.getInstance().setDistributor_id(place_id);

            new DownloadTask().execute(("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key" +
                    "=AIzaSyC4IFgnQ2J8xpbC2DmR6fIvrS5JIQV5vkA"));
        } catch (Exception e) {
            e.printStackTrace();
        }



        removeplacebutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                DataHolderClass.getInstance2().setDistributor_id2("isselected");

                DataHolderClass.getInstance().setDistributor_id(place_id);
                FragmentManager fragmentManager = LocationDetailsActivity.this.getActivity().getSupportFragmentManager();
                MapsActivity.removeWaypoint(place_id);
        //        DataHolderClass.getInstancearray().removeDistributor_idarray();

                if(fragmentManager.getBackStackEntryCount()>0) {
                    Log.d(TAG, "popbackrunning");
                    fragmentManager.popBackStack();
                    Log.d(TAG, "popbackrunning2");

                }
                Toast.makeText(getActivity().getApplicationContext(), "Location removed....!", Toast.LENGTH_SHORT).show();
            }
        });
        setplacebutton.setEnabled(false);
        removeplacebutton.setEnabled(false);

        setplacebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                try {
                                    HashMap selecteddata = new HashMap();
                                    //TODO: Timestamp confirmation? Not Selected?
                                    selecteddata.put("place_id",place_id);
                                    selecteddata.put("starttime",0L);
                                    selecteddata.put("duration",0L);


                                    if (selecteddata != null) {

                                        DataHolderClass.getInstance2().setDistributor_id2("unselected");
                                        MapsActivity.addWaypointwithDateList(selecteddata);

                                  //      DataHolderClass.getInstancearray().removeDistributor_idarray();

                                        FragmentManager fragmentManager = LocationDetailsActivity.this.getActivity().getSupportFragmentManager();
                                        if (fragmentManager.getBackStackEntryCount() > 0) {

                                            fragmentManager.popBackStack();
                                        }
                                        Toast.makeText(getActivity().getApplicationContext(), "Location Added!!", Toast.LENGTH_SHORT).show();


                                    }
                                }catch (Exception e){

                                    Toast.makeText(getActivity().getApplicationContext(), "Some Error has occured, please try again later!", Toast.LENGTH_SHORT).show();
                                    Log.d("LocationDetailsTPicker", e.toString());


                                }

//TODO: See if needed to add the dialog box back in....?
//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                builder.setMessage("Do you want to set the time now?").setPositiveButton("Yes", dialogClickListener)
//                        .setNegativeButton("No", dialogClickListener).show();
       //         newFragment.setArguments(args);






            }
        });






return view;



    }

    private class PickerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 2;
        Fragment timePickerFragment;
        Fragment datePickerFragment;

        PickerAdapter(FragmentManager fm) {
            super(fm);
            timePickerFragment = new Fragment();
            datePickerFragment = new Fragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return timePickerFragment;
                case 1:
                default:
                    return datePickerFragment;
            }
        }

        int getTitle(int position) {
            switch(position) {
                case 0:
                    return R.string.tab_title_time;
                case 1:
                default:
                    return R.string.tab_title_date;
            }
        }
    }
//        @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_place_details);
//      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//
//    }

    @Override
    public void onDetach() {
        super.onDetach();



        DataHolderClass.getInstancearray().removeDistributor_idarray();


    }

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
            Log.d(TAG, "runningdataparser " + " before details");

            DataParser dataParser=new DataParser();
        //Comment for single list to parse result
          placedetails=  dataParser.parse(result);

            //This is needed to insert data into the page it self
            //Name places list ?
            //TODO: content_place_details: Create Content View to insert the data
            //Change List of placedetails back to normal

            try {
                HashMap<String, Object> placedetail = placedetails.get(0);
                Log.d("DetailsResult", placedetail.toString());

                place_name.setText( placedetail.get("place_name").toString());


                HashMap<String,Object>test= placedetails.get(0);

                Log.d("Followthisshit", placedetails.toString());

                //      String photo=placedetails.get(0).get(2).toString();
                if(test.containsKey("photo_reference"))
                {

                    Log.d("Has photoreference ", test.toString());


                }

                String  photo=test.get("photo_reference").toString();


                List<String> photoList = new ArrayList<String>(Arrays.asList(photo.split(",")));




                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager
                        .HORIZONTAL,false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);



                pictureadapter = new LocationDetailsPictureAdapter( photoList);

                recyclerView.setAdapter(pictureadapter);

                pager = (ViewPager) view.findViewById(R.id.vpDetailsReviews);
                if(!isAdded()) {
                    return;
                }



                //TODO:has changed to child, use support if problem persist?
                viewpageradapter=new MyPagerAdapter(getChildFragmentManager());
                pager.setAdapter(viewpageradapter);
                int choice=0;
                if(DataHolderClass.getInstance2().getDistributor_id2()=="Details"){

                    choice=0;
                }else if(DataHolderClass.getInstance2().getDistributor_id2()=="Time"){
                    choice=2;


                }
                pager.setCurrentItem(choice);
                viewpageradapter.notifyDataSetChanged();
                TabLayout tabLayout= (TabLayout) view.findViewById(R.id.pager_header);
                tabLayout.setupWithViewPager(pager);
                //TODO: set adapter somtimes :Android IllegalStateException: Fragment not attached to Activity webview (isadded used to see if problem
                tabLayout.getTabAt(0).setIcon(R.drawable.detailsicon);
                tabLayout.getTabAt(1).setIcon(R.drawable.reviewsicon);
                tabLayout.getTabAt(2).setIcon(R.drawable.timewhiteicon);
              //  Toast.makeText(getActivity().getApplicationContext(), "Successfully HA!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "LocationDetailsactivity successfully");

                Picasso.with(getActivity()).setLoggingEnabled(true);

                //place_address.setText( placedetail.get("formatted_address"));

            }catch (Exception e){
                //TODO:Fragment no longer exists for key f1: index 0 (Sometimes got this ... ignore?
                Log.d(TAG, "Some Error has occured, Plea" + e);

           //     Toast.makeText(getActivity().getApplicationContext(), "Some Error has occured, Please try again!", Toast.LENGTH_SHORT).show();

            }

   //         Log.d(TAG, "Dqqqqqqqqq"+place_address.getText());

        }



}


    @Override
    public void onResume() {
        super.onResume();
if(viewpageradapter!=null) {
    TabLayout tabLayout= (TabLayout) view.findViewById(R.id.pager_header);
    tabLayout.setupWithViewPager(pager);
    viewpageradapter.notifyDataSetChanged();
    Log.d(TAG, "LocationDetailsactivity successfully inside onresume"+placedetails);
    tabLayout.getTabAt(0).setIcon(R.drawable.detailsicon);
    tabLayout.getTabAt(1).setIcon(R.drawable.reviewsicon);
    tabLayout.getTabAt(2).setIcon(R.drawable.timewhiteicon);
}


    }

    public static HashMap<String, String> toMap(JSONObject object) throws JSONException {
        HashMap<String, String> map = new HashMap<String, String>();

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





    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int pos) {

            Log.d(TAG, "locationdetailsgetitem"+placedetails);

            switch(pos) {

                case 0: return LocationDetails.newInstance(placedetails,"Ok yupe");
                case 1: return LocationDetailsReviews.newInstance(placedetails," Instance 1");
                case 2: return Time_Selection_Fragment.newInstance(placedetails,"ASd");
                //       case 3: return ThirdFragment.newInstance("ThirdFragment, Instance 2");
                //     case 4: return ThirdFragment.newInstance("ThirdFragment, Instance 3");
                default: return LocationDetails.newInstance(placedetails,"Ok yupe");


            }


        }


        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            Log.d(TAG, "isthiscall....1");

            if (position == 0) {
                title = "Details";
            } else if (position == 1) {
                title = "Reviews";
            }else if (position == 2) {
                title = "Time Slot";
            }

            return title;
        }
        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(Object object){



            return POSITION_NONE;
        }


    }
}
/////


