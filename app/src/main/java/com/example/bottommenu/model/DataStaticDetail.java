package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

public class DataStaticDetail {

    private String status;

    @SerializedName("data-availability")
    private String dataAvailability;


    @SerializedName("data")
    DataStaticItemDetail data;

    public String getStatus() {
        return status;
    }
    public String getDataAvailability() {
        return dataAvailability;
    }
    public DataStaticItemDetail getData() {
        return data;
    }
}
