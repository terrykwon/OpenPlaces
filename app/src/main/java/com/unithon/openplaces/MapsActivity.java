package com.unithon.openplaces;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.naver.speech.clientapi.SpeechConfig;
import com.unithon.openplaces.speech.AudioWriterPCM;
import com.unithon.openplaces.speech.NaverRecognizer;
import com.unithon.openplaces.speech.SampleSpeechActivity;

import java.lang.ref.WeakReference;

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

    private AutoCompleteTextView searchText;

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

//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MapsActivity.this);
                Intent intent = new Intent(MapsActivity.this, SampleSpeechActivity.class);
//                startActivity(intent, options.toBundle());
                startActivity(intent);
            }
        });

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // editText
        searchText = (AutoCompleteTextView) findViewById(R.id.toolbar_search);
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


        //===============================================================================================================

        ImageView btnStart = (ImageView) findViewById(R.id.toolbar_voice);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID, SPEECH_CONFIG);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecongizer by calling recognize().
                    mResult = "";
                    //txtResult.setText("Connecting...");
                    searchText.setText(R.string.str_listening);
                    isRunning = true;

                    naverRecognizer.recognize();
                } else {
                    // This flow is occurred by pushing start button again
                    // when SpeechRecognizer is running.
                    // Because it means that a user wants to cancel speech
                    // recognition commonly, so call stop().
                    //btnStart.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });

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
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // sample markers
        generateSampleMarkers();

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

    //debug
    private void generateSampleMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.5, 126.9))
                .title("Seoul")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple))
                .infoWindowAnchor(0.5f, 0.5f));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.51, 126.92))
                .title("Seoul")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple))
                .infoWindowAnchor(0.5f, 0.5f));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.52, 126.91))
                .title("Seoul")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple))
                .infoWindowAnchor(0.5f, 0.5f));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.15, 126.93))
                .title("Seoul")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple))
                .infoWindowAnchor(0.5f, 0.5f));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.54, 126.93))
                .title("Seoul")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple))
                .infoWindowAnchor(0.5f, 0.5f));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.56, 126.96))
                .title("Seoul")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple))
                .infoWindowAnchor(0.5f, 0.5f));mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.57, 126.92))
                .title("Seoul")
                .snippet("Population: 4,627,300")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_purple))
                .infoWindowAnchor(0.5f, 0.5f));


    }




//=====================ookoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo


    public static final String LOG_TAG = SampleSpeechActivity.class.getSimpleName();

    private static final String CLIENT_ID = Authentication.CLIENT_ID; // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    private static final SpeechConfig SPEECH_CONFIG = SpeechConfig.OPENAPI_KR; // or SpeechConfig.OPENAPI_EN

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private String mResult;

    private AudioWriterPCM writer;

    private boolean isRunning;

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                searchText.setText("준비됨");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                //searchText.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                String[] results = (String[]) msg.obj;
                mResult = results[0];
                searchText.setText(mResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                searchText.setText(mResult);
                //btnStart.setText(R.string.str_start);
                //btnStart.setEnabled(true);
                isRunning = false;
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                //searchText.setText(R.string.str_start);
                //btnStart.setEnabled(true);
                isRunning = false;
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // initialize() must be called on resume time.
        naverRecognizer.getSpeechRecognizer().initialize();

        mResult = "";
        //txtResult.setText("");
        //searchText.setText(R.string.str_start);
        //btnStart.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // release() must be called on pause time.
        naverRecognizer.getSpeechRecognizer().stopImmediately();
        naverRecognizer.getSpeechRecognizer().release();
        isRunning = false;
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<MapsActivity> mActivity;

        RecognitionHandler(MapsActivity activity) {
            mActivity = new WeakReference<MapsActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MapsActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }
}
