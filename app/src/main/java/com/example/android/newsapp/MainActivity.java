package com.example.android.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * Constant value for the loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;
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

//        String[] array = {"aa", "bb"};
        adapter = new NewsReportAdapter(this, new ArrayList<NewsReport>());
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        listView.setAdapter(adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
//            android.app.LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
 //           loaderManager.initLoader(NEWS_LOADER_ID, null, (android.app.LoaderManager.LoaderCallbacks<Object>) this);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    newsReports = QueryUtils.fetchNewsReportData(GUARDIAN_REQUEST_URL);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Clear the adapter of previous earthquake data
//                            adapter.clear();

                            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
                            // data set. This will trigger the ListView to update.
                            if (newsReports != null && !newsReports.isEmpty()) {
                                List<String> reports = new ArrayList<String>();
                                for (NewsReport newsReport: newsReports) {
                                    reports.add(newsReport.headline);
                                }
                                adapter = new NewsReportAdapter(MainActivity.this,newsReports);
//                                adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, reports);
                                listView.setAdapter(adapter);

                                hideLoadingIndicator();

//                                adapter.addAll(reports);
                            }

                        }
                    });

                }
            });
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

/*
    @NonNull
    @Override
    public Loader<List<NewsReport>> onCreateLoader(int id, @Nullable Bundle args) {
        Loader loader= new NewsReportsLoader(this, GUARDIAN_REQUEST_URL);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsReport>> loader, List<NewsReport> data) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display
        emptyStateTextView.setText("No news found.");

        // Clear the adapter of previous earthquake data
        adapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            List<String> reports = new ArrayList<String>();
            for (NewsReport newsReport: data) {
                reports.add(newsReport.headline);
            }
            adapter.addAll(reports);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsReport>> loader) {
        adapter.clear();
    }

 */
}