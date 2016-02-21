package com.majordiversifed.getoutside;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrew on 2/21/16.
 */
public enum QueryType {

    SEARCH_BY_DISTANCE("Search by Distance"),
    SEARCH_BY_RATING("Search by Rating"),
    SEARCH_BY_POPULARITY("Search by Popularity");
    private String display;
    QueryType(String display) {
        this.display = display;
    }

    public static List<QueryType> getList() {
        return Arrays.asList(QueryType.values());
    }
}
