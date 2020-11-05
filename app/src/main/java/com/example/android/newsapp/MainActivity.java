package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsReport>> {
    private final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?&api-key=test";

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
        return new NewsReportsLoader(this, GUARDIAN_REQUEST_URL);
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