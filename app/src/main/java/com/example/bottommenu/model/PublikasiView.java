package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

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


