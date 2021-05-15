package com.bps_jatim_3500.statistik_jatim.model;

public class BeritaItemDetail {
    private int news_id;
    private String news_type;
    private String title;
    private String news;
    private String rl_date;
    private String picture;

    public int getNews_id() {
        return news_id;
    }

    public String getNews_type() {
        return news_type;
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

    public String getPicture() {
        return picture;
    }

    @Override
    public String toString() {
        return "BeritaItemDetail{" +
                "news_id=" + news_id +
                ", news_type='" + news_type + '\'' +
                ", title='" + title + '\'' +
                ", news='" + news + '\'' +
                ", rl_date='" + rl_date + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
