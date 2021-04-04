package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
