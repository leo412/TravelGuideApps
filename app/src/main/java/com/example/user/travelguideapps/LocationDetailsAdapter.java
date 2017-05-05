package com.example.user.travelguideapps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 3/18/2017.
 */
//Put items into listview
public class LocationDetailsAdapter extends RecyclerView.Adapter<LocationDetailsAdapter.ViewHolder> {
    // private ArrayList<AndroidVersion> android_versions;
    private Context context;
    private RecyclerView.ViewHolder viewHolder2;
    private List mDataSource;

    public LocationDetailsAdapter(Context context, List mDataSource) {
        this.context = context;
        this.mDataSource = mDataSource;
        Log.d("isthislistor... ", mDataSource.toString());

    }

    @Override
    public LocationDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.place_details_photo_horizontal, viewGroup, false);
        return new ViewHolder(rowView);
    }

    final Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d(TAG, "Success! Bitmap is downloaded.");
            Log.d("whichrun", "Onbitmap");

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d(TAG, "Failed! Bitmap could not downloaded.");

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d(TAG, "BitMapStartRunning");

        }
    };

    //TODO:Figure wtf caused the picture to go all wrong
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Log.d("whichrun", "Onbind");


        viewHolder2 = viewHolder;
        viewHolder.setIsRecyclable(false);
        String data = mDataSource.get(viewHolder.getLayoutPosition()).toString();
        String changeddata = data.replace("[", "").replace("]", "").replace(" ", "").trim();
//String data= Arrays.toString(mDataSource.toArray()).replace("[", "").replace("]", "").replace(" ","").trim();
        final String tested = changeddata;
        Log.d("DetailsResult", "Diditfrea" + getItemCount());
        Log.d("DetailsResult", "Thenumber" + i);
        Log.d("DetailsResult", "Thenumber2" + viewHolder.getLayoutPosition());

//      Picasso.with(context).load("https://maps.googleapis.com/maps/api/place/photo?photoreference="+mDataSource.get(0).toString().trim()
        //   Picasso.with(context).cancelRequest(viewHolder.imageView);
        Picasso.with(context).load("https://maps.googleapis.com/maps/api/place/photo?photoreference=" + changeddata +
                "&sensor=false&maxheight=1000&maxwidth=1000&key=AIzaSyDSF5Cc8Vu9gn-OzTtrzWMA5kXX-g--NMk").placeholder(R.drawable.loading_gif).into
                (viewHolder.imageView);

        viewHolder.imageView.setTag(mTarget);

    }

    @Override
    public int getItemCount() {
        //Notsure if correct wtf is this
        //   return android_versions.size();
        return mDataSource.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View rowView) {
            super(rowView);

            imageView =
                    (ImageView) rowView.findViewById(R.id.list_image_horizontal);
        }
    }
}





