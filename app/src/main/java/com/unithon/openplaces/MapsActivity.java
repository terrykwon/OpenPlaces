package com.unithon.openplaces;

import android.*;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private BottomSheetBehavior mBottomSheetBehavior;

    private boolean fabShown = true;

    private static final int STATE_SEARCHING = 0;
    private static final int STATE_VIEWING_MAP = 1;

    private static int STATE = STATE_VIEWING_MAP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFab = (FloatingActionButton) findViewById(R.id.FAB);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getBaseContext(), SampleSearchActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_up, R.anim.slide_up);

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MapsActivity.this);
                Intent intent = new Intent(MapsActivity.this, SampleSearchActivity.class);
                startActivity(intent, options.toBundle());
            }
        });

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // editText
        AutoCompleteTextView searchText = (AutoCompleteTextView) findViewById(R.id.toolbar_search);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MapsActivity.this);
                Intent intent = new Intent(MapsActivity.this, SearchActivity.class);
                startActivity(intent, options.toBundle());*/

                if (STATE == STATE_VIEWING_MAP) {
                    // Create new fragment and transaction
                    Fragment newFragment = SearchFragment.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager()
                            .beginTransaction();

                    // Replace whatever is in the fragment_container view with this fragment,
                    // and add the transaction to the back stack
                    transaction.add(R.id.map, newFragment);
                    transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                    // Commit the transaction
                    transaction.commit();

                    STATE = STATE_SEARCHING;
                }
            }
        });
        String[] countries = getResources().getStringArray(R.array.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        searchText.setAdapter(adapter);
        searchText.setThreshold(1);

        // set custom animation
        getWindow().setExitTransition(null);
        getWindow().setReenterTransition(null);

        initializeBottomSheet();
    }

    private void initializeBottomSheet() {
        View bottomSheet = findViewById(R.id.layout_panel);

        // Set up animations
        final Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_shrink);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
//        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (fabShown) {
                            mFab.startAnimation(shrinkAnimation);
                        } else {
                            mFab.startAnimation(growAnimation);
                        }
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mFab.setVisibility(View.VISIBLE);
                        fabShown = true;
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mFab.setVisibility(View.INVISIBLE);
                        fabShown = false;
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (STATE == STATE_SEARCHING) {
            STATE = STATE_VIEWING_MAP;
        }
        super.onBackPressed();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Remove directions, show in map button at bottom.
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMarkerClickListener(new CustomMarkerClickListener(mBottomSheetBehavior));
        mMap.setOnMyLocationButtonClickListener(new CustomMyLocationButtonClickListener());
        enableMyLocation();
        mMap.setOnMapClickListener(new CustomOnMapClickListener(mBottomSheetBehavior));
    }



    // 현재위치 버튼 enable.
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission missing
            Toast.makeText(this, "ACCESS_FINE_LOCATION permission missing", Toast.LENGTH_SHORT)
                    .show();

        } else {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}
