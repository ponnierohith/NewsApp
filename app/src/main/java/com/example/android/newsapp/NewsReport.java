package com.example.android.newsapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsReport {
    public String headline;
    public String webUrl;
    public String pillarName;
    public String sectionName;
    public Date date;

    public NewsReport(String headline, String webUrl, String pillarName, String sectionName, String webPublicationDate) {
        this.headline = headline;
        this.webUrl = webUrl;
        this.pillarName = pillarName;
        this.sectionName = sectionName;
        this.date = parseDate(webPublicationDate);
    }

    private Date parseDate(String webPublicationDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date = format.parse(webPublicationDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
