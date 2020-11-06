package com.example.android.newsapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsReport {
    public String headline;
    public String webUrl;
    public String pillarName;
    public String sectionName;
    public String authorName;
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

    public String getHeadline() {
        return headline;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getPillarName() {
        return pillarName;
    }

    public void setPillarName(String pillarName) {
        this.pillarName = pillarName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void setDate(String webPublicationDate) {
        this.date = parseDate(webPublicationDate);
    }
}
