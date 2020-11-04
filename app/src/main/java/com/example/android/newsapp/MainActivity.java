package com.example.android.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    newsReports = QueryUtils.fetchNewsReportData(GUARDIAN_REQUEST_URL);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Clear the adapter of previous earthquake data
//                            adapter.clear();

                            if (newsReports != null && !newsReports.isEmpty()) {
                                List<String> reports = new ArrayList<String>();
                                for (NewsReport newsReport: newsReports) {
                                    reports.add(newsReport.headline);
                                }
                                adapter = new NewsReportAdapter(MainActivity.this,newsReports);
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

}