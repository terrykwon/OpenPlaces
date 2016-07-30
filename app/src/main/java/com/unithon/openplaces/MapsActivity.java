package com.unithon.openplaces;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.naver.speech.clientapi.SpeechConfig;
import com.unithon.openplaces.network.HttpFactory;
import com.unithon.openplaces.network.response.SearchResponse;
import com.unithon.openplaces.speech.AudioWriterPCM;
import com.unithon.openplaces.speech.NaverRecognizer;
import com.unithon.openplaces.speech.SampleSpeechActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private BottomSheetBehavior mBottomSheetBehavior;

    private boolean fabShown = true;

    private static final int STATE_SEARCHING = 0;
    private static final int STATE_VIEWING_MAP = 1;

    private static int STATE = STATE_VIEWING_MAP;

    // TextViews in BottomSheet
    private TextView PlaceNameTextView;
    private TextView PlaceOpenTextView;
    private TextView PlacePhoneNumTextView;
    private TextView PlaceAddressTextView;
    private TextView PlaceHoursTextView;

    private AutoCompleteTextView searchText;

    // Result of query.
    private Map<String, SearchResponse> placeInfoMap = Maps.newHashMap();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //check Manifest uses-permissions is on
        checkPermissions();

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
//                Intent intent = new Intent(MapsActivity.this, SampleSpeechActivity.class);
//                startActivity(intent, options.toBundle());
//                startActivity(intent);

                zoomToMyLocation();
            }
        });

        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        searchText.setAdapter(adapter);
        searchText.setThreshold(1);

        // TODO enter
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                 @Override
                                                 public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                                                     if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                         zoomToMyLocation();
                                                         getSupportFragmentManager().popBackStack();
                                                         STATE = STATE_VIEWING_MAP;
//                                                         getWindow().setSoftInputMode(
//                                                                 WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
//                                                         );
                                                         hideKeyboard(MapsActivity.this);

                                                         String title = searchText.getText().toString();
                                                         String region = "상암동";
                                                         Location location = mMap.getMyLocation();
                                                         Call<List<SearchResponse>> call = HttpFactory.search().search(title, location.getLatitude(), location.getLongitude(), region);

                                                         call.enqueue(new Callback<List<SearchResponse>>() {
                                                             @Override
                                                             public void onResponse(Call<List<SearchResponse>> call, Response<List<SearchResponse>> response) {
                                                                 if (response.isSuccessful()) {
                                                                     List<SearchResponse> responses = response.body();
                                                                     Log.d("testtest", responses.toString());
                                                                     renderMarkers(responses);
                                                                 }
                                                             }

                                                             @Override
                                                             public void onFailure(Call<List<SearchResponse>> call, Throwable t) {
                                                                 Log.d("search error:", " search error");
                                                             }
                                                         });

                                                         return true;
                                                     }
                                                     return false;
                                                 }
                                             }
        );

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

        //bottom sheet call set
        final LinearLayout callLayout = (LinearLayout) findViewById(R.id.bottomsheet_call);
        callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView telephone = (TextView) callLayout.findViewById(R.id.Telephone);
                String number = telephone.getText().toString();
                call(number);
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void darkenToolbarColor() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.bottomsheet_heading);
        TextView textView = (TextView) findViewById(R.id.StoreName);

        ObjectAnimator tabColorFade;
        tabColorFade = ObjectAnimator.ofObject(
                layout, "backgroundColor",
                new ArgbEvaluator(),
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.colorPrimary));

        tabColorFade.setDuration(1000);
        tabColorFade.start();

        textView.setTextColor(ContextCompat.getColor(this, R.color.white));

    }

    private void lightenToolbarColor() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.bottomsheet_heading);
        TextView textView = (TextView) findViewById(R.id.StoreName);

        ObjectAnimator tabColorFade;
        tabColorFade = ObjectAnimator.ofObject(
                layout, "backgroundColor",
                new ArgbEvaluator(),
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.white));

        tabColorFade.setDuration(500);
        tabColorFade.start();

        textView.setTextColor(ContextCompat.getColor(this, R.color.black));
    }

    private void initializeBottomSheet() {
        View bottomSheet = findViewById(R.id.layout_panel);

        PlaceNameTextView = (TextView) findViewById(R.id.StoreName);
        PlaceOpenTextView = (TextView) findViewById(R.id.Operating);
        PlacePhoneNumTextView = (TextView) findViewById(R.id.Telephone);
        PlaceAddressTextView = (TextView) findViewById(R.id.Address);
        PlaceHoursTextView = (TextView) findViewById(R.id.OperatingTime);

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
                            darkenToolbarColor();
                        } else {
                            mFab.startAnimation(growAnimation);
                        }
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mFab.setVisibility(View.VISIBLE);
                        fabShown = true;
                        lightenToolbarColor();
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

    // To be called when search button is pressed. Returns an ArrayList of responses.
    private ArrayList<SearchResponse> search() {
        ArrayList<SearchResponse> responses = new ArrayList<>();
        SearchResponse sr1 = new SearchResponse();

        return responses;
    }

    private String millisToTime(long openAt, long closeAt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(openAt);
        String open = String.format("%02d",calendar.get(Calendar.HOUR));
        String openMin = String.format("%02d",calendar.get(Calendar.MINUTE));
        String openAm = (calendar.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";

        calendar.setTimeInMillis(closeAt);
        String close = String.format("%02d",calendar.get(Calendar.HOUR));
        String closeMin = String.format("%02d",calendar.get(Calendar.MINUTE));
        String closeAm = (calendar.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM";

        return open + ":" + openMin + openAm + " ~ " + close + ":" + closeMin + closeAm;
    }

    private void renderMarkers(List<SearchResponse> responses) {
        for (SearchResponse r : responses) {
            LatLng latLng = new LatLng(r.getLat(), r.getLng());
            String status = r.getStatus();
            Long timeMillis = r.getCloseAt();
            Marker marker = null;

            if(status == null) {
                continue;
            }


            if (status.equals("OPEN")) {
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(r.getTitle())
                        .snippet(millisToTime(r.getOpenAt(), r.getCloseAt()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green))
                        .infoWindowAnchor(0.5f, 0.5f));
            } else if (status.equals("BEFORE_ONE_HOUR")){
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(r.getTitle())
                        .snippet(millisToTime(r.getOpenAt(), r.getCloseAt()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_yellow))
                        .infoWindowAnchor(0.5f, 0.5f));
            } else if (status.equals("CLOSE")) {
                marker =   mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(r.getTitle())
                        .snippet(millisToTime(r.getOpenAt(), r.getCloseAt()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
                        .infoWindowAnchor(0.5f, 0.5f));
            } else {
                marker =  mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(r.getTitle())
                        .snippet("정보없음")
                        .infoWindowAnchor(0.5f, 0.5f));
            }

            placeInfoMap.put(marker.getId(), r);


        }
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


        // Remove directions, show in map button at bottom.
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMyLocationButtonClickListener(new CustomMyLocationButtonClickListener());
        enableMyLocation();
        mMap.setOnMapClickListener(new CustomOnMapClickListener(mBottomSheetBehavior));

        //debug
//        ArrayList<SearchResponse> responses = DummyDatabase.getInstance().getResponses();
//        renderMarkers(responses);


    }

    private void zoomToMyLocation() {
        Location location = mMap.getMyLocation();
        LatLng latLng;

        latLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                14));
    }


    // 현재위치 enable.
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

    private ArrayList<SearchResponse> generateSampleMarkers() {
        ArrayList<SearchResponse> responses = new ArrayList<>();

        SearchResponse s1 = new SearchResponse();
        s1.lat = 37.5;
        s1.lng = 126.8;
        s1.title = "킨코스코리아 마포지점";

        responses.add(s1);

        SearchResponse s2 = new SearchResponse();
        s2.lat = 37.521;
        s2.lng = 126.81;
        s2.title = "s2";

        responses.add(s2);

        SearchResponse s3 = new SearchResponse();
        s3.lat = 37.552;
        s3.lng = 126.81;
        s3.title = "s3";

        responses.add(s3);

        SearchResponse s4 = new SearchResponse();
        s4.lat = 37.553;
        s4.lng = 126.81;
        s4.title = "s4";
        responses.add(s4);
        return responses;
    }

    //phone call
    private void call(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("-","");
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(intent);
        } catch (Exception ex) {
            Toast.makeText(this,"통화가 안돠용",Toast.LENGTH_LONG).show();
        }
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        String id = marker.getId();

        SearchResponse response = placeInfoMap.get(id);

        PlaceNameTextView.setText(response.getTitle());
        PlacePhoneNumTextView.setText(response.getTel());
        PlaceAddressTextView.setText(response.getAddress());
        PlaceOpenTextView.setText(response.getStatus());
        return false;
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

    private boolean checkPermissions() {
        int res = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if(res == PackageManager.PERMISSION_GRANTED) {
            Log.e("RECORD_AUDIO","ok");
        } else {
            Log.e("RECORD_AUDIO","NOT OK");

        }

        res = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        if(res == PackageManager.PERMISSION_GRANTED) {
            Log.e("CALL_PHONE","ok");
        } else {
            Log.e("CALL_PHONE","NOT OK");

        }

        res = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(res == PackageManager.PERMISSION_GRANTED) {
            Log.e("WRITE_EXTERNAL_STORAGE","ok");
        } else {
            Log.e("WRITE_EXTERNAL_STORAGE","NOT OK");

        }

        res = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(res == PackageManager.PERMISSION_GRANTED) {
            Log.e("ACCESS_COARSE_LOCATION","ok");
        } else {
            Log.e("ACCESS_COARSE_LOCATION","NOT OK");

        }

        res = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(res == PackageManager.PERMISSION_GRANTED) {
            Log.e("ACCESS_FINE_LOCATION","ok");
        } else {
            Log.e("ACCESS_FINE_LOCATION","NOT OK");

        }

        res = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if(res == PackageManager.PERMISSION_GRANTED) {
            Log.e("INTERNET","ok");
        } else {
            Log.e("INTERNET","NOT OK");
        }

        return true;
    }
}
