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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class LocationDetailsActivity extends Fragment{
static String TAG="tag";
    private static TextView place_name;
    private static TextView place_address;
    private static TextView place_addressHere;

    PickerAdapter adapter;



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
        ArrayList waypoint=MapsActivity.getWaypointwithDateList();
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
                MapsActivity.removeWaypoint(place_id);

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
                final String[] SelectedDate = new String[1];
               // DataHolderClass.getInstance().setDistributor_id(place_id);
                Calendar now = Calendar.getInstance();
//Select the day you wanna go on, set time ?
                //Set time now?
                final String[] SelectedTime = new String[1];
                final String[] StartSelectedTime = new String[1];

                final TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hour, int minute, int second) {
                            //    String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                            //    String SelectedDateText=dayOfMonth+" "+monthOfYear+" "+year;

                                String hourText;
                                String minuteText;
                                if( String.valueOf(hour).length()==1){

                                    hourText="0"+hour;
                                }else{
                                    hourText=Integer.toString(hour);
                                }
                                if( String.valueOf(minute).length()==1){

                                    minuteText="0"+minute;
                                }else{

                                    minuteText=Integer.toString(minute);

                                }

                                String SelectedTimeText=hourText+":"+minuteText;
                                SelectedTime[0] =SelectedTimeText;


                                adapter = new PickerAdapter(getFragmentManager());

                                try {
                                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                                    SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm");

                                    SimpleDateFormat datetimeformat = new SimpleDateFormat("dd-MM-yyyy hh:mm");

                                    //TODO: add one more for length of time ?
                                    Date selectedStarttime = datetimeformat.parse(SelectedDate[0]+" "+StartSelectedTime[0]);
                                    Date selectedtime = datetimeformat.parse(SelectedDate[0]+" "+SelectedTime[0]);


                                    Date selecteddate = dateformat.parse(SelectedDate[0]);
                                   // Date selectedStarttime = timeformat.parse(StartSelectedTime[0]);
                                   // Date selectedtime = timeformat.parse(SelectedTime[0]);
ArrayList selecteddata=new ArrayList();
                                //    selecteddata.add(selecteddate);
                                    selecteddata.add(place_id);

                                    selecteddata.add(selectedStarttime);
                                    selecteddata.add(selectedtime);

//TODO: see if checknig time or date it self is better
                                    Toast.makeText(getActivity(), "You have set time as "+selecteddate, Toast.LENGTH_SHORT).show();
                                    Log.d("qqqqqqqqqqqq", "ASD"+selecteddate+selectedStarttime+selectedtime);
                                    Log.d("qqqqqqqqqqqq", "ZXC"+selecteddata);

                                    if(selecteddata!=null){

                                        DataHolderClass.getInstance2().setDistributor_id2("unselected");
                                        MapsActivity.addWaypointwithDateList(selecteddata);



                                        FragmentManager fragmentManager = LocationDetailsActivity.this.getActivity().getSupportFragmentManager();
                                        if(fragmentManager.getBackStackEntryCount()>0) {
                                            fragmentManager.popBackStack();
                                        }
                                        Toast.makeText(getActivity(), "Location set....!", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getActivity(), place_id.toString(), Toast.LENGTH_SHORT).show();





                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //     dateTextView.setText(date);
                            }
                        },
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        false
                );

                final TimePickerDialog tpdStart = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hour, int minute, int second) {
                                //    String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                                //    String SelectedDateText=dayOfMonth+" "+monthOfYear+" "+year;

                                String hourText;
                                String minuteText;
                                if( String.valueOf(hour).length()==1){

                                    hourText="0"+hour;
                                }else{
                                    hourText=Integer.toString(hour);
                                }
                                if( String.valueOf(minute).length()==1){

                                    minuteText="0"+minute;
                                }else{

                                    minuteText=Integer.toString(minute);

                                }

                                String SelectedTimeText=" "+hourText+":"+minuteText;
                                tpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

                                StartSelectedTime[0] =SelectedTimeText;


                                adapter = new PickerAdapter(getFragmentManager());


                                //     dateTextView.setText(date);
                            }
                        },
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        false
                );





                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                String dayText;
                                String monthText;
                                if( String.valueOf(dayOfMonth).length()==1){

                                    dayText="0"+dayOfMonth;
                                }else{
                                    dayText=Integer.toString(dayOfMonth);
                                }
                                if( String.valueOf(monthOfYear).length()==1){

                                    monthText="0"+monthOfYear;
                                }else{

                                    monthText=Integer.toString(monthOfYear);

                                }


                                String SelectedDateText=dayText+"-"+monthText+"-"+year;
                                tpdStart.show(getActivity().getFragmentManager(), "Datepickerdialog");
                                SelectedDate[0] =SelectedDateText;
                           //     dateTextView.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );




                dpd.setTitle("Date for visiting");
                tpdStart.setTitle("Starting Time");
                tpd.setTitle("Ending Time");

                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");






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
                Log.d(TAG, "Some Error has occured, Plea" + e);

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


