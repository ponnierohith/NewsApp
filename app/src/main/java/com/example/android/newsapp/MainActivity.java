package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsReport>> {
    private final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?&api-key=test";
    private final String GUARDIAN_BASE_URL = "https://content.guardianapis.com/search?";

    private NewsReportAdapter adapter;
    private ListView listView;
    private TextView emptyStateTextView;
    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);

        listView = findViewById(R.id.list);
        emptyStateTextView = findViewById(R.id.empty_view);
        loadingIndicator = findViewById(R.id.loading_indicator);
        adapter = new NewsReportAdapter(this, new ArrayList<>());

        listView.setEmptyView(emptyStateTextView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            NewsReport newsReport = adapter.getItem(position);
            if (newsReport != null) {
                Uri uri = Uri.parse(newsReport.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(websiteIntent);
            }
            else {

            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            hideLoadingIndicator();
            emptyStateTextView.setText(R.string.NoInternetConnection);
        }

    }
    private void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    //    @NonNull
    @Override
    public Loader<List<NewsReport>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri = Uri.parse(GUARDIAN_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", "news");
        uriBuilder.appendQueryParameter("api-key", "test");

        return new NewsReportsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsReport>> loader, List<NewsReport> data) {
        hideLoadingIndicator();
        // Set empty state text to display
        emptyStateTextView.setText(R.string.NoNewsFound);
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter = new NewsReportAdapter(MainActivity.this, data);
            listView.setAdapter(adapter);
            hideLoadingIndicator();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsReport>> loader) {
        adapter.clear();
    }
}