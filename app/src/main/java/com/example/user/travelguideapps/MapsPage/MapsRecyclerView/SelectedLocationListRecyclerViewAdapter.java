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
//TODO: Edit2 Yeeeppp this is probably needed, because they need different function in finding setchecked..
//TODO: most probably this is not needed, change if only needed .... called from LocationRecyclerVIew1/2
public class SelectedLocationListRecyclerViewAdapter extends RecyclerView.Adapter<SelectedLocationListRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private static List<LinkedHashMap<String, Object>> mDataSource;
    private static List<LinkedHashMap<String, Object>> mDataSourceforSend;

    private int Position;
    private static RecyclerView.ViewHolder holder2;

    private static ArrayList duration = new ArrayList();
    private static ArrayList distance = new ArrayList();


    public SelectedLocationListRecyclerViewAdapter(Context context, ArrayList<LinkedHashMap<String, Object>> items) {
        mContext = context;

        mDataSourceforSend = items;
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

    public static List<LinkedHashMap<String, Object>> getItem() {


        return mDataSourceforSend;
    }

    public static RecyclerView.ViewHolder getHolder2() {
        return holder2;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("A", "DujjjjjjjViewholder");
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_selected, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder


        return vh;
    }

    int globalPosition = -1;
    boolean completedloop = false;
    int previousposition=-1;
ArrayList previousarray=new ArrayList();

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // set the data in items
//new box(except of events cards
//        duration.clear();
//        distance.clear();



        Log.d("A", "testingforpositionrunnning fort positions" + previousposition+"><"+position);

        if(previousarray.contains(position)){
            //becomes false = don't run notify
            completedloop = true;
            Log.d("A", "testingforposition" + "TOPPPPPPPP"+distance);


        }else{
            //Run notify??
            Log.d("A", "testingforposition" + "BOTTTTTTOM"+distance);

            completedloop = false;

        }
        previousarray.add(position);
//
            previousposition   =position;
        Log.d("A", "testingforposition" + MapsActivity.getWayPointDetailsList().size());

        Log.d("A", "calculatethesize" + completedloop);
        Log.d("A", "calculatethesizewhatistheitem" + position);
//
        if (MapsActivity.getWayPointDetailsList().size() == mDataSource.size()) {
            //TODO: can add travel mode/avoid

            String place_id = mDataSource.get(position).toString();

            String singlephotoreference = mDataSource.get(position).get("photo_reference").toString();

            Log.d("A", "singlephotoreferenceahaha" + singlephotoreference);

//Well this helps when comning back from other page sooo....
            if (position == globalPosition) {

                //change color like
                holder.itemView.setSelected(true);


            } else {
                //revert back to regular color
                holder.itemView.setSelected(false);
            }

            ArrayList<HashMap> waypoint = MapsActivity.getWaypointwithDateList();

            for (int i = 0; i < waypoint.size(); i++) {

                Log.d("A", "selectedwaypointcheck" + waypoint.get(i).get("place_id"));
                Log.d("A", "selectedwaypointcheck" +mDataSource.get(position).get("place_id"));

                if (waypoint.get(i).get("place_id").equals(mDataSource.get(position).get("place_id"))) {

                    holder.selectedimage.setVisibility(View.VISIBLE);

                } else {
                    //Well no point using this...
                    //holder.Selected.setVisibility(View.INVISIBLE);


                }


            }
            List<String> myList = new ArrayList<String>(Arrays.asList(singlephotoreference.split(",")));

            String list = myList.get(0).replace("[", "").replace("]", "");

            holder.lat.setText(mDataSource.get(position).get("lat").toString());
            holder.lng.setText(mDataSource.get(position).get("lng").toString());


try {
    holder.distance.setText(mDataSource.get(position).get("distancetonext").toString());
    holder.duration.setText(mDataSource.get(position).get("durationtonext").toString());

}catch (Exception e){

    Log.d("SelectedLocationList", "Line 223 "+ e);

}





      //      Log.d("A", "checkdistancetonext" + mDataSource.get(position).get("distancetonext").toString());

            Log.d("A", "distancedurationonly  in adapter" + mDataSource.get(position).get("distancetonext").toString());


           // holder.distance.setText(mDataSource.get(position).get("distancetonext").toString());
            //holder.duration.setText(mDataSource.get(position).get("durationtonext").toString());
            Log.d("A", "pherw" +mDataSource.get(position).get("distancetonext").toString());
            Log.d("A", "pherw" +mDataSource.get(position).get("durationtonext").toString());


            holder.vicinity.setText(mDataSource.get(position).get("vicinity").toString());

            holder.place_id.setText(mDataSource.get(position).get("place_id").toString());

            if (mDataSource.get(position).get("rating").toString() != "No Rating") {
                holder.ratingBar.setRating(Float.parseFloat(mDataSource.get(position).get("rating").toString()));
            }
            final ImageButton detailsbutton = (ImageButton) holder.itemView.findViewById(R.id.details_btn);
            final ImageButton directionsbutton = (ImageButton) holder.itemView.findViewById(R.id.directions_btn);
            final ImageButton googlemapsbutton = (ImageButton) holder.itemView.findViewById(R.id.google_details_btn);
            final ImageButton timebutton = (ImageButton) holder.itemView.findViewById(R.id.time_btn);

            int add = position + 1;
            holder.name.setText(add + ") " + mDataSource.get(position).get("place_name").toString());
            //   holder.image.setImageResource("https://maps.googleapis.com/maps/api/place/photo?photoreference="+list
            //      +"&sensor=false&maxheight=1000&maxwidth=1000&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().into(thumbnailImageView);
            Log.d("A", "checkimageselected" + list);
            try {

                Picasso.with(mContext).load(R.drawable.selected).fit().into
                        (holder.selectedimage);

                if(!holder.distance.getText().equals("")){
                    Picasso.with(mContext).load(R.drawable.durationicon).fit().into
                            (holder.durationimage);
                    Picasso.with(mContext).load(R.drawable.distanceicon).fit().into
                            (holder.distanceimage);
                    Picasso.with(mContext).load(R.drawable.arrowdownicon).fit().into
                            (holder.arrowimage);
//TODO working but need some adjustment
                }


                Picasso.with(mContext).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + list
                        + "&sensor=false&maxheight=100&maxwidth=100&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().transform(new
                        CircleTransform()
                ).error(R.drawable
                        .noimage)
                        .placeholder(R
                                .drawable
                                .loading_gif).into(holder.image);
                Picasso.with(mContext).load(R.drawable.googlemapsicon).fit().into
                        (googlemapsbutton);

                Picasso.with(mContext).load(R.drawable.viewdetails).fit().into
                        (detailsbutton);
                Picasso.with(mContext).load(R.drawable.directionsicon).fit().into
                        (directionsbutton);
                Picasso.with(mContext).load(R.drawable.timeicon).fit().into
                        (timebutton);
            } catch (Exception e) {

                Log.d("SelectedLocationList", "exception 168");


            }
//TODO:Image not loading
            // implement setOnClickListener event on item view.

            detailsbutton.setVisibility(View.VISIBLE);

            final String lat = holder.lat.getText().toString();
            final String lng = holder.lng.getText().toString();


            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //         MapsActivity m = new MapsActivity();
                    //Allow single selection hack,...
                    notifyDataSetChanged();
                    //TODO: uhhhh this one is not really that important anyway....
                    //   detailsbutton.setVisibility(View.VISIBLE);

                    ArrayList<Marker> mapMarkers = MapsActivity.getMapMarkers();

                    view.setSelected(true);

                    globalPosition = position;

                    //  Toast.makeText(getApplicationContext(), lat.toString(), Toast.LENGTH_SHORT).show();
                    LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    Marker result = null;
                    //For each marker in the map...where the lat equals to the lat provided by the  recycle view?
                    if (mapMarkers != null) {
                        for (Marker c : mapMarkers) {

                            if (lat.equals(String.valueOf(c.getPosition().latitude)) && lng.equals(String.valueOf(c.getPosition().longitude))) {
//MapsActivity m= new MapsActivity();
                                String s = MapsActivity.showPath(c);
                                MapsActivity.downloadUrl download = new MapsActivity.downloadUrl(mContext);
                                download.execute(s);
                                Log.d("Checkruntimes", "Runningdownloadurl  6");
//This
                                // should not be here... showpath is already up there.
                                //       mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                                //      mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

                                break;
                            }
                        }
                    }
                }

            });


            // display a toast with person name on item click
            //  Toast.makeText(context, personNames.get(position), Toast.LENGTH_SHORT).show();


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
            timebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//TODO: Move data to there
                    View parentRow = (View) v.getParent();


                    TextView place_id = (TextView) parentRow.findViewById(R.id.place_id);
                    String place_id_text = place_id.getText().toString();

                    Fragment fragment = null;
                    Class fragmentClass = null;
                    fragmentClass = LocationDetailsActivity.class;
                    try {
                        // DataHolderClass.getInstance().setDistributor_id(place_id_text);
                        DataHolderClass.getInstance2().setDistributor_id2("Time");


                        DataHolderClass.getInstancearray().setDistributor_idarray(place_id_text);

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
            detailsbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//TODO: Move data to there
                    View parentRow = (View) v.getParent();

                    TextView place_id = (TextView) parentRow.findViewById(R.id.place_id);
                    String place_id_text = place_id.getText().toString();

                    Fragment fragment = null;
                    Class fragmentClass = null;
                    fragmentClass = LocationDetailsActivity.class;
                    try {
                        // DataHolderClass.getInstance().setDistributor_id(place_id_text);
                        DataHolderClass.getInstancearray().setDistributor_idarray(place_id_text);


                        fragment = (Fragment) fragmentClass.newInstance();
                        //fragment.setArguments(args);

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


                    //   fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();

                }
            });
        }

    }


    @Override
    public int getItemCount() {
        if (mDataSource != null) {
            Log.d("A", "Dujjjjjjjgetitemcount" + mDataSource.size());

            return mDataSource.size();
        }
        return 0;
    }

    public static int getItemCount2() {
        if (mDataSource != null) {
            Log.d("A", "Dujjjjjjjgetitemcount" + mDataSource.size());

            return mDataSource.size();
        }
        return 0;
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
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
        ImageView arrowimage;
        ImageView distanceimage;
        ImageView durationimage;

        TextView distance;
        TextView duration;

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
            arrowimage = (ImageView) itemView.findViewById(R.id.arrow_down);
            durationimage = (ImageView) itemView.findViewById(R.id.duration);

            distance = (TextView) itemView.findViewById(R.id.distance_text);
            duration = (TextView) itemView.findViewById(R.id.duration_text);

            distanceimage = (ImageView) itemView.findViewById(R.id.distance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("check", "hhhhhhhhhhhchecklocationtecycler");


            //        v.setTag(getAdapterPosition());

                    // mItemClickListener.onItemClick(getLayoutPosition(), v, id);
                }
            });


        }
    }
}
