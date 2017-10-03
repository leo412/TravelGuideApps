package com.example.user.travelguideapps;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 5/31/2017.
 */

public class DataHolderClass {
    private static DataHolderClass dataObject = null;
    private static DataHolderClass dataObject2 = null;
    private static DataHolderClass dataObject3 = null;
    private static DataHolderClass dataObjectarray = null;
    private static DataHolderClass dataObject4 = null;
    private static DataHolderClass arraylistofradio = null;

    private static DataHolderClass arraylistofradio2 = null;
    private static DataHolderClass booleanhaschanges = null;

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






    public static DataHolderClass getInstance4() {
        if (dataObject4 == null)
            dataObject4 = new DataHolderClass();
        return dataObject4;
    }
    private ArrayList distributor_id4;;

    public ArrayList getDistributor_id4() {
        return distributor_id4;
    }

    public void setDistributor_id4(ArrayList distributor_id4) {
        this.distributor_id4 = distributor_id4;
    }




    public static DataHolderClass getInstancearraylistofradio2() {
        if (arraylistofradio2 == null)
            arraylistofradio2 = new DataHolderClass();
        return arraylistofradio2;
    }
    private HashMap<Integer,ArrayList> distributor_idarraylistofradio2;

    public HashMap<Integer,ArrayList>getDistributor_idarraylistofradio2() {
        return distributor_idarraylistofradio2;
    }

    public void setDistributor_idarraylistofradio2( HashMap<Integer,ArrayList> distributor_idarraylistofradio2) {
        this.distributor_idarraylistofradio2 = distributor_idarraylistofradio2;
    }



    public static DataHolderClass getInstancearraylistofradio() {
        if (arraylistofradio == null)
            arraylistofradio = new DataHolderClass();
        return arraylistofradio;
    }
    private HashMap<Integer,ArrayList> distributor_idarraylistofradio;

    public HashMap<Integer,ArrayList>getDistributor_idarraylistofradio() {
        return distributor_idarraylistofradio;
    }

    public void setDistributor_idarraylistofradio( HashMap<Integer,ArrayList> distributor_idarraylistofradio) {
        this.distributor_idarraylistofradio = distributor_idarraylistofradio;
    }








    public static DataHolderClass getBooleanhaschanges() {
        if (booleanhaschanges == null)
            booleanhaschanges = new DataHolderClass();
        return booleanhaschanges;
    }
    private boolean distributor_idbooleanhaschanges;

    public boolean getbooleanhaschanges() {
        return distributor_idbooleanhaschanges;
    }

    public void setdistributor_idbooleanhaschanges( boolean distributor_idbooleanhaschanges) {
        this.distributor_idbooleanhaschanges = distributor_idbooleanhaschanges;
    }
}