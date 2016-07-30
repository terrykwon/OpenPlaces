package com.unithon.openplaces;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * On marker click
 */
public class CustomMarkerClickListener implements GoogleMap.OnMarkerClickListener {

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}
