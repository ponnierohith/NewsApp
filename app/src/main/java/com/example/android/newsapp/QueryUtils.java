package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<NewsReport> fetchNewsReportData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<NewsReport> newsReports = extractFeatureFromJson(jsonResponse);
        sort(newsReports);
        return newsReports;
    }

    public static void sort(List<NewsReport> newsReports) {
        Collections.sort(newsReports, (report1, report2) -> {
//            int result = report1.getPillarName().compareTo(report2.getPillarName());
//            if (result != 0) return result;
//            result = report1.getSectionName().compareTo(report2.getSectionName());
//            if (result != 0) return result;
            int result = report2.getDate().compareTo(report1.getDate());
            return result;
        });
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsReport> extractFeatureFromJson(String newsReportJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsReportJSON)) {
            return null;
        }

        List<NewsReport> newsReports = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(newsReportJSON);

            JSONObject responseJsonResponse = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = responseJsonResponse.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentNews = resultsArray.getJSONObject(i);

                NewsReport newsReport = new NewsReport(currentNews.getString("webTitle"));
                if (currentNews.has("webUrl")) {
                    newsReport.setWebUrl(currentNews.getString("webUrl"));
                }
                if (currentNews.has("pillarName")) {
                    newsReport.setPillarName(currentNews.getString("pillarName"));
                }
                if (currentNews.has("sectionName")) {
                    newsReport.setSectionName(currentNews.getString("sectionName"));
                }
                if (currentNews.has("webPublicationDate")) {
                    newsReport.setDate(currentNews.getString("webPublicationDate"));
                }
                if (currentNews.has("tags")) {
                    JSONArray tagsArray = currentNews.getJSONArray("tags");
                    newsReport.setAuthorName(parseAuthorName(tagsArray));
                }
                newsReports.add(newsReport);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return newsReports;
    }

    private static String parseAuthorName(JSONArray tags) {
        try {
            for (int i = 0; i < tags.length(); i++) {
                JSONObject tag = tags.getJSONObject(i);
                if (tag.has("type") && (tag.getString("type").contentEquals("contributor"))) {
                    if (tag.has("webTitle")) return tag.getString("webTitle");
                    if (tag.has("firstName") && tag.has("lastName"))
                        return tag.getString("firstName") + tag.getString("lastName");
                }
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON tags", e);
        }
        return null;
    }

}
