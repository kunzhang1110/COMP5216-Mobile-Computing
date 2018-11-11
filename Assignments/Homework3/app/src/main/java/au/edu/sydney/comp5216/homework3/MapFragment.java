package au.edu.sydney.comp5216.homework3;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;

    private TreeMap<DateTime, Location> runningStatistics = new TreeMap<>();
    private Polyline runningRoute;
    double distance;

    // widgets
    private ToggleButton btnTogStartPause;
    private TextView tvShowDistance;
    private TextView tvShowDuration;
    private TextView tvMapShowSpeed;
    private TextView tvMapShowPace;

    // constants
    private static final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 17;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = MapFragment.class.getSimpleName();
    private static final String KEY_LOCATION = "locations";     // Keys for storing activity state.

    //database fields
    RunningEntryDB  db;
    RunningEntryDao dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_map, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            runningStatistics = (TreeMap<DateTime, Location>) savedInstanceState.getSerializable(KEY_LOCATION);
        }

        View v = getView();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        btnTogStartPause = v.findViewById(R.id.btnTogStartPause);
        tvShowDistance= v.findViewById(R.id.tvShowDistance);
        tvShowDuration= v.findViewById(R.id.tvShowDuration);

        tvMapShowSpeed= v.findViewById(R.id.tvMapShowSpeed);
        tvMapShowPace= v.findViewById(R.id.tvMapShowPace);

        //Setup database
        db = RunningEntryDB.getDatabase(getActivity().getApplicationContext());
        dao = db.runningEntryDao();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Setup Buttons
        Button btnSaveRun = (Button) v.findViewById(R.id.btnSaveRun);

        Button btnMusic = (Button) v.findViewById(R.id.btnMusic);

        btnSaveRun.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
        //long durationMillis = runningStatistics.lastKey().getTime() - runningStatistics.firstKey().getTime();

                RunningEntry runningEntry;
                if(runningStatistics.size()!= 0) {
                    runningEntry = new RunningEntry(runningStatistics.lastKey().getMillis(),
                            runningStatistics.firstKey().getMillis(),
                            distance);
                } else{
                    runningEntry = new RunningEntry(new DateTime().getMillis()+1L,
                            new DateTime().getMillis(),
                            0.00);
                }

                saveToDatabase(runningEntry);
                Toast toast = Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT);
                toast.show();
                runningStatistics = new TreeMap<>(); // reset runningStatistics
                runningRoute.setPoints(new ArrayList<LatLng>()); //reset running route
            }
        });

        btnMusic.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                launchMusicPlayer();
            }
        });
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putSerializable(KEY_LOCATION, runningStatistics);
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        moveCamera(mDefaultLocation,1);
        // Set Start Pause Button
        setupBtnTogStartPause();

    }

    /*
    * Toggle Start and Stop Button
     */
    private void setupBtnTogStartPause(){
        btnTogStartPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){ //Start
                    try {
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());
                    } catch (SecurityException e){
                        e.getMessage();
                    }
                }else{ //Pause
                    distance = SphericalUtil.computeLength(runningRoute.getPoints());    //in meters
                    long durationMillis = runningStatistics.lastKey().getMillis() - runningStatistics.firstKey().getMillis();

                    float speed = StatCalculator.getSpeed((float) distance/1000, (float) durationMillis/1000/60);
                    float pace =  StatCalculator.getPace((float) distance/1000, (float) durationMillis/1000/60);

                    tvMapShowSpeed.setText(String.format(Locale.US,"%.2f", speed) + " km/hr");
                    tvMapShowPace.setText(String.format(Locale.US, "%.2f", pace) + " min/km");


                    tvShowDistance.setText(String.format(Locale.US,"%.2f", distance) + " m");
                    tvShowDuration.setText(StatCalculator.getTimeStringFromMillis(durationMillis));


                    mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                    Log.i(TAG, "Distance: " + distance + " Duration: " + getDurationString(durationMillis));
                }
            }
        });
    }



    private String getDurationString(Long _durationMillis){
        String durationString = String.format(Locale.US, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(_durationMillis),
                TimeUnit.MILLISECONDS.toMinutes(_durationMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(_durationMillis)),
                TimeUnit.MILLISECONDS.toSeconds(_durationMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(_durationMillis)));
        return durationString;
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions
     * in getLocationPermission()
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }


    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true); // false to disable my location button
                mMap.getUiSettings().setZoomControlsEnabled(true); // false to disable zoom controls
                mMap.getUiSettings().setCompassEnabled(true); // false to disable compass
                mMap.getUiSettings().setRotateGesturesEnabled(true); // false to disable rotate gesture

                //setup location requests
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(2000); // get location every 2 seconds
                mLocationRequest.setFastestInterval(2000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


                //setup polyline
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.CYAN);
                polylineOptions.width(15);
                runningRoute = mMap.addPolyline(polylineOptions);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private LocationCallback mLocationCallback = new LocationCallback(){

        @Override
        public void onLocationResult(LocationResult locationResult) {

            Location location = locationResult.getLastLocation();
            Log.i(TAG, "Location: " + " lat" +location.getLatitude() + " long" + location.getLongitude());
            updateRouteOnMap(location);
            moveCamera(location,500);
            //save time and location
            DateTime currentTime = new DateTime(); //get current time with System's Timezone
            runningStatistics.put(currentTime, location);
            Log.i(TAG,"Time:"+ runningStatistics.lastKey() + " Location:" + runningStatistics.lastEntry().getValue());
        }

    };


    private void updateRouteOnMap(@NonNull Location _location){
        List<LatLng> points = runningRoute.getPoints();
        points.add(new LatLng(_location.getLatitude(), _location.getLongitude()));
        runningRoute.setPoints(points);
    }


    // Move Camera based on LatLng
    private void moveCamera(LatLng _latlng, int durationMs){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(_latlng, DEFAULT_ZOOM),
                durationMs,
                null);
    }

    // Move Camera based on Location
    private void moveCamera(Location _location, int durationMs){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(_location.getLatitude(), _location.getLongitude()), DEFAULT_ZOOM),
                durationMs,
                null);
    }


    // Launch Music Player
    private void launchMusicPlayer(){
        try {
            Intent intent=Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MUSIC);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    //Database methods ------------------------------------------------
    private void saveToDatabase(RunningEntry runningEntry) {
        Log.i("xxx","saveToDatabase()"+runningEntry);
        new saveToDatabase(this, dao).execute(runningEntry);
    }


    private static class saveToDatabase extends AsyncTask<RunningEntry, Void, Void> {

        private RunningEntry runningEntry;
        private RunningEntryDao dao;

        saveToDatabase(MapFragment _mapFragment, RunningEntryDao _dao){
            this.dao = _dao;
        }

        protected Void doInBackground(RunningEntry... _runningEntries) {
            dao.insert(_runningEntries[0]);
            Log.i("xxx", "SQLite saved item: finish time = "+_runningEntries[0].getFinishTime());
            return null;
        }
    }
}
