package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.content.Context;
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
import android.widget.Button;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by User on 6/2/2017.
 */
public class LocationListRecyclerViewAdapter extends RecyclerView.Adapter<LocationListRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private  List<LinkedHashMap<String,String>> mDataSource;
    private static List<LinkedHashMap<String,String>> mDataSourceforSend;

    private int Position;
    private static RecyclerView.ViewHolder holder2;

    public LocationListRecyclerViewAdapter(Context context, List<LinkedHashMap<String,String>> items) {
        mContext = context;
            Log.d("A", "DujjjjjjjNumberofitems(List)"+items);
        mDataSourceforSend=items;
        mDataSource = items;
        Log.d("A", "DujjjjjjjNumberofitems(List)Upperitemcount"+mDataSourceforSend);

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


    public static List<LinkedHashMap<String,String>>  getItem() {
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
    int    globalPosition=-1;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // set the data in items

        holder2=holder;
        Log.d("A", "DujjjjjjjonbidViewholder"+holder2);

        String singlephotoreference=mDataSource.get(position).get("photo_reference").toString();
            //Well this helps when comning back from other page sooo....so that i didnt reset to old one or some shit
        if(position==globalPosition)
        {
            Log.d("A", "Postitionchanges1"+holder.name.getText());

            //change color like
            holder.itemView.setSelected(true);

        }
        else
        {

            Log.d("A", "Postitionchanges2"+holder.name.getText());

            //revert back to regular color
            holder.itemView.setSelected(false);
        }

       ArrayList waypoint= MapsActivity.getWaypointwithDateList();
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
                                                           //If possible dont care about this now
                                                           //TODO: Seems to be unsolvable , need little hack but not now (Triggering marker onclick
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


                        TextView place_id=  (TextView) parentRow.findViewById(R.id.place_id);
                        String place_id_text= place_id.getText().toString();

                        Fragment fragment = null;
                        Class fragmentClass=null;
                        fragmentClass = LocationDetailsActivity.class;
                        try {
                            DataHolderClass.getInstance().setDistributor_id(place_id_text);

                            fragment = (Fragment) fragmentClass.newInstance();

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




                    }
                });



            }








    @Override
    public int getItemCount() {
        if(mDataSource!=null) {
            Log.d("A", "DujjjjjjjNumberofitems(List)  Item COUNT"+ mDataSource.size());

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