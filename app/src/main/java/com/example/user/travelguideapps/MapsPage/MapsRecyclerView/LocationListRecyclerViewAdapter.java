package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.LocationDetails.LocationDetailsActivity;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.example.user.travelguideapps.MapsPage.MapsActivity.CurrentLocation;

/**
 * Created by User on 6/2/2017.
 */
public class LocationListRecyclerViewAdapter extends RecyclerView.Adapter<LocationListRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<LinkedHashMap<String, Object>> mDataSource;
    private static List<LinkedHashMap<String, Object>> mDataSourceforSend;
    private static View clickedView;
    private int Position;
    private static RecyclerView.ViewHolder holder2;

    public LocationListRecyclerViewAdapter(List<LinkedHashMap<String, Object>> items) {
        Log.d("A", "DujjjjjjjNumberofitems(List)" + items);
        mDataSourceforSend = items;
        mDataSource = items;
        Log.d("A", "DujjjjjjjNumberofitems(List)Upperitemcount" + mDataSourceforSend);

        //mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //Little hack again
    public void setSelectedItem()

    {

        if (clickedView != null) {
            Log.d("A", "stopthiscluster" + clickedView.isSelected());

            Log.d("A", "stopthiscluster" + clickedView);

            clickedView.setSelected(true);

            Log.d("A", "stopthiscluster" + clickedView.isSelected());

        }

    }

    public static List<LinkedHashMap<String, Object>> getItem() {
        return mDataSourceforSend;
    }

    public static RecyclerView.ViewHolder getHolder2() {
        return holder2;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("A", "DujjjjjjjViewholdertrying to find this ");

        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder


        return vh;
    }

    int globalPosition = -1;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // set the data in items
        final Context mContext = holder.itemView.getContext();
        Log.d("LocationListRecyc", "LocationListRecycOnBindviewholder");


        String singlephotoreference = mDataSource.get(position).get("photo_reference").toString();
        //Well this helps when comning back from other page sooo....so that i didnt reset to old one or some shit
        if (position == globalPosition) {
            Log.d("A", "Postitionchanges1" + holder.name.getText());

            //change color like
            holder.itemView.setSelected(true);

        } else {

            Log.d("A", "Postitionchanges2" + holder.name.getText());

            //revert back to regular color
            holder.itemView.setSelected(false);
        }

        ArrayList<HashMap> waypoint = MapsActivity.getWaypointwithDateList();
        Log.d("A", "nopeway" + waypoint);

        for (int i = 0; i < waypoint.size(); i++) {
            // Log.d("A", "nopeway" + waypoint.get(i).get(0));
            Log.d("A", "nopeway" + mDataSource.get(position).get("place_name"));


            if (waypoint.get(i).get("place_id").equals(mDataSource.get(position).get("place_id"))) {
                Log.d("A", "setas visible?" + waypoint);

                holder.selectedimage.setVisibility(View.VISIBLE);
                break;
            } else {
                Log.d("A", "setas gone?" + waypoint);

                holder.selectedimage.setVisibility(View.GONE);

            }

            //i had taken
        }

//check
        List<String> myList = new ArrayList<String>(Arrays.asList(singlephotoreference.split(",")));

        String list = Arrays.toString(myList.toArray()).replace("[", "").replace("]", "");

        holder.lat.setText(mDataSource.get(position).get("lat").toString());
        holder.lng.setText(mDataSource.get(position).get("lng").toString());


        holder.vicinity.setText(mDataSource.get(position).get("vicinity").toString());

        holder.place_id.setText(mDataSource.get(position).get("place_id").toString());

        if (mDataSource.get(position).get("rating").toString() != "No Rating") {
            holder.ratingBar.setRating(Float.parseFloat(mDataSource.get(position).get("rating").toString()));
        }
        //.....is this the one?
        int add = position + 1;
        holder.name.setText(add + ") " + mDataSource.get(position).get("place_name").toString());
        //   holder.image.setImageResource("https://maps.googleapis.com/maps/api/place/photo?photoreference="+list
        //      +"&sensor=false&maxheight=1000&maxwidth=1000&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().into(thumbnailImageView);
        //TODO: can change image to different type
        Log.d("A", "checkimagelocation" + list);
        final ImageButton b = (ImageButton) holder.itemView.findViewById(R.id.details_btn);
        final ImageButton directionsbutton = (ImageButton) holder.itemView.findViewById(R.id.directions_btn);
        final ImageButton googlemapsbutton = (ImageButton) holder.itemView.findViewById(R.id.google_details_btn);
        //  final ImageButton timebutton = (ImageButton) holder.itemView.findViewById(R.id.time_btn);
        try {
            //TODO Can take away circle transformation for memory iussue
            //Changed to noplaceholder..... TODO: PLS REMOVE PLACEHOLDER WORST CASE SCENARIO (WORST ANNOYING THINGS EVER (also circle transform
//            Picasso.with(mContext).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + list
//                    + "&sensor=false&maxheight=100&maxwidth=100&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().transform(new CircleTransform())
//                    .error(R.drawable.noimage)
//                    .placeholder(R.drawable.loading_gif).into
//                    (holder.image);
//

            Picasso.with(mContext).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + list
                    + "&sensor=false&maxheight=100&maxwidth=100&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit()
                    .error(R.drawable.noimage)
                    .placeholder(R.drawable.loading_gif).into
                    (holder.image);


            Picasso.with(mContext).load(R.drawable.googlemapsicon).fit().into
                    (googlemapsbutton);

            Picasso.with(mContext).load(R.drawable.viewdetails).fit().into
                    (b);
            Picasso.with(mContext).load(R.drawable.directionsicon).fit().into
                    (directionsbutton);
//            Picasso.with(mContext).load(R.drawable.timeicon).fit().into
//                    (timebutton);
        } catch (Exception e) {

            Toast.makeText(mContext, "An exception has occured (Picasso)" + e, Toast.LENGTH_SHORT).show();
        }
        // implement setOnClickListener event on item view.


        final String lat = holder.lat.getText().toString();
        final String lng = holder.lng.getText().toString();
        holder2 = holder;
        Log.d("A", "DujjjjjjjonbidViewholder" + holder2);
        b.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //         MapsActivity m = new MapsActivity();
                //Allow single selection hack,...
                notifyDataSetChanged();
                //TODO: uhhhh this one is not really that important anyway....
                //   b.setVisibility(View.VISIBLE);

                ArrayList<Marker> mapMarkers = MapsActivity.getMapMarkers();
                clickedView = view;

                //          clickedView.setSelected(true);
//setSelectedItem();
                Log.d("A", "Isthistunnable" + clickedView);
                Log.d("A", "Isthistunnable" + view);

                globalPosition = position;
                Log.d("check", "isview" + view.isSelected());

                //  Toast.makeText(getApplicationContext(), lat.toString(), Toast.LENGTH_SHORT).show();
                LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                Marker result = null;
                //For each marker in the map...where the lat equals to the lat provided by the  recycle view?
                for (Marker c : mapMarkers) {

                    if (lat.equals(String.valueOf(c.getPosition().latitude)) && lng.equals(String.valueOf(c.getPosition().longitude))) {
                        //If possible dont care about this now
                        //TODO: Seems to be unsolvable , need little hack but not now (Triggering marker onclick
                        String s = MapsActivity.showPath(c);
                        MapsActivity.downloadUrl download = new MapsActivity.downloadUrl(mContext);
                        download.execute(s);
                        Log.d("Checkruntimes", "Runningdownloadurl  1");

                        break;
                    }
                }

            }

        });
        directionsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = Double.toString(CurrentLocation.getLatitude());
                String lng = Double.toString(CurrentLocation.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + lat + "," + lng +
                        "&destination=A&destination_place_id=" + holder.place_id.getText()
                ));
                mContext.startActivity(intent);


            }
        });
        googlemapsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  String lat = Double.toString(CurrentLocation.getLatitude());
                // String lng = Double.toString(CurrentLocation.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google" +
                        ".com/maps/search/?api=1&query=" + lat + "," + lng + "&query_place_id=" + holder.place_id.getText()

                ));
                mContext.startActivity(intent);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO: Move data to there
                View parentRow = (View) v.getParent();
                //tODO:CHECK
                MapsActivity.pd.show();


                TextView place_id = (TextView) parentRow.findViewById(R.id.place_id);
                String place_id_text = place_id.getText().toString();

                Fragment fragment = null;
                Class fragmentClass = null;
                fragmentClass = LocationDetailsActivity.class;
                try {
                    // DataHolderClass.getInstance().setDistributor_id(place_id_text);
                    DataHolderClass.getInstancearray().setDistributor_idarray(place_id_text);
                    DataHolderClass.getInstance2().setDistributor_id2("Details");

                    fragment = (Fragment) fragmentClass.newInstance();

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContent2, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
//        timebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////TODO: Move data to there
//                View parentRow = (View) v.getParent();
//                MapsActivity.pd.show();
//
//
//                TextView place_id = (TextView) parentRow.findViewById(R.id.place_id);
//                String place_id_text = place_id.getText().toString();
//
//                Fragment fragment = null;
//                Class fragmentClass = null;
//                fragmentClass = LocationDetailsActivity.class;
//                try {
//                    // DataHolderClass.getInstance().setDistributor_id(place_id_text);
//                    DataHolderClass.getInstance2().setDistributor_id2("Time");
//
//
//                    DataHolderClass.getInstancearray().setDistributor_idarray(place_id_text);
//
//                    fragment = (Fragment) fragmentClass.newInstance();
//
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//
//                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.flContent2, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//
//
//            }
//        });
MapsActivity.pd.dismiss();
    }


    @Override
    public int getItemCount() {
        if (mDataSource != null) {
            Log.d("A", "DujjjjjjjNumberofitems(List)  Item COUNT" + mDataSource.size());

            return mDataSource.size();
        }
        Log.d("A", "DujjjjjjjNumberofitems(List)  Nope COUNT");

        return 0;
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
        ImageView selectedimage;
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
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            lat = (TextView) itemView.findViewById(R.id.lat);
            lng = (TextView) itemView.findViewById(R.id.lng);
            selectedimage = (ImageView) itemView.findViewById(R.id.selectedimage);
            Picasso.with(mContext).load(R.drawable.selected).fit().into
                    (selectedimage);

        }
    }


}