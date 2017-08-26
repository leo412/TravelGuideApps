package com.example.user.travelguideapps;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by User on 5/31/2017.
 */

public class DataHolderClass {
    private static DataHolderClass dataObject = null;
    private static DataHolderClass dataObject2 = null;
    private static DataHolderClass dataObject3 = null;
    private static DataHolderClass dataObjectarray = null;

    private DataHolderClass() {
        // left blank intentionally
    }

    public static DataHolderClass getInstance() {
        if (dataObject == null)
            dataObject = new DataHolderClass();
        return dataObject;
    }
    private String distributor_id;;

    public String getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(String distributor_id) {
        this.distributor_id = distributor_id;
    }




    public static DataHolderClass getInstance2() {
        if (dataObject2 == null)
            dataObject2 = new DataHolderClass();
        return dataObject2;
    }
    private String distributor_id2;;

    public String getDistributor_id2() {
        return distributor_id2;
    }
    public void setDistributor_id2(String distributor_id2) {
        this.distributor_id2 = distributor_id2;
    }


    public static DataHolderClass getInstance3() {
        if (dataObject3 == null)
            dataObject3 = new DataHolderClass();
        return dataObject3;
    }
    private ArrayList distributor_id3;;

    public ArrayList getDistributor_id3() {
        return distributor_id3;
    }

    public void setDistributor_id3(ArrayList distributor_id3) {
        this.distributor_id3 = distributor_id3;
    }




    public static DataHolderClass getInstancearray() {
        if (dataObjectarray == null)
            dataObjectarray = new DataHolderClass();
        return dataObjectarray;
    }


    private static ArrayList distributor_idarray= new ArrayList();
//ChIJgRkpDSxPzDERtOZ78kN6b74
    //ChIJMwcN545JzDERT2W3Zya5af0
    //ChIJgRkpDSxPzDERtOZ78kN6b74

    public String getDistributor_idarray() {
        Log.d("Dataholder", "dataholdercheck get 1 "+distributor_idarray);
        Log.d("Dataholder", "dataholdercheck get 2 "+distributor_idarray.get(distributor_idarray.size()-1).toString());

        return distributor_idarray.get(distributor_idarray.size()-1).toString();
    }

    public void setDistributor_idarray(String distributor_idarray) {
        Log.d("Dataholder", "dataholdercheck set 1 "+distributor_idarray);

        this.distributor_idarray.add(distributor_idarray);
        Log.d("Dataholder", "dataholdercheck set 2 "+this.distributor_idarray);

    }

    public static void removeDistributor_idarray() {


        distributor_idarray.remove(distributor_idarray.size()-1);
        Log.d("Dataholder", "dataholdercheck remove 1 "+distributor_idarray);

    }

}