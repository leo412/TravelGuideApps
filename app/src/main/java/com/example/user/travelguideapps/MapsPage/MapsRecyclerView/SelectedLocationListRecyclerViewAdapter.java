package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import com.example.user.travelguideapps.DialogTravelSettingFragment;
import com.example.user.travelguideapps.LocationDetails.LocationDetailsActivity;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.user.travelguideapps.MapsPage.MapsActivity.CurrentLocation;

/**
 * Created by User on 6/2/2017.
 */
//TODO: Edit2 Yeeeppp this is probably needed, because they need different function in finding setchecked..
//TODO: most probably this is not needed, change if only needed .... called from LocationRecyclerVIew1/2
public class SelectedLocationListRecyclerViewAdapter extends RecyclerView.Adapter<SelectedLocationListRecyclerViewAdapter.MyViewHolder> {
    private LayoutInflater mInflater;
    private static List<HashMap<String, Object>> mDataSource;
    private static List<HashMap<String, Object>> mDataSourceforSend;

    private int position;
    private static RecyclerView.ViewHolder holder2;
    private ProgressDialog pDialog;

    private static ArrayList duration = new ArrayList();
    private static ArrayList distance = new ArrayList();


    public SelectedLocationListRecyclerViewAdapter(ArrayList<HashMap<String, Object>> items) {
        mDataSource = items;
        //   mInflater = (LayoutInflater) holder.itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    //2
    //  @Override
    //public Object getItem(int holder.getAdapterPosition()) {
    //     return mDataSource.get(holder.getAdapterPosition());
    // }

    //3
    @Override
    public long getItemId(int position) {
        return position;
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

    int globalposition = -1;
    boolean completedloop = false;
    int previousposition = -1;
    ArrayList previousarray = new ArrayList();

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // set the data in items
//new box(except of events cards
//        duration.clear();
//        distance.clear();
        Log.d("A", "selectedwaypoinaskdakjsd " + mDataSource);


        if (previousarray.contains(holder.getAdapterPosition())) {
            //becomes false = don't run notify
            completedloop = true;
            Log.d("A", "testingforholder.getAdapterPosition()" + "TOPPPPPPPP" + distance);


        } else {
            //Run notify??
            Log.d("A", "testingforholder.getAdapterPosition()" + "BOTTTTTTOM" + distance);

            completedloop = false;

        }
        previousarray.add(holder.getAdapterPosition());
//
        previousposition = position;
        Log.d("A", "testingforholder.getAdapterPosition()" + MapsActivity.getWayPointDetailsList().size());
        Log.d("A", "ttryginsytoseee  for selected )" + holder.getAdapterPosition());

        Log.d("A", "calculatethesize" + completedloop);
        Log.d("A", "calculatethesizewhatistheitem" + holder.getAdapterPosition());
//
        if (MapsActivity.getWayPointDetailsList().size() == mDataSource.size()) {
            //TODO: can add travel mode/avoid

            String place_id = mDataSource.get(holder.getAdapterPosition()).toString();
//
            String singlephotoreference = mDataSource.get(holder.getAdapterPosition()).get("photo_reference").toString();

            Log.d("A", "singlephotoreferenceahaha" + singlephotoreference);

//Well this helps when comning back from other page sooo....
            if (holder.getAdapterPosition() == globalposition) {

                //change color like
                holder.itemView.setSelected(true);


            } else {
                //revert back to regular color
                holder.itemView.setSelected(false);
            }


            ArrayList<HashMap> waypoint = MapsActivity.getWaypointwithDateList();

            for (int i = 0; i < waypoint.size(); i++) {

                Log.d("A", "selectedwaypointcheck" + waypoint.get(i).get("place_id"));

                if (waypoint.get(i).get("place_id").equals(mDataSource.get(holder.getAdapterPosition()).get("place_id"))) {

                    holder.selectedimage.setVisibility(View.VISIBLE);

                } else {
                    //Well no point using this...
                    //holder.Selected.setVisibility(View.INVISIBLE);


                }


            }
            List<String> myList = new ArrayList<String>(Arrays.asList(singlephotoreference.split(",")));

            String list = myList.get(0).replace("[", "").replace("]", "");

            holder.lat.setText(mDataSource.get(holder.getAdapterPosition()).get("lat").toString());
            holder.lng.setText(mDataSource.get(holder.getAdapterPosition()).get("lng").toString());

            Log.d("A", "okaytryingtinfindout Position " + holder.getAdapterPosition() + "   " + mDataSource.get(holder.getAdapterPosition()).get
                    ("distance"));
            Log.d("A", "okaytryingtinfindout Position " + holder.getAdapterPosition() + "   " + mDataSource.get(holder.getAdapterPosition()).get
                    ("distancetonext"));
            try {
                holder.distance.setText(mDataSource.get(holder.getAdapterPosition()).get("distancetonext").toString());
                holder.duration.setText(mDataSource.get(holder.getAdapterPosition()).get("durationtonext").toString());
                Log.d("A", "selectedwaypointgfhh " + mDataSource.get(holder.getAdapterPosition()).get("distancetonext"));

                Log.d("A", "selectedwaypointgfhh " + mDataSource.get(holder.getAdapterPosition()).get("timetostarttravel"));

                holder.localdistancetext.setText(mDataSource.get(holder.getAdapterPosition()).get("distance").toString());
                holder.localdurationtext.setText(mDataSource.get(holder.getAdapterPosition()).get("duration").toString());

                holder.departuretime.setText(mDataSource.get(holder.getAdapterPosition()).get("timetostarttravel").toString());


                Log.d("A", "selectedwaypointcheckwtfhere 3 " + mDataSource.get(holder.getAdapterPosition()).get("distancetonext"));

            } catch (Exception e) {

                Log.d("SelectedLocationList", "Line 223 " + e);
//rrrwhen i was 7 www
            }


            //      Log.d("A", "checkdistancetonext" + mDataSource.get(holder.getAdapterPosition()).get("distancetonext").toString());

            Log.d("A", "distancedurationonly  in adapter" + mDataSource.get(holder.getAdapterPosition()).get("distancetonext").toString());


            // holder.distance.setText(mDataSource.get(holder.getAdapterPosition()).get("distancetonext").toString());
            //holder.duration.setText(mDataSource.get(holder.getAdapterPosition()).get("durationtonext").toString());
            Log.d("A", "pherw " + mDataSource.get(holder.getAdapterPosition()).get("distancetonext").toString() + " Position " + holder
                    .getAdapterPosition
                            ());


            holder.vicinity.setText(mDataSource.get(holder.getAdapterPosition()).get("vicinity").toString());

            holder.place_id.setText(mDataSource.get(holder.getAdapterPosition()).get("place_id").toString());

            if (mDataSource.get(holder.getAdapterPosition()).get("rating").toString() != "No Rating") {
                holder.ratingBar.setRating(Float.parseFloat(mDataSource.get(holder.getAdapterPosition()).get("rating").toString()));
            }
            final ImageButton detailsbutton = (ImageButton) holder.itemView.findViewById(R.id.details_btn);
            final ImageButton directionsbutton = (ImageButton) holder.itemView.findViewById(R.id.directions_btn);
            final ImageButton googlemapsbutton = (ImageButton) holder.itemView.findViewById(R.id.google_details_btn);
            final ImageButton timebutton = (ImageButton) holder.itemView.findViewById(R.id.time_btn);

            int add = holder.getAdapterPosition() + 1;
            holder.name.setText(add + ") " + mDataSource.get(holder.getAdapterPosition()).get("place_name").toString());

            try {

                Picasso.with(holder.itemView.getContext()).load(R.drawable.selected).fit().into
                        (holder.selectedimage);
                Picasso.with(holder.itemView.getContext()).load(R.drawable.distanceicon).fit().into
                        (holder.localdistance);
                Picasso.with(holder.itemView.getContext()).load(R.drawable.durationicon).fit().into
                        (holder.localduration);
                Log.d("A", "randomlygetitgher" + mDataSource.get(holder.getAdapterPosition()).get("durationtonext"));
                Log.d("A", "randomlygetitgherthewhole" + mDataSource.get(holder.getAdapterPosition()));
                Picasso.with(holder.itemView.getContext()).load(R.drawable.settingsicon).fit().into
                        (holder.travellocalsetting);
                if (!mDataSource.get(holder.getAdapterPosition()).get("durationtonext").toString().equals("0") && !Location_RecyclerView_Selected
                        .getissorted()) {

                    Log.d("A", "Loading...  isskashdkja" + Location_RecyclerView_Selected
                            .getissorted());

                    Picasso.with(holder.itemView.getContext()).load(R.drawable.durationicon).fit().into
                            (holder.durationimage);
                    Picasso.with(holder.itemView.getContext()).load(R.drawable.distanceicon).fit().into
                            (holder.distanceimage);
                    Picasso.with(holder.itemView.getContext()).load(R.drawable.down_arrow).fit().into
                            (holder.arrowimage);
                    Picasso.with(holder.itemView.getContext()).load(R.drawable.roadicon).fit().into
                            (holder.startingtimeimage);
                    Picasso.with(holder.itemView.getContext()).load(R.drawable.settingsicon).fit().into
                            (holder.travelsetting);


                    holder.distanceimage.setVisibility(View.VISIBLE);
                    holder.durationimage.setVisibility(View.VISIBLE);
                    holder.arrowimage.setVisibility(View.VISIBLE);
                    holder.distance.setVisibility(View.VISIBLE);
                    holder.duration.setVisibility(View.VISIBLE);
                    holder.startingtimeimage.setVisibility(View.VISIBLE);
                    holder.departuretime.setVisibility(View.VISIBLE);
                    holder.travelsetting.setVisibility(View.VISIBLE);
                  //  holder.travellocalsetting.setVisibility(View.VISIBLE);

                    //TODO working but need some adjustment
                } else {

                    Log.d("A", "Loading...for  inviisble" + holder.getAdapterPosition());
          //          holder.travellocalsetting.setVisibility(View.GONE);

                    holder.distanceimage.setVisibility(View.GONE);
                    holder.durationimage.setVisibility(View.GONE);
                    holder.arrowimage.setVisibility(View.GONE);
                    holder.distance.setVisibility(View.GONE);
                    holder.duration.setVisibility(View.GONE);

                    holder.startingtimeimage.setVisibility(View.GONE);
                    holder.departuretime.setVisibility(View.GONE);
                    holder.travelsetting.setVisibility(View.GONE);


                }


                Picasso.with(holder.itemView.getContext()).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + list
                        + "&sensor=false&maxheight=100&maxwidth=100&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().error(R.drawable
                        .noimage)
                        .placeholder(R
                                .drawable
                                .loading_gif).into(holder.image);

                Picasso.with(holder.itemView.getContext()).load(R.drawable.googlemapsicon).fit().into
                        (googlemapsbutton);


                Picasso.with(holder.itemView.getContext()).load(R.drawable.viewdetails).fit().into
                        (detailsbutton);
                Picasso.with(holder.itemView.getContext()).load(R.drawable.directionsicon).fit().into
                        (directionsbutton);
                Picasso.with(holder.itemView.getContext()).load(R.drawable.timeicon).fit().into
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
                    //TODO:why did i put this here... ...
                    //        notifyDataSetChanged();
                    //TODO: uhhhh this one is not really that important anyway....
                    //   detailsbutton.setVisibility(View.VISIBLE);

                    ArrayList<Marker> mapMarkers = MapsActivity.getMapMarkers();

                    view.setSelected(true);

                    globalposition = holder.getAdapterPosition();

                    //For each marker in the map...where the lat equals to the lat provided by the  recycle view?
                    if (mapMarkers != null) {
                        for (Marker c : mapMarkers) {

                            if (lat.equals(String.valueOf(c.getPosition().latitude)) && lng.equals(String.valueOf(c.getPosition()
                                    .longitude))) {
//MapsActivity m= new MapsActivity();
                                String s = MapsActivity.showPath(c);
                                MapsActivity.downloadUrl download = new MapsActivity.downloadUrl(holder.itemView.getContext());
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
                    Log.d("Hashthisruns", "notifyDataSetChanged  6");
                    //TODO:why did i put this here... ...

                 //   notifyDataSetChanged();

                }

            });


            // display a toast with person name on item click
            //  Toast.makeText(context, personNames.get(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
            holder.travellocalsetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager manager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();

                    // Create and show the dialog.
                    DialogTravelSettingFragment newFragment = DialogTravelSettingFragment.newInstance(holder.getAdapterPosition(),"local");
                    newFragment.show(manager, "dialog");


                }
            });

            holder.travelsetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager manager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();

                    // Create and show the dialog.
                    DialogTravelSettingFragment newFragment = DialogTravelSettingFragment.newInstance(holder.getAdapterPosition(),"tonext");
                    newFragment.show(manager, "dialog");


                }
            });

//i was house-sitting there werewe call tmy best friend til i am




            directionsbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    String lat = Double.toString(CurrentLocation.getLatitude());
                    String lng = Double.toString(CurrentLocation.getLongitude());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&origin=" + lat + "," + lng +
                            "&destination=A&destination_place_id=" + holder.place_id.getText()
                    ));
                    holder.itemView.getContext().startActivity(intent);

//causing hism to symbke if you block, dodge
                }
            });
            timebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//TODO: Move data to there
                    View parentRow = (View) v.getParent();

                    if (MapsActivity.pd.isShowing()) {
                        MapsActivity.pd.dismiss();
                    }
                    MapsActivity.pd.show();

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

                    FragmentManager fragmentManager = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //TODO:Note this one is originally replace, change to add to allow not refreash
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
                    holder.itemView.getContext().startActivity(intent);
                }
            });
            detailsbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    detailsbutton.setEnabled(false);
                    View parentRow = (View) v.getParent();
                    if (MapsActivity.pd.isShowing()) {
                        MapsActivity.pd.dismiss();
                    }
                    MapsActivity.pd.show();

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
//TODO: somehow change the holder.itemView.getContext() to this one works....
                    FragmentManager fragmentManager = ((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //TODO:Note this one is originally replace, change to add to allow not refresh
                    fragmentTransaction.replace(R.id.flContent2, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    detailsbutton.setEnabled(true);

                    //   fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();

                }
            });
        }
        // MapsActivity.pd.dismiss();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                MapsActivity.pd.dismiss();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 1000);
    }


    @Override
    public int getItemCount() {
        if (mDataSource != null) {
            // Log.d("A", "Dujjjjjjjgetitemcount" + mDataSource.size());

            return mDataSource.size();
        }
        return 0;
    }

    public static int getItemCount2() {
        if (mDataSource != null) {
            //Log.d("A", "Dujjjjjjjgetitemcount" + mDataSource.size());

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
        ImageView startingtimeimage;
        ImageButton travelsetting;
        ImageButton travellocalsetting;

        TextView localdistancetext;
        ImageView localdistance;

        ImageView localduration;
        TextView localdurationtext;

        TextView distance;
        TextView duration;
        TextView departuretime;

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
            startingtimeimage = (ImageView) itemView.findViewById(R.id.departuretime);
            travelsetting = (ImageButton) itemView.findViewById(R.id.travelsettingbutton);
            travellocalsetting = (ImageButton) itemView.findViewById(R.id.travellocalsettingbutton);

            localdistance = (ImageView) itemView.findViewById(R.id.local_distance);

            localdistancetext = (TextView) itemView.findViewById(R.id.local_distance_text);
            localduration = (ImageView) itemView.findViewById(R.id.local_duration);

            localdurationtext = (TextView) itemView.findViewById(R.id.local_duration_text);
            distance = (TextView) itemView.findViewById(R.id.distance_text);
            duration = (TextView) itemView.findViewById(R.id.duration_text);
            departuretime = (TextView) itemView.findViewById(R.id.departure_text);

            distanceimage = (ImageView) itemView.findViewById(R.id.distance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("check", "hhhhhhhhhhhchecklocationtecycler");


                    //        v.setTag(getAdapterholder.getAdapterPosition()());

                    // mItemClickListener.onItemClick(getLayoutholder.getAdapterPosition()(), v, id);
                }
            });


        }
    }
}
