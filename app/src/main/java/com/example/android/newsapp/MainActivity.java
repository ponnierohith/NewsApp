package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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

    /** TextView that is displayed when the list is empty */
    private TextView emptyStateTextView;
    private List<NewsReport> newsReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list);
        listView = (ListView) findViewById(R.id.list);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyStateTextView);

        adapter = new NewsReportAdapter(this, new ArrayList<NewsReport>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsReport newsReport = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri uri = Uri.parse(newsReport.getWebUrl());

                // Create a new intent to view the URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, uri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

//         If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            hideLoadingIndicator();
            // Update empty state with no connection error message
            emptyStateTextView.setText("No internet connection");
        }

    }
    private void hideLoadingIndicator() {
        View loadingIndicator = findViewById(R.id.loading_indicator);
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
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display
        emptyStateTextView.setText("No news found.");
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