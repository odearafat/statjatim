package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

public class BrsDetail {

    private String status;

    @SerializedName("data-availability")
    private String dataAvailability;


    @SerializedName("data")
    BrsItemDetail data;

    public String getStatus() {
        return status;
    }
    public String getDataAvailability() {
        return dataAvailability;
    }
    public BrsItemDetail getData() {
        return data;
    }

    @Override
    public String toString() {
        return "BrsDetail{" +
                "status='" + status + '\'' +
                ", dataAvailability='" + dataAvailability + '\'' +
                ", data=" + data +
                '}';
    }
}
