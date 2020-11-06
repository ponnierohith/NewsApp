package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsReportsLoader extends AsyncTaskLoader<List<NewsReport>> {

    private String mUrl;

    public NewsReportsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsReport> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchNewsReportData(mUrl);
    }
}