package com.bps_jatim_3500.statistik_jatim.model;

import com.google.gson.annotations.SerializedName;

public class PublikasiView {


    private String status;

    @SerializedName("data-availability")
    private String dataAvailability;


    @SerializedName("data")
    PublikasiViewItem data=new PublikasiViewItem();

    public PublikasiViewItem getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getDataAvailability() {
        return dataAvailability;
    }



}


