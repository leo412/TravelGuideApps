package com.example.user.travelguideapps;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by User on 6/2/2017.
 */
//TODO: most probably this is not needed, change if only needed .... called from LocationRecyclerVIew1/2
public class SelectedLocationListRecyclerViewAdapter extends RecyclerView.Adapter<SelectedLocationListRecyclerViewAdapter.MyViewHolder> implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "SelectedLocation";
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<LinkedHashMap<String,String>> mDataSource;
    public SelectedLocationListRecyclerViewAdapter(Context context, ArrayList<LinkedHashMap<String, String>> items) {
        mContext = context;
            Log.d("A", "DujjjjjjjNumberofitems(Selected)"+items);

        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);




    }
    //1


    //2
  //  @Override
    //public Object getItem(int position) {
   //     return mDataSource.get(position);
   // }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }





    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("A", "DujjjjjjjViewholder");

        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder







        return vh;
    }
    int    globalPosition=-1;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // set the data in items

//TODO:Either use mGOoglApiClient or use the URL way





      String place_id = mDataSource.get(position).toString();



        new DownloadTask().execute(("https://maps.googleapis.com/maps/api/place/details/json?placeid="+place_id+"&key" +
                "=AIzaSyC4IFgnQ2J8xpbC2DmR6fIvrS5JIQV5vkA"));
        Log.d("what is mdata", mDataSource.toString());






        String singlephotoreference=mDataSource.get(position).get("photo_reference").toString();
//Well this helps when comning back from other page sooo....
        if(position==globalPosition)
        {
            //change color like
holder.itemView.setSelected(true);


        }
        else
        {
            //revert back to regular color
            holder.itemView.setSelected(false);
        }

        ArrayList waypoint= MapsActivity.getWaypoint();
        if(waypoint.contains(mDataSource.get(position).get("place_id"))){

            holder.Selected.setVisibility(View.VISIBLE);

        }else{
            holder.Selected.setVisibility(View.INVISIBLE);


        }
        List<String> myList = new ArrayList<String>(Arrays.asList(singlephotoreference.split(",")));

        String list = Arrays.toString(myList.toArray()).replace("[", "").replace("]", "");

        holder.lat.setText(mDataSource.get(position).get("lat").toString());
        holder.lng.setText(mDataSource.get(position).get("lng").toString());


        holder.vicinity.setText(mDataSource.get(position).get("vicinity").toString());

        holder.place_id.setText(mDataSource.get(position).get("place_id").toString());

            if(mDataSource.get(position).get("rating").toString()!="No Rating") {
                holder.ratingBar.setRating(Float.parseFloat(mDataSource.get(position).get("rating").toString()));
           }
int add=position+1;
        holder.name.setText(add+") "+mDataSource.get(position).get("place_name").toString());
     //   holder.image.setImageResource("https://maps.googleapis.com/maps/api/place/photo?photoreference="+list
          //      +"&sensor=false&maxheight=1000&maxwidth=1000&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().into(thumbnailImageView);

        Picasso.with(mContext).load("https://maps.googleapis.com/maps/api/place/photo?photoreference="+list
                +"&sensor=false&maxheight=1000&maxwidth=1000&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().into(holder.image);


        // implement setOnClickListener event on item view.
        final Button b = (Button) holder.itemView.findViewById(R.id.details_btn);
        b.setVisibility(View.VISIBLE);

        final String lat=holder.lat.getText().toString();
        final String lng=holder.lng.getText().toString();


        holder.itemView.setOnClickListener(new View.OnClickListener() {

                                               @Override
                                               public void onClick(View view) {
                                           //         MapsActivity m = new MapsActivity();
                                                   //Allow single selection hack,...
                                                   notifyDataSetChanged();
                                                   //TODO: uhhhh this one is not really that important anyway....
                                                //   b.setVisibility(View.VISIBLE);

                                                   ArrayList<Marker> mapMarkers= MapsActivity.getMapMarkers();

                                                   view.setSelected(true);

                                                   globalPosition=position;

                                                   //  Toast.makeText(getApplicationContext(), lat.toString(), Toast.LENGTH_SHORT).show();
                                                   LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                                   Marker result = null;
                                                   //For each marker in the map...where the lat equals to the lat provided by the  recycle view?
                                                   for (Marker c : mapMarkers) {

                                                       if (lat.equals(String.valueOf(c.getPosition().latitude)) && lng.equals(String.valueOf(c.getPosition().longitude))) {
//MapsActivity m= new MapsActivity();
                                                           MapsActivity.showPath(c);
                                                           result = c;
                                                           //This should not be here... showpath is already up there.
                                                           //       mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                                                           //      mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

                                                           break;
                                                       }
                                                   }

                                               }

                                           });








                // display a toast with person name on item click
              //  Toast.makeText(context, personNames.get(position), Toast.LENGTH_SHORT).show();






                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//TODO: Move data to there
                        View parentRow = (View) v.getParent();

                  //      RecyclerView listView = (RecyclerView) parentRow.getParent();

                 //       final int position = listView.getPositionForView(parentRow);

                   //     Intent intent = new Intent(mContext, LocationDetailsActivity.class);
                        // EditText editText = (EditText) findViewById(R.id.editText);

                        //String checking=(String) v.findViewById(R.id.lat).toString();
                        //TODO:This is so wrong
                        //Is this it??
                        TextView place_id=  (TextView) parentRow.findViewById(R.id.place_id);
                        String place_id_text= place_id.getText().toString();

                        //    String place_id = ((TextView) v.findViewById(R.id.place_id)).getText().toString();

                        //  String place_id=listView.getItemAtPosition(position).toString();

                        //Send place id to placeDretails Activity
                        //   intent.putExtra("place_id", place_id_text);
                        //   mContext.startActivity(intent);

                        Fragment fragment = null;
                        Class fragmentClass=null;
                        fragmentClass = LocationDetailsActivity.class;
                        try {
                            DataHolderClass.getInstance().setDistributor_id(place_id_text);

                            //      Bundle args = new Bundle();
                            //   args.putString("place_id",place_id_text);
                            //      LocationDetailsActivity newFragment = new LocationDetailsActivity ();
                            fragment = (Fragment) fragmentClass.newInstance();
                            //fragment.setArguments(args);

                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.flContent2, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();



                        //   fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();

                    }
                });



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
          //  LinkedHashMap<String,String> placedetail=placedetails.get(0);

           // place_name.setText( placedetail.get("place_name"));
// TODO: Commented and used hardcode, check if able to soft code
//int photoindex=placedetails.indexOf("photo_reference");
            //Doing this
//            LinkedHashMap<String,String>test= placedetails.get(0);
//
//            Log.d("Followthisshit", placedetails.toString());
//
//            Log.d("Followthisshit", Integer.toString(2));
//
//            //      String photo=placedetails.get(0).get(2).toString();
//            if(test.containsKey("photo_reference"))
//            {
//
//                Log.d("YesBitches", test.toString());
//
//
//            }
//            if(test.containsKey("place_id"))
//            {
//
//
//
//            }
//            String  photo=test.get("photo_reference");


         //   List<String> photoList = new ArrayList<String>(Arrays.asList(photo.split(",")));
//
//            for(int i=0;i<photoList.size();i++){
//
//
//
//            }


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext,LinearLayoutManager
                    .HORIZONTAL,false);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setHasFixedSize(true);
//
//            pictureadapter = new LocationDetailsPictureAdapter(getActivity(), photoList);
//
//            recyclerView.setAdapter(pictureadapter);





//Problem caused : empty space in front of something..........



    //        Picasso.with(getActivity()).setLoggingEnabled(true);



        }



    }

    //TODO: Maps activity has this too, try and see if it can be use from there.
    //NOTE: this will get data for a single place
    private static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        Log.d(TAG, "Newdirectionurl" + strUrl);


        try {

            URL url = new URL(strUrl);

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
            Log.d(TAG, "SelectedLocationList" + data);

            br.close();

        } catch (Exception e) {
            Log.d("newexceptionurl", e.toString());
        } finally {
            // iStream.close();
//            urlConnection.disconnect();
        }
        return data;
    }


    @Override
    public int getItemCount() {
        if(mDataSource!=null) {
            Log.d("A", "Dujjjjjjjgetitemcount" + mDataSource.size());

        return mDataSource.size();
        }
        return 0;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //NOTE:Add new items here to get
        // init the item view's
        TextView name;
        TextView vicinity;
        TextView rating;
        TextView place_id;
RatingBar ratingBar;
        TextView lat;
        TextView lng;

        TextView Selected;


        ImageView image;
        AdapterView.OnItemClickListener mItemClickListener;

        public MyViewHolder(final View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.list_image);
            place_id = (TextView) itemView.findViewById(R.id.place_id);
            vicinity = (TextView) itemView.findViewById(R.id.vicinity);
            ratingBar= (RatingBar) itemView.findViewById(R.id.ratingBar);
            lat = (TextView) itemView.findViewById(R.id.lat);
            lng = (TextView) itemView.findViewById(R.id.lng);
            Selected = (TextView) itemView.findViewById(R.id.selectedtext);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("check", "hhhhhhhhhhhchecklocationtecycler");


                v.setTag(getAdapterPosition());

                   // mItemClickListener.onItemClick(getLayoutPosition(), v, id);
                }
            });





        }
    }
}