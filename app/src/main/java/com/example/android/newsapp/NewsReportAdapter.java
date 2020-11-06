/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class NewsReportAdapter extends ArrayAdapter<NewsReport> {

    public NewsReportAdapter(Context context, List<NewsReport> reports) {
        super(context, 0, reports);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        NewsReport newsReport = getItem(position);

        TextView headlineView = (TextView) listItemView.findViewById(R.id.headline);
        headlineView.setText(newsReport.getHeadline());

        TextView pillarView = (TextView) listItemView.findViewById(R.id.pillar);
        pillarView.setText(newsReport.getPillarName());

        TextView sectionView = (TextView) listItemView.findViewById(R.id.section);
        sectionView.setText(newsReport.getSectionName());
        sectionView.setTextColor(getSectionColor(newsReport.getSectionName()));

        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(newsReport.getAuthorName());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(newsReport.getDate());
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(newsReport.getDate());
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private int getSectionColor(String section) {
        int colorResourceId;
        switch (section) {
            case "Politics":
                colorResourceId = R.color.politics;
                break;
            case "Football":
                colorResourceId = R.color.football;
                break;
            case "US news":
                colorResourceId = R.color.usNews;
                break;
            case "Business":
                colorResourceId = R.color.business;
                break;
            case "UK news":
                colorResourceId = R.color.ukNews;
                break;
            default:
                colorResourceId = R.color.worldNews;
                break;
        }
        return ContextCompat.getColor(getContext(), colorResourceId);
    }

}
