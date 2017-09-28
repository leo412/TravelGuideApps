package com.example.user.travelguideapps;

/**
 * Created by User on 6/5/2017.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogCategoriesFragment extends DialogFragment  {

    private EditText mEditText;


    private Context mContext;
    private LayoutInflater mInflater;
    private  ArrayList mDataSource;
    private static List<HashMap<String,String>> mDataSourceforSend;

    private int Position;
    private static RecyclerView.ViewHolder holder2;
    public static DialogCategoriesFragment newInstance(String title) {
        DialogCategoriesFragment frag = new DialogCategoriesFragment();
        Bundle args = new Bundle();
        System.out.println("DialogFragmentFirst");




        //  args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }
    // Empty constructor required for DialogFragment

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view=inflater.inflate(R.layout.fragment_fragment_dialog_categories,null);

        final CheckBox barCheckBox = (CheckBox) view.findViewById(R.id.barCheckBox);
        final CheckBox restaurantCheckBox = (CheckBox) view.findViewById(R.id.restaurantCheckBox);
        final CheckBox amusement_parkCheckBox = (CheckBox) view.findViewById(R.id.amusement_parkCheckBox);
        final CheckBox shopping_mallCheckBox = (CheckBox) view.findViewById(R.id.shopping_mallCheckBox);
        final CheckBox aquarium_CheckBox = (CheckBox) view.findViewById(R.id.aquariumCheckBox);
        final CheckBox book_Store_CheckBox = (CheckBox) view.findViewById(R.id.bookstoreCheckBox);
        final CheckBox movie_theater_CheckBox = (CheckBox) view.findViewById(R.id.movietheaterCheckBox);

        final CheckBox departmentstoreCheckBox = (CheckBox) view.findViewById(R.id.departmentstoreCheckBox);
        final CheckBox conveniencestoreCheckBox = (CheckBox) view.findViewById(R.id.conveniencestoreCheckBox);
        final CheckBox jewelrystoreCheckBox = (CheckBox) view.findViewById(R.id.jewelrystoreCheckBox);
        final CheckBox liquorstoreCheckBox = (CheckBox) view.findViewById(R.id.liquorstoreCheckBox);
        final CheckBox electronicsstoreCheckBox = (CheckBox) view.findViewById(R.id.electronicsstoreCheckBox);
        final CheckBox clothingstoreCheckBox = (CheckBox) view.findViewById(R.id.clothingstoreCheckBox);
        final CheckBox shoestoreCheckBox = (CheckBox) view.findViewById(R.id.shoestoreCheckBox);
        final CheckBox pharmacystoreCheckBox = (CheckBox) view.findViewById(R.id.pharmacystoreCheckBox);
        final CheckBox gymCheckBox = (CheckBox) view.findViewById(R.id.gymCheckBox);
        final CheckBox spaCheckBox = (CheckBox) view.findViewById(R.id.spaCheckBox);
        final CheckBox bowlingCheckBox = (CheckBox) view.findViewById(R.id.bowlingCheckBox);
        final CheckBox campgroundCheckBox = (CheckBox) view.findViewById(R.id.campgroundCheckBox);
        final CheckBox subwayCheckBox = (CheckBox) view.findViewById(R.id.subwayCheckBox);
        final CheckBox transitCheckBox = (CheckBox) view.findViewById(R.id.transitCheckBox);

        final CheckBox trainCheckBox = (CheckBox) view.findViewById(R.id.trainCheckBox);
        final CheckBox busCheckBox = (CheckBox) view.findViewById(R.id.busCheckBox);
        final CheckBox carrentalCheckBox = (CheckBox) view.findViewById(R.id.carrentalCheckBox);




        if(DataHolderClass.getInstance3().getDistributor_id3()!=null ){
            ArrayList array= DataHolderClass.getInstance3().getDistributor_id3();

            if (array.contains("bar")) {
                barCheckBox.setChecked(true);

            }
            if (array.contains("restaurant")) {
                restaurantCheckBox.setChecked(true);


            }

            if (array.contains("amusement_park")) {

                amusement_parkCheckBox.setChecked(true);

            }
            if (array.contains("shopping_mall")) {
                shopping_mallCheckBox.setChecked(true);


            }
            if (array.contains("aquarium")) {

                aquarium_CheckBox.setChecked(true);

            }
            if (array.contains("movie_theater")) {
                movie_theater_CheckBox.setChecked(true);


            }
            if (array.contains("library")) {

                // libraryCheckBox.setSelected(true);

            }
            if (array.contains("book_store")) {

                book_Store_CheckBox.setChecked(true);

            }

            if (array.contains("department_store")) {
                departmentstoreCheckBox.setChecked(true);

            }
            if (array.contains("convenience_store")) {
                conveniencestoreCheckBox.setChecked(true);


            }

            if (array.contains("book_store")) {

                book_Store_CheckBox.setChecked(true);

            }
            if (array.contains("jewelry_store")) {
                jewelrystoreCheckBox.setChecked(true);


            }
            if (array.contains("liquor_store")) {

                liquorstoreCheckBox.setChecked(true);

            }
            if (array.contains("electronics_store")) {
                electronicsstoreCheckBox.setChecked(true);


            }
            if (array.contains("clothing_store")) {

                clothingstoreCheckBox.setChecked(true);

            }
            if (array.contains("shoes_store")) {

                shoestoreCheckBox.setChecked(true);

            }
            if (array.contains("pharmacy")) {
                pharmacystoreCheckBox.setChecked(true);

            }
            if (array.contains("gym")) {
                gymCheckBox.setChecked(true);


            }

            if (array.contains("spa")) {

                spaCheckBox.setChecked(true);

            }
            if (array.contains("bowling_alley")) {
                bowlingCheckBox.setChecked(true);


            }
            if (array.contains("campground")) {

                campgroundCheckBox.setChecked(true);

            }
            if (array.contains("subway_station")) {
                subwayCheckBox.setChecked(true);


            }
            if (array.contains("transit_station")) {

                transitCheckBox.setChecked(true);

            }
            if (array.contains("train_station")) {

                trainCheckBox.setChecked(true);

            }
            if (array.contains("bus_station")) {

                busCheckBox.setChecked(true);

            }
            if (array.contains("car_rental")) {

                carrentalCheckBox.setChecked(true);

            }

        }



        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(view);
        alertDialog.setIcon(android.R.drawable.stat_notify_error);
        alertDialog.setTitle("Location Categories");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                      ArrayList array= new ArrayList();

                        if (barCheckBox.isChecked()) {
                            array.add("bar");

                        }
                        if (restaurantCheckBox.isChecked()) {
                            array.add("restaurant");


                        }

                        if (amusement_parkCheckBox.isChecked()) {
                            array.add("amusement_park");


                        }
                        if (shopping_mallCheckBox.isChecked()) {
                            array.add("shopping_mall");


                        }
                        if (aquarium_CheckBox.isChecked()) {
                            array.add("aquarium");


                        }
                        if (movie_theater_CheckBox.isChecked()) {
                            array.add("movie_theater");


                        }

                        if (book_Store_CheckBox.isChecked()) {
                            array.add("book_store");


                        }if (departmentstoreCheckBox.isChecked()) {
                            array.add("department_store");

                        }
                        if (conveniencestoreCheckBox.isChecked()) {
                            array.add("convenience_store");


                        }

                        if (jewelrystoreCheckBox.isChecked()) {
                            array.add("jewelry_store");


                        }
                        if (liquorstoreCheckBox.isChecked()) {
                            array.add("liquor_store");


                        }
                        if (electronicsstoreCheckBox.isChecked()) {
                            array.add("electronics_store");


                        }
                        if (clothingstoreCheckBox.isChecked()) {
                            array.add("clothing_store");


                        }

                        if (shoestoreCheckBox.isChecked()) {
                            array.add("shoes_store");


                        }if (pharmacystoreCheckBox.isChecked()) {
                            array.add("pharmacy");

                        }
                        if (gymCheckBox.isChecked()) {
                            array.add("gym");


                        }

                        if (spaCheckBox.isChecked()) {
                            array.add("spa");


                        }
                        if (bowlingCheckBox.isChecked()) {
                            array.add("bowling_alley");


                        }
                        if (campgroundCheckBox.isChecked()) {
                            array.add("campground");


                        }
                        if (subwayCheckBox.isChecked()) {
                            array.add("subway_station");


                        }

                        if (transitCheckBox.isChecked()) {
                            array.add("transit_station");


                        }


                        if (trainCheckBox.isChecked()) {
                            array.add("train_station");


                        }
                        if (busCheckBox.isChecked()) {
                            array.add("bus_station");


                        }

                        if (carrentalCheckBox.isChecked()) {
                            array.add("car_rental");


                        }






                        DataHolderClass.getInstance3().setDistributor_id3(array);



                        Toast.makeText(getActivity().getApplicationContext(), "Pressed OK"+array, Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity().getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        System.out.println("DialogFragment3rd");

    //    alertDialog.show();
        System.out.println("DialogFragmentLast");

        return  alertDialog.show();
    }

}