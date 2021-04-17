package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

public class BrsItemDetail {

    private int brs_id;
    private String title;

    @SerializedName("abstract")
    private String abstract_brs;

    private String rl_date;
    private String updt_date;
    private String pdf;
    private String size;
    private String subj_id;
    private String subj;

    public int getBrs_id() {
        return brs_id;
    }
    public String getTitleBrs() {
        return title;
    }
    public String getAbstract_brs() { return abstract_brs; }
    public String getRl_date() {
        return rl_date;
    }
    public String getUpdt_date() {
        return updt_date;
    }
    public String getPdf() {
        return pdf;
    }
    public String getSizeBrs() { return size; }

    public String getSubj_id() {
        return subj_id;
    }

    public String getSubj() {
        return subj;
    }
}
