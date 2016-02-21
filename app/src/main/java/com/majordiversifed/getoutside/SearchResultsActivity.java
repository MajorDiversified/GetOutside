package com.majordiversifed.getoutside;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Intent intent = getIntent();
        int searchType = intent.getIntExtra("searchType", 5);
        double currentLat = intent.getDoubleExtra("currentLat", 10000);
        double currentLon = intent.getDoubleExtra("currentLon", 10000);
        double distance = intent.getDoubleExtra("distance", 100000);
        int limit = intent.getIntExtra("limit", 34453);
        List<Location> locs;
        if (searchType == 0) {
            locs = Utility.getLocationsByDistance(currentLat, currentLon, distance, limit);
        } else if (searchType == 1) {
            locs = Utility.getLocationsByRating(currentLat, currentLon, distance, limit);
        } else {
            locs = Utility.getLocationsByPopularity(currentLat, currentLon, distance, limit);
        }
        System.out.println("     B     " + locs.size());
        List<String> locsInfo = new ArrayList<>();
        for (int i = 0; i < locs.size(); i++) {
            locsInfo.add(i, "Location ID: " + locs.get(i).getId() + "\nRating: " + locs.get(i).getRating() + "\n(Lat, Lon) = (" + locs.get(i).getLat() + ", " + locs.get(i).getLon() + ")");
        }
        ListView results = (ListView) findViewById(R.id.results);
        ArrayAdapter<String> locsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, locsInfo);
        results.setAdapter(locsAdapter);
        results.setDividerHeight(20);
    }
}
