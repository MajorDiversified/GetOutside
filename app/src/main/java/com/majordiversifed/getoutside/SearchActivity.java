package com.majordiversifed.getoutside;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final EditText distanceParam = (EditText) findViewById(R.id.distanceSearch);
        final EditText limitParam = (EditText) findViewById(R.id.searchLimit);
        List<QueryType> queries = QueryType.getList();
        ArrayAdapter<QueryType> queryAdapter = new ArrayAdapter<QueryType>(this, android.R.layout.simple_spinner_dropdown_item, queries);
        final Spinner querySpinner = (Spinner) findViewById(R.id.spinnerSearchType);
        queryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        querySpinner.setAdapter(queryAdapter);
        Button search = (Button) findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String distanceText = distanceParam.getText().toString();
                String limitText = limitParam.getText().toString();
                QueryType query = (QueryType) querySpinner.getSelectedItem();
                double distance;
                int limit;
                try {
                    distance = Double.parseDouble(distanceText);
                    limit = Integer.parseInt(limitText);
                } catch (Exception e) {
                    distance = 100000;
                    limit = 16;
                }
                ArrayList<Location> locs;
                if (query.equals(QueryType.SEARCH_BY_DISTANCE)) {
                    locs = Utility.getLocationsByDistance(currentLat, currentLon, distance, limit);
                } else if (query.equals(QueryType.SEARCH_BY_RATING)) {
                    locs = Utility.getLocationsByRating(currentLat, currentLon, distance, limit);
                } else {
                    locs = Utility.getLocationsByPopularity(currentLat, currentLon, distance, limit);
                }
                Intent intent = new Intent(this, SearchResultsActivity.class);
                intent.putParcelableArrayListExtra("sortedList", locs);
                startActivity(intent);

            }
        });
    }
}
