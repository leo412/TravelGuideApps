package com.example.user.travelguideapps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
//USELESS CAN ONLY BE USED IN LISTVIEW
/**
 * Created by User on 3/18/2017.
 */
//Put items into listview
public class LocationListViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<LinkedHashMap<String,String>> mDataSource;

    public LocationListViewAdapter(Context context, List<LinkedHashMap<String,String>> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_row, parent, false);
// Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.title);

// Get subtitle element
        TextView vicinityTextView =
                (TextView) rowView.findViewById(R.id.vicinity);

// Get detail element
        //TODO:Remove rating when not needed. or change to other String (Need it for "No rating")?
        TextView ratingTextView =
                (TextView) rowView.findViewById(R.id.rating);
        RatingBar ratingBar=(RatingBar) rowView.findViewById((R.id.ratingBar));
        TextView latTextView =
                (TextView) rowView.findViewById(R.id.lat);
        TextView lngTextView =
                (TextView) rowView.findViewById(R.id.lng);

        TextView place_idTextView =
                (TextView) rowView.findViewById(R.id.place_id);
// Get thumbnail element
//TOdo, check why "GetView cannot find its usages

//TODO: Add hidden View to get Lat, lng for the "Onclickitemlistener" in maps activity
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.list_image);

        Button button=(Button)rowView.findViewById(R.id.details_btn);
//ToDO:Set button to go to  LocationDetails
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO: Move data to there
                View parentRow = (View) v.getParent();

                ListView listView = (ListView) parentRow.getParent();

                final int position = listView.getPositionForView(parentRow);

                Intent intent = new Intent(mContext, LocationDetailsActivity.class);
               // EditText editText = (EditText) findViewById(R.id.editText);

                //String checking=(String) v.findViewById(R.id.lat).toString();
                //TODO:This is so wrong
                //Is this it??
                TextView place_id=  (TextView) parentRow.findViewById(R.id.place_id);
                String place_id_text= place_id.getText().toString();



            //    String place_id = ((TextView) v.findViewById(R.id.place_id)).getText().toString();

              //  String place_id=listView.getItemAtPosition(position).toString();


               String message = getItem(position).toString();
                Log.d("bbbbbbbb ", place_id_text);
                //Send place id to placeDretails Activity
                //   intent.putExtra("place_id", place_id_text);
             //   mContext.startActivity(intent);



                Fragment fragment = null;
                Class fragmentClass=null;
                fragmentClass = LocationDetailsActivity.class;
                try {

                    Bundle args = new Bundle();
                    args.putString("place_id",place_id_text);
              //      LocationDetailsActivity newFragment = new LocationDetailsActivity ();
                    fragment = (Fragment) fragmentClass.newInstance();
                    fragment.setArguments(args);

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();

            }
        });



String singlephotoreference=mDataSource.get(position).get("photo_reference").toString();


//Get first pictuer
        List<String> myList = new ArrayList<String>(Arrays.asList(singlephotoreference.split(",")));

//Remove the bracket... for some reason
        String list = Arrays.toString(myList.toArray()).replace("[", "").replace("]", "");




        //Log.d("ADAPTER", mDataSource.get(position).get("photo_reference").toString() );
    Picasso.with(mContext).load("https://maps.googleapis.com/maps/api/place/photo?photoreference="+list
            +"&sensor=false&maxheight=1000&maxwidth=1000&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().into(thumbnailImageView);
if(mDataSource.get(position).get("rating").toString()!="No Rating") {
    ratingBar.setRating(Float.parseFloat(mDataSource.get(position).get("rating").toString()));
}
        titleTextView.setText(mDataSource.get(position).get("place_name").toString());
        vicinityTextView.setText(mDataSource.get(position).get("vicinity").toString());
        place_idTextView.setText(mDataSource.get(position).get("place_id").toString());

        ratingTextView.setText(mDataSource.get(position).get("rating").toString());
        latTextView.setText(mDataSource.get(position).get("lat").toString());
        lngTextView.setText(mDataSource.get(position).get("lng").toString());

        return rowView;
    }
}