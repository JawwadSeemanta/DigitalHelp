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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RequestHelpUserLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    FusedLocationProviderClient mFusedLocationProviderClient;

    private GoogleMap mMap;
    private Boolean mLocationPermissionsGranted = false;
    private double Latitude = 0.0, Longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help_user_location);

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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

            mMap.setIndoorEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);

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
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionsGranted = true;
            initializeMap();
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
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

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            //Get Location With Network Provider
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                Latitude = currentLocation.getLatitude();
                                Longitude = currentLocation.getLongitude();
                            } else {
                                Toast.makeText(RequestHelpUserLocationActivity.this, "Please Select Either \"Battery Saving\" or \"High Accuracy\" in Location Options", Toast.LENGTH_SHORT).show();

                                //Open Location Settings
                                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 123);
                            }
                            LatLng mLatLng = new LatLng(Latitude, Longitude);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 18));

                        }
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(RequestHelpUserLocationActivity.this, "Unable to Get Current Location", Toast.LENGTH_SHORT).show();
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
