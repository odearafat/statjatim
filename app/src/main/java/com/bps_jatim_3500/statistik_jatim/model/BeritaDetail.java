package com.bps_jatim_3500.statistik_jatim.model;

import com.google.gson.annotations.SerializedName;

public class BeritaDetail {

    private String status;

    @SerializedName("data-availability")
    private String dataAvailability;


    @SerializedName("data")
    BeritaItemDetail data;

    public String getStatus() {
        return status;
    }
    public String getDataAvailability() {
        return dataAvailability;
    }
    public BeritaItemDetail getData() {
        return data;
    }
}
