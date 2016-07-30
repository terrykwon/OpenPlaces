package com.unithon.openplaces;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Terry Kwon on 7/30/2016.
 */
public class CustomMyLocationButtonClickListener implements GoogleMap.OnMyLocationButtonClickListener {
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
