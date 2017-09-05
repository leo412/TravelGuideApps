package com.example.user.travelguideapps.LocationDetails;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.travelguideapps.DataHolderClass;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.SavedDataListFragment.OnListFragmentInteractionListener;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.dummy.DummyContent.DummyItem;
import com.example.user.travelguideapps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class dialog_overlap_adapter extends RecyclerView.Adapter<dialog_overlap_adapter.ViewHolder> {
    private static String ItemName;
    private Context mContext;
    Dialog dialog;
    private final ArrayList<LinkedHashMap<String, Object>> mValues;
    // private final OnListFragmentInteractionListener mListener;

    public dialog_overlap_adapter(ArrayList<LinkedHashMap<String, Object>> items, Dialog dialog) {
        System.out.println("dialogoverlapadapter THISITMES" + items);

      //  mContext = context;
        this.dialog = dialog;
        mValues = items;
        // mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dialog_overlap_recycler, parent, false);
        Context mContext = parent.getContext();

        return new ViewHolder(view);
    }

    //Global positiong sismple hack
    int globalPosition = -1;
    private View viewforselected;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Context mContext = holder.itemView.getContext();

        holder.mItem = mValues.get(position).toString();
        holder.mPlace_name.setText(mValues.get(position).get("place_name").toString());

        if (!mValues.get(position).get
                ("selectedenddate").toString().equals("N/A")) {
            holder.mTime.setText(mValues.get(position).get("selectedstartdate").toString().substring(12) + " - " + mValues.get(position).get
                    ("selectedenddate").toString().substring(12));
        } else {
            holder.mTime.setText(mValues.get(position).get("selectedstartdate").toString().substring(12) + " - N/A");

        }

        if (!mValues.get(position).get
                ("selectedenddate").toString().equals("N/A")) {
            holder.mDate.setText(mValues.get(position).get("selectedstartdate").toString().substring(0, 12) + "-" + mValues.get(position).get
                    ("selectedenddate").toString().substring(0, 12));

        }
        else {
            holder.mDate.setText(mValues.get(position).get("selectedstartdate").toString().substring(0,12) + " - N/A");

        }

        holder.place_id.setText(mValues.get(position).get("place_id").toString());

        final ImageButton b = (ImageButton) holder.itemView.findViewById(R.id.details_btn);
//
        final ImageView markerimage = (ImageView) holder.itemView.findViewById(R.id.markerimage);
        final ImageView timeimage = (ImageView) holder.itemView.findViewById(R.id.timeicon);
        final ImageView calendarimage = (ImageView) holder.itemView.findViewById(R.id.calendaricon);



        b.setOnClickListener(new View.OnClickListener() {
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
                    DataHolderClass.getInstancearray().setDistributor_idarray(place_id_text);

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
                dialog.dismiss();
                FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
             //   FragmentTransaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right, R.anim.slide_right, R.anim.slide_left);
//TODO:Try to add animation
                fragmentTransaction.replace(R.id.flContent2, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


                //   fragmentManager.beginTransaction().replace(R.id.flContent2, fragment).commit();

            }
        });
        String singlephotoreference = mValues.get(position).get("photo_reference").toString();
        List<String> myList = new ArrayList<String>(Arrays.asList(singlephotoreference.split(",")));
        String list = myList.get(0).replace("[", "").replace("]", "");
        //TODO:WELL lets ignore the image unless necessary....? (Image out of memory error
//        Picasso.with(mContext).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + list
//                + "&sensor=false&maxheight=100&maxwidth=100&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit().transform(new CircleTransform())
//                .error(R.drawable.noimage)
//                .placeholder(R.drawable.loading_gif).into
//                (holder.image);
        Picasso.with(mContext).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + list
                + "&sensor=false&maxheight=100&maxwidth=100&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").fit()
                .error(R.drawable.noimage)
                .placeholder(R.drawable.loading_gif).into
                (holder.image);
        Picasso.with(mContext).load(R.drawable.mapmarkericon).fit().into
                (markerimage);
        Picasso.with(mContext).load(R.drawable.timeicon).fit().into
                (timeimage);
        Picasso.with(mContext).load(R.drawable.calendaricon).fit().into
                (calendarimage);
        Picasso.with(mContext).load(R.drawable.viewdetails).fit().into
                (b);
        System.out.println("Whatispostbeforeitems THISITMESName" + mValues.get(position).toString());
        if (getItemName() != null) {
            if (getItemName().equals(mValues.get(position).toString())) {
                holder.itemView.setSelected(true);


            }
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewforselected != null) {
                    viewforselected.setSelected(false);
                }
                viewforselected = v;
                v.setSelected(true);
                ItemName = mValues.get(position).toString();


            }
        });


    }

    public static String getItemName() {
        return ItemName;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mPlace_name;
        public final TextView mDate;
        public final TextView mTime;
        public final ImageButton detailsbutton;
        public final TextView place_id;

        public String mItem;
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPlace_name = (TextView) view.findViewById(R.id.place_name);
            mDate = (TextView) view.findViewById(R.id.overlapdate);
            mTime = (TextView) view.findViewById(R.id.overlaptime);
            detailsbutton = (ImageButton) view.findViewById(R.id.details_btn);
            place_id = (TextView) view.findViewById(R.id.place_id);

            image = (ImageView) view.findViewById(R.id.list_image);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDate.getText() + "'";
        }
    }
}
