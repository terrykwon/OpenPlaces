package com.unithon.openplaces.model;

/**
 * Created by choi on 2016-07-31.
 */
public class Result {
    /*"results" : [
      {
         "geometry" : {
            "location" : {
               "lat" : -33.86755700000001,
               "lng" : 151.201527
            },
            "viewport" : {
               "northeast" : {
                  "lat" : -33.86752310000001,
                  "lng" : 151.2020721
               },
               "southwest" : {
                  "lat" : -33.8675683,
                  "lng" : 151.2013453
               }
            }
         },
         "icon" : "https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png",
         "id" : "ce4ffe228ab7ad49bb050defe68b3d28cc879c4a",
         "name" : "Sydney Showboats",
         */
    private Locations geometry;
    private String name;

    public Locations getLocations() {
        return geometry;
    }

    public String getName() {
        return name;
    }
}
