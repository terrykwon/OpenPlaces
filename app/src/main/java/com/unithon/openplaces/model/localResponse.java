package com.unithon.openplaces.model;

/**
 * Created by choi on 2016-07-30.
 */
public class LocalResponse {
    /*
    {
   "html_attributions" : [],
   "results" : [
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
         "opening_hours" : {
            "open_now" : false,
            "weekday_text" : []
         },
         "photos" : [
            {
               "height" : 750,
               "html_attributions" : [
                  "\u003ca href=\"https://maps.google.com/maps/contrib/107415973755376511005/photos\"\u003eSydney Showboats\u003c/a\u003e"
               ],
               "photo_reference" : "CoQBcwAAAGGnHEstSzRfuVP4-H4LL_qsbCyXEorzPCRSd5OswoH6b9DCLKqRqOU4Y4Bwjx9OquxfBk-zVsRtWqr0UCW6MxxfNelVsMk_pgw-UcGDCc3o641MK5uKRvC99rCblsAsH0rikAOr7N2G_KLSd9pw6p5pXFDmIx-6j1MCEUbk670NEhDqu6-pzD2_4StftlFnbQBGGhRN_YBdPyp1YhJlh5lhdfQ0w2AXag",
               "width" : 1181
            }
         ],
         "place_id" : "ChIJjRuIiTiuEmsRCHhYnrWiSok",
         "rating" : 4.3,
         "reference" : "CnRkAAAAXjZ3z0ER4gqBWzvHDQHjX6Y-9_CDoSsi9EZu4bWT9ustSgf0DXZigC55ab-x6f79MnleeVZImBhndfuPq6VjNQ9PIEqALmToR60wVhXLiNkIdlQ-prxgz4VA1dfnYzscnbtU0QVkkEBkxQowMJty1BIQxsIC24wqaIQaG-QacQacaBoUFZiqGMJcyhJBkWWM0PQigJ7XhVc",
         "scope" : "GOOGLE",
         "types" : [
            "travel_agency",
            "restaurant",
            "food",
            "point_of_interest",
            "establishment"
         ],
         "vicinity" : "King Street Wharf 5, Lime Street, Sydney"
      },
      {
         "geometry" : {
     */

    private String title;
    private String address;

    public void setTitle(String title) {
        this.title = title;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getTitle() {
        return title;
    }
    public String getAddress() {
        return address;
    }
}
