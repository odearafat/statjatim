package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

public class BRSViewItem {
    int pub_id;
    String title, kat_no, pub_no, issn, abstract_pub, sch_date, rl_date, updt_date,
            cover, pdf,size;

    //public int getPage() {
    //    return pub_id;
    //}

    @SerializedName("abstract")
    public String getAbstract_pub(){
        return abstract_pub;
    }

    public int getPub_id() {
        return pub_id;
    }

    public String getTitle() {
        return title;
    }

    public String getKat_no() {
        return kat_no;
    }

    public String getPub_no() {
        return pub_no;
    }

    public String getIssn() {
        return issn;
    }

    public String getSch_date() {
        return sch_date;
    }

    public String getRl_date() {
        return rl_date;
    }

    public String getUpdt_date() {
        return updt_date;
    }

    public String getCover() {
        return cover;
    }

    public String getPdf() {
        return pdf;
    }

    public String getSize() {
        return size;
    }
}


