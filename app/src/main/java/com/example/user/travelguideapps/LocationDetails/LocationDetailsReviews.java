package com.example.user.travelguideapps.LocationDetails;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.travelguideapps.DataParser;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationDetailsReviews.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationDetailsReviews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationDetailsReviews extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "locationDetailsReivew";

    // TODO: Rename and change types of parameters
    private static LinkedHashMap<String, String> placedetail;
    private String mParam2;
    private static TextView Reviews;
    private static TextView Author;

    private RecyclerView recyclerView;

private LocationDetailsReviewAdapter reviewAdapter;

    Button setplacebutton;
    Button removeplacebutton;
    private OnFragmentInteractionListener mListener;

    public LocationDetailsReviews() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
    // * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationDetailsReviews newInstance(List<LinkedHashMap<String, String>> placedetails, String param2) {
        LocationDetailsReviews fragment = new LocationDetailsReviews();
        placedetail=placedetails.get(0);

        Log.d("qqqqqqqqqqqq", "wellllll"+placedetail);

     //   Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
     //   args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
       //     mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_location_details_reviews, container, false);

  //      Reviews=(TextView)view.findViewById(R.id.textViewReviewsText);
       // Reviews.setText( placedetail.get("reviews"));



        String  review_text=placedetail.get("review_text");
        String  author_name=placedetail.get("review_author_name");
        String author_photo=placedetail.get("review_author_photo");
        String review_rating=placedetail.get("review_rating");
        String relative_time_description=placedetail.get("review_time_description");


        Log.d("Hasreviews placedetail", placedetail.toString());

        Log.d("Hasreviews review_text", review_text.toString());


        ArrayList<String> reviews_textlist = new ArrayList<String>(Arrays.asList(review_text.split("Separator")));
        ArrayList<String> author_namelist = new ArrayList<>(Arrays.asList(author_name.split(",")));

        ArrayList<String> author_photolist = new ArrayList<>(Arrays.asList(author_photo.split(",")));
        ArrayList<String> review_ratinglist = new ArrayList<>(Arrays.asList(review_rating.split(",")));
        ArrayList<String> relative_time_descriptionlist = new ArrayList<>(Arrays.asList(relative_time_description.split(",")));



        for(int i = 0; i < reviews_textlist.size(); i++) {
            if (reviews_textlist.get(i).startsWith(",")) {
                Log.d("Hasreviews ", "Uh huh, changed?????");

                reviews_textlist.set(i, reviews_textlist.get(i).replaceFirst(",", ""));
                Log.d("Hasreviews ", "Uh huh, changed");

            }
        }
        recyclerView=(RecyclerView)view.findViewById((R.id. recyclerViewReviews));


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager
                .VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        reviewAdapter = new LocationDetailsReviewAdapter(getActivity(),author_namelist,reviews_textlist,author_photolist,
                relative_time_descriptionlist,review_ratinglist);
        Log.d("Hasreviews ", author_namelist.toString());
        Log.d("Hasreviews ", reviews_textlist.toString());



        recyclerView.setAdapter(reviewAdapter);

        //View mainview=inflater.inflate(R.layout.content_place_details, container, false);

   //     setplacebutton=(Button) mainview.findViewById(R.id.addLocation);
     //   removeplacebutton=(Button) mainview.findViewById(R.id.removeLocation);

        return view ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {


        super.onAttach(context);



//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
            DataParser dataParser=new DataParser();





         //   pager = (ViewPager) view.findViewById(R.id.vpDetailsReviews);

            //TODO:has changed to child, use support if problem persist?
      //      viewpageradapter=new LocationDetailsActivity.MyPagerAdapter(getChildFragmentManager());
        //    pager.setAdapter(viewpageradapter);


//Problem caused : empty space in front of something..........



            Picasso.with(getActivity()).setLoggingEnabled(true);

            //place_address.setText( placedetail.get("formatted_address"));
       //     viewpageradapter.notifyDataSetChanged();

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
}
