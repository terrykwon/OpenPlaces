package com.unithon.openplaces.network.response;

import java.util.ArrayList;

/**
 * Created by Noverish on 2016-07-31.
 */
public class DummyDatabase {
    private ArrayList<SearchResponse> responses = new ArrayList<>();

    private static DummyDatabase dummyDatabase = null;
    public static DummyDatabase getInstance() {
        if(dummyDatabase == null)
            dummyDatabase = new DummyDatabase();
        return dummyDatabase;
    }

    private DummyDatabase() {
        SearchResponse s1 = new SearchResponse();
        s1.setLat(37.5);
        s1.setLng(126.8);
        s1.setTitle("s1");

        responses.add(s1);

        SearchResponse s2 = new SearchResponse();
        s2.setLat(37.521);
        s2.setLng(126.81);
        s2.setTitle("s2");

        responses.add(s2);


        SearchResponse s3 = new SearchResponse();
        s3.setLat(37.552);
        s3.setLng(126.81);
        s3.setTitle("s3");

        responses.add(s3);

        SearchResponse s4 = new SearchResponse();
        s4.setLat(37.553);
        s4.setLng(126.81);
        s4.setTitle("s4");

        responses.add(s4);

        SearchResponse s5 = new SearchResponse();
        s5.setLat(37.557);
        s5.setLng(126.81);
        s5.setTitle("s5");

        responses.add(s5);
    }

    public ArrayList<SearchResponse> getResponses() {
        return responses;
    }
}
