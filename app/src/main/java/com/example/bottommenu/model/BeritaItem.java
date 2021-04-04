package com.example.bottommenu.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BeritaItem{

    private int news_id;
    private String newscat_id;
    private String newscat_name;
    private String title;
    private String news;
    private String rl_date;

    public int getNews_id() {
        return news_id;
    }

    public String getNewscat_id() {
        return newscat_id;
    }

    public String getNewscat_name() {
        return newscat_name;
    }

    public String getTitle() {
        return title;
    }

    public String getNews() {
        return news;
    }

    public String getRl_date() {
        return rl_date;
    }
}

