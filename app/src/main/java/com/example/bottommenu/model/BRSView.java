package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

public class BRSView {


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


