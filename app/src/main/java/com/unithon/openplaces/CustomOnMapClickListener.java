package com.unithon.openplaces;

import android.support.design.widget.BottomSheetBehavior;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Terry Kwon on 7/30/2016.
 */
public class CustomOnMapClickListener implements GoogleMap.OnMapClickListener {

    private BottomSheetBehavior bottomSheetBehavior;

    public CustomOnMapClickListener(BottomSheetBehavior bottomSheetBehavior) {
        this.bottomSheetBehavior = bottomSheetBehavior;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

}
