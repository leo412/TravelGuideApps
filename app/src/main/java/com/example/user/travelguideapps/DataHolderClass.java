package com.example.user.travelguideapps;

/**
 * Created by User on 5/31/2017.
 */

public class DataHolderClass {
    private static DataHolderClass dataObject = null;
    private static DataHolderClass dataObject2 = null;

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




}