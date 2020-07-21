package com.example.android.news;

/**
 * Created by HP on 6/17/2017.
 */

public class NewsData {

   public final String newsSource;

    public final String title;

    public final String desc;

    public final String newsUrl;

    public final String newsImageUrl;

    public final String time;


    public NewsData(String newsSource, String title, String desc, String newsUrl, String newsImageUrl, String time) {

        this.newsSource = newsSource;
        this.title = title;
        this.desc = desc;
        this.newsUrl = newsUrl;
        this.newsImageUrl = newsImageUrl;
        this.time = time;
    }
}
