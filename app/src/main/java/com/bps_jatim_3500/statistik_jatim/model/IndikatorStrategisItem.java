package com.bps_jatim_3500.statistik_jatim.model;

import com.google.gson.annotations.SerializedName;

public class IndikatorStrategisItem {

    private String title;

    @SerializedName("name")
    private String desc;
    private String data_source;

    private Integer indicator_id;

    @SerializedName("value")
    private double nilai;
    private String unit;

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getData_source() {
        return data_source;
    }

    public double getNilai() {
        return nilai;
    }

    public String getUnit() {
        return unit;
    }

    public Integer getIndicator_id() {
        return indicator_id;
    }
}

