package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.user.travelguideapps.BaseActivity;
import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.LoginPage.LoginActivity;
import com.example.user.travelguideapps.MapsPage.MapsActivity;
import com.example.user.travelguideapps.R;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by User on 6/2/2017.
 */
//TODO: NOT used anymore??!??
public class LocationDataAdapter extends RecyclerView.Adapter<LocationDataAdapter.MyViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList mDataSource;
    private static List<LinkedHashMap<String, String>> mDataSourceforSend;
    private SelectedLocationListRecyclerViewAdapter Locationadapter;
    private static String ItemName;

    private int Position;
    private static RecyclerView.ViewHolder holder2;

    public LocationDataAdapter(ArrayList items) {
        Log.d("A", "LocationDataadap(List)" + items);
        //     mDataSourceforSend=items;
        mDataSource = items;

        //mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


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


    public static List<LinkedHashMap<String, String>> getItem() {
        return mDataSourceforSend;
    }

    public static RecyclerView.ViewHolder getHolder2() {
        return holder2;
    }

    public static String getItemName() {
        return ItemName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("A", "DujjjjjjjLocationDataadap");
        Context context = parent.getContext();

        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder


        return vh;
    }

    int globalPosition = -1;

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // set the data in items
        final Context mContext = holder.itemView.getContext();

        //    holder2=holder;

        ItemName = mDataSource.get(position).toString();
        Log.d("A", "LocationDataadapStop pls" + ItemName);

        holder.dataname.setText(mDataSource.get(position).toString());


        //Well this helps when comning back from other page sooo....so that i didnt reset to old one or some shit
        if (position == globalPosition) {
            //change color like
            holder.itemView.setSelected(true);

        } else {

            //revert back to regular color
            holder.itemView.setSelected(false);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                notifyDataSetChanged();
                //TODO: uhhhh this one is not really that important anyway....
                //   b.setVisibility(View.VISIBLE);

                ArrayList<Marker> mapMarkers = MapsActivity.getMapMarkers();

                view.setSelected(true);
                Log.d("A", "CurrentWaypoint" + MapsActivity.getWaypointwithDateList() + DataHolderClass.getInstance2().getDistributor_id2());

                globalPosition = position;

                if (DataHolderClass.getInstance2().getDistributor_id2().equals("Save")) {
                    Log.d("A", "CurrentWaypoint" + MapsActivity.getWaypointwithDateList());

                    BaseActivity.mDatabase.child("users").child(LoginActivity.getUserID()).child(mDataSource.get
                            (position).toString())
                            .setValue
                                    (MapsActivity
                                            .getWaypointwithDateList());
                    Log.d("A", "CurrentWaypoint" + MapsActivity.getWaypointwithDateList());


                } else {


                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference().child("users").child(LoginActivity.getUserID())
                            .child(holder.dataname.getText().toString());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            LinkedHashMap post = (LinkedHashMap) dataSnapshot.getValue();


                            //   ArrayList a=new ArrayList();

                            //      a=new ArrayList<>(post.values());
//                                                        for(int i=0;i<post.size();i++){
//
//                                                            a.add(post.values());
//                                                        }
                            Log.d("A", "LocationDataadapClicked" + post);


                            MapsActivity.addWaypointwithDateList(post);
                            //Cannot directly update, findways to reset details,,,,

                            Locationadapter = new SelectedLocationListRecyclerViewAdapter(
                                    MapsActivity
                                            .getWayPointDetailsList());


                            Locationadapter.notifyDataSetChanged();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }


                    });
                }


            }

        });


        // display a toast with person name on item click
        //  Toast.makeText(context, personNames.get(position), Toast.LENGTH_SHORT).show();

//
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
        TextView dataname;

        AdapterView.OnItemClickListener mItemClickListener;

        public MyViewHolder(final View itemView) {
            super(itemView);
            // get the reference of item view's
            dataname = (TextView) itemView.findViewById(R.id.data_name);

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