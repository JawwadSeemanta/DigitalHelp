package com.drifters.help.MapActivities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drifters.help.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class RequestHelpUserLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;
    private Boolean mLocationPermissionsGranted = false;
    private double Latitude = 0.0, Longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help_user_location);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton fab = findViewById(R.id.fab_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Passing Locations to RequestHelpActivity
                Intent result = new Intent();
                result.putExtra("Lat", Latitude);
                result.putExtra("Lang", Longitude);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

        FloatingActionButton fab2 = findViewById(R.id.fab_refresh);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RequestHelpUserLocationActivity.this, "Refreshing", Toast.LENGTH_SHORT).show();
                getDeviceLocation();
            }
        });
        getLocationPermission();
        initializeMap();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            getDeviceLocation();
        }
    }

    public void initializeMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        } else {

            mLocationPermissionsGranted = true;
            initializeMap();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionsGranted = false;
        String[] perm = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE};

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            ActivityCompat.requestPermissions(this, perm, LOCATION_PERMISSION_REQUEST_CODE);
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initializeMap();
                }
            }
        }
    }

    private void getDeviceLocation() {
            if (mLocationPermissionsGranted) {

                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE};
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
                } else {

                    mFusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null && location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
                                        Latitude = location.getLatitude();
                                        Longitude = location.getLongitude();
                                    } else {
                                        Toast.makeText(RequestHelpUserLocationActivity.this, "Please Select Either \"Battery Saving\" or \"High Accuracy\" in Location Options", Toast.LENGTH_SHORT).show();
                                        //Open Location Settings
                                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 123);
                                        finish();
                                    }
                                    LatLng mLatLng = new LatLng(Latitude, Longitude);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 19));
                                }
                            })
                            .addOnFailureListener(this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RequestHelpUserLocationActivity.this, "Unable to Get Current Location", Toast.LENGTH_SHORT).show();
                                    Log.d("Location Failure", e.getMessage());
                                    finish();
                                }
                            });
                }

            }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getLocationPermission();
    }

    @Override
    public void onBackPressed() {
        // Passing Locations to RequestHelpActivity
        Intent result = new Intent();
        result.putExtra("Lat", Latitude);
        result.putExtra("Lang", Longitude);
        setResult(Activity.RESULT_OK, result);
        finish();
    }
}
