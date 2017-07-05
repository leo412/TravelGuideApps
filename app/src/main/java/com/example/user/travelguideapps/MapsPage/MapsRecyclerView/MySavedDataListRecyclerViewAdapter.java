package com.example.user.travelguideapps.MapsPage.MapsRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.SavedDataListFragment.OnListFragmentInteractionListener;
import com.example.user.travelguideapps.MapsPage.MapsRecyclerView.dummy.DummyContent.DummyItem;
import com.example.user.travelguideapps.R;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySavedDataListRecyclerViewAdapter extends RecyclerView.Adapter<MySavedDataListRecyclerViewAdapter.ViewHolder> {
    private static String ItemName;

    private final ArrayList mValues;
    private final OnListFragmentInteractionListener mListener;

    public MySavedDataListRecyclerViewAdapter(ArrayList items, OnListFragmentInteractionListener listener) {
        System.out.println("Whatispostbeforeitems THISITMES" + items);

        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_saveddatalist, parent, false);

        return new ViewHolder(view);
    }

    //Global positiong sismple hack
    int globalPosition = -1;
    private View viewforselected;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position).toString();
        holder.mIdView.setText(mValues.get(position).toString());
        holder.mContentView.setText(mValues.get(position).toString());
        System.out.println("Whatispostbeforeitems THISITMESName" + ItemName);
if(getItemName()!=null) {
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
                ItemName =mValues.get(position).toString() ;

                if (null != mListener) {


                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }


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
        public final TextView mIdView;
        public final TextView mContentView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
