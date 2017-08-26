package com.example.user.travelguideapps.LocationDetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.travelguideapps.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 3/18/2017.
 */
//Put items into listview
public class LocationDetailsReviewAdapter extends RecyclerView.Adapter<LocationDetailsReviewAdapter.ViewHolder> {
    // private ArrayList<AndroidVersion> android_versions;
    private Context context;
    private RecyclerView.ViewHolder viewHolder2;
    private ArrayList author_namelist;
    private ArrayList reviews_textlist;
    private ArrayList author_photolist;
    private ArrayList relative_time_descriptionlist;
    private ArrayList review_ratinglist;


    public LocationDetailsReviewAdapter(ArrayList mDataSource, ArrayList mDataSource2, ArrayList mDataSource3, ArrayList
            mDataSource4, ArrayList mDataSource5) {
        this.context = context;
        this.author_namelist = mDataSource;
        this.reviews_textlist = mDataSource2;
        this.author_photolist = mDataSource3;
        this.relative_time_descriptionlist = mDataSource4;
        this.review_ratinglist = mDataSource5;


        Log.d("isthislistor... ", mDataSource.toString());

    }

    @Override
    public LocationDetailsReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row_reviews, viewGroup, false);


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

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Context context = viewHolder.itemView.getContext();

        Log.d("whichrun", "rrrrrrrr" + viewHolder.author_name.getText());

        //    viewHolder.setIsRecyclable(false);
        String data = author_namelist.get(i).toString();
        String data2 = reviews_textlist.get(i).toString();
        String data3 = author_photolist.get(i).toString();
        String data4 = relative_time_descriptionlist.get(i).toString();
        String data5 = review_ratinglist.get(i).toString();


        String changeddata = data.replace("[", "").replace("]", "").trim();
        String changeddata2 = data2.replace("[", "").replace("]", "").trim();
        String changeddata3 = data3.replace("[", "").replace("]", "").trim();
        String changeddata4 = data4.replace("[", "").replace("]", "").trim();
        String changeddata5 = data5.replace("[", "").replace("]", "").trim();

        if (changeddata5 != "") {

            viewHolder.author_name.setText(changeddata);
            viewHolder.review_text.setText(changeddata2);
            viewHolder.relative_time_description.setText(changeddata4);
            viewHolder.rating_bar.setRating(Float.parseFloat(changeddata5));


            Log.d("whichrun", "rrrrrrrr" + viewHolder.author_name.getText());
            Log.d("whichrun", "rrrrrrrr222222" + viewHolder.review_text.getText());
//String data= Arrays.toString(author_namelist.toArray()).replace("[", "").replace("]", "").replace(" ","").trim();

//      Picasso.with(context).load("https://maps.googleapis.com/maps/api/place/photo?photoreference="+author_namelist.get(0).toString().trim()
//           Picasso.with(context).cancelRequest(viewHolder.imageView);
//        Only beow this TODO
            if (changeddata3 != "") {
                Picasso.with(context).load(changeddata3).placeholder(R.drawable.loading_gif).into
                        (viewHolder.imageView);

                viewHolder.imageView.setTag(mTarget);
            }
        }
    }

    @Override
    public int getItemCount() {
        //Notsure if correct wtf is this
        //   return android_versions.size();
        return author_namelist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView author_name;
        TextView review_text;
        RatingBar rating_bar;
        TextView relative_time_description;

        public ViewHolder(View rowView) {
            super(rowView);

            imageView =
                    (ImageView) rowView.findViewById(R.id.list_image);
            author_name = (TextView) rowView.findViewById(R.id.author_name);

            review_text = (TextView) rowView.findViewById(R.id.review_text);

            relative_time_description = (TextView) rowView.findViewById(R.id.relative_time_description);

            rating_bar = (RatingBar) rowView.findViewById(R.id.rating_Bar);


        }
    }
}





