package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class IndikatorStrategis {


    private String status;

    @SerializedName("data-availability")
    private String dataAvailability;


    @SerializedName("data")
    List<Object>data=new ArrayList<Object>();

    public List<Object> getData(){
        data.add(new Page());
        data.add(new ArrayList<IndikatorStrategisItem>());
        return data;
    }

    public String getStatus() {
        return status;
    }

    public String getDataAvailability() {
        return dataAvailability;
    }



}


