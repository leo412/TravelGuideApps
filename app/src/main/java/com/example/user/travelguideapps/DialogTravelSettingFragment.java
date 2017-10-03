package com.example.user.travelguideapps;

/**
 * Created by User on 6/5/2017.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.user.travelguideapps.MapsPage.MapsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogTravelSettingFragment extends DialogFragment {

    private EditText mEditText;


    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList mDataSource;
    private static List<HashMap<String, String>> mDataSourceforSend;
    private static String typeof;

    private static String transport = "driving";
    private static String avoid = "";

private     HashMap<Integer,ArrayList> hasharraytonext;
    private     HashMap<Integer,ArrayList> hasharraylocal;

    private static int Position;
    private static RecyclerView.ViewHolder holder2;

    public static DialogTravelSettingFragment newInstance(int position,String type) {
        DialogTravelSettingFragment frag = new DialogTravelSettingFragment();
        Bundle args = new Bundle();
        Position = position;
typeof=type;


        frag.setArguments(args);
        return frag;
    }
    // Empty constructor required for DialogFragment
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_dialog_distance_setting, null);

        final RadioButton drivingradio = (RadioButton) view.findViewById(R.id.drivingradio);
        final RadioButton walkingradio = (RadioButton) view.findViewById(R.id.walkingradio);
        final RadioButton bicyclingradio = (RadioButton) view.findViewById(R.id.bicyclingradio);
        final RadioButton transitradio = (RadioButton) view.findViewById(R.id.transitradio);

        final RadioButton noradio = (RadioButton) view.findViewById(R.id.noavoidradio);

        final RadioButton tollsradio = (RadioButton) view.findViewById(R.id.tollsradio);
        final RadioButton highwaysradio = (RadioButton) view.findViewById(R.id.highwaysradio);
        final RadioButton ferriesradio = (RadioButton) view.findViewById(R.id.ferriesradio);

        hasharraytonext = DataHolderClass.getInstancearraylistofradio().getDistributor_idarraylistofradio();
        hasharraylocal= DataHolderClass.getInstancearraylistofradio2().getDistributor_idarraylistofradio2();

        Toast.makeText(getActivity().getApplicationContext(), "tonext"+hasharraytonext, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity().getApplicationContext(), "local"+hasharraylocal, Toast.LENGTH_SHORT).show();


        ArrayList array = null;
        ArrayList localarray = null;

        if(hasharraytonext !=null) {

            array = hasharraytonext.get(Position);
        }else{
            hasharraytonext = new HashMap<Integer, ArrayList>();


        }
        if(hasharraylocal !=null) {

            localarray = hasharraylocal.get(Position);
        }else{
            hasharraylocal = new HashMap<Integer, ArrayList>();


        }
        if(typeof.equals("tonext")) {
            if (array != null) {
                switch (array.get(0).toString()) {
                    case "driving":
                        drivingradio.setChecked(true);
                        break;
                    case "walking":
                        walkingradio.setChecked(true);
                        break;
                    case "bicycling":
                        bicyclingradio.setChecked(true);
                        break;
                    case "transit":
                        transitradio.setChecked(true);
                        break;

                }


                switch (array.get(1).toString()) {
                    case "":
                        noradio.setChecked(true);
                        break;
                    case "tolls":
                        tollsradio.setChecked(true);
                        break;
                    case "highways":
                        highwaysradio.setChecked(true);
                        break;
                    case "ferries":
                        ferriesradio.setChecked(true);
                        break;

                }
            }
        }else {
            if (localarray != null) {
                switch (localarray.get(0).toString()) {
                    case "driving":
                        drivingradio.setChecked(true);
                        break;
                    case "walking":
                        walkingradio.setChecked(true);
                        break;
                    case "bicycling":
                        bicyclingradio.setChecked(true);
                        break;
                    case "transit":
                        transitradio.setChecked(true);
                        break;

                }


                switch (localarray.get(1).toString()) {
                    case "":
                        noradio.setChecked(true);
                        break;
                    case "tolls":
                        tollsradio.setChecked(true);
                        break;
                    case "highways":
                        highwaysradio.setChecked(true);
                        break;
                    case "ferries":
                        ferriesradio.setChecked(true);
                        break;

                }
            }
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setView(view);
        alertDialog.setIcon(android.R.drawable.stat_notify_error);
        alertDialog.setTitle("Travel Setting");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                ArrayList arrayforadd = new ArrayList();

                if (drivingradio.isChecked()) {

                    transport = "driving";
                } else if (walkingradio.isChecked()) {
                    transport = "walking";

                } else if (bicyclingradio.isChecked()) {
                    transport = "bicycling";

                } else if (transitradio.isChecked()) {
                    transport = "transit";

                }


                if (noradio.isChecked()) {
                    avoid = "";

                } else if (tollsradio.isChecked()) {
                    avoid = "tolls";

                } else if (highwaysradio.isChecked()) {
                    avoid = "highways";

                } else if (ferriesradio.isChecked()) {
                    avoid = "ferries";

                }
                arrayforadd.add(transport);
                arrayforadd.add(avoid);
                hasharraytonext.put(Position,arrayforadd);
                //arrayforadd.add(Position);
                if(typeof.equals("local")){

                    DataHolderClass.getInstancearraylistofradio2().setDistributor_idarraylistofradio2(hasharraytonext);


                }else {
                    DataHolderClass.getInstancearraylistofradio().setDistributor_idarraylistofradio(hasharraytonext);

                }
    //   System.out.println("isrunningwalking" + array);
                // MapsActivity.setNextDistanceURL(mContext);
                Toast.makeText(getActivity().getApplicationContext(), "Setting completed", Toast.LENGTH_SHORT).show();
                MapsActivity.setNextDistanceURL(getContext());

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity().getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        }).create();
        System.out.println("DialogFragment3rd");

        return alertDialog.show();
    }

    public static String getTransport() {
        return transport;

    }

    public static String getAvoid() {
        return avoid;

    }


}