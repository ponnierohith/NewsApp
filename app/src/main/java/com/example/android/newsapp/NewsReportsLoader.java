package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsReportsLoader extends AsyncTaskLoader<List<NewsReport>> {

    /** Tag for log messages */
    private static final String LOG_TAG = NewsReportsLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link NewsReportsLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */



    public NewsReportsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsReport> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<NewsReport> newsReports = QueryUtils.fetchNewsReportData(mUrl);
        return newsReports;
    }
}