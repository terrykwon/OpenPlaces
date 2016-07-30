package com.unithon.openplaces;

import android.support.design.widget.BottomSheetBehavior;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * On marker click
 */
public class CustomMarkerClickListener implements GoogleMap.OnMarkerClickListener {
    private BottomSheetBehavior bottomSheetBehavior;

    public CustomMarkerClickListener(BottomSheetBehavior bottomSheetBehavior) {
        this.bottomSheetBehavior = bottomSheetBehavior;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        return false;
    }

}
