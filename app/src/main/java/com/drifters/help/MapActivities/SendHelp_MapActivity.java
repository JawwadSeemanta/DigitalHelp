package com.drifters.help.MapActivities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.drifters.help.ConnectionCheck;
import com.drifters.help.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendHelp_MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference databaseReference;
    private GoogleMap mMap;
    private String reqID;
    
    protected LatLng end;

    int times = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_help_map);
        final FloatingActionButton drive, done, reset;
        drive = findViewById(R.id.sh_fab_drive);
        done = findViewById(R.id.sh_fab_done);
        reset = findViewById(R.id.fab_reset);

        //Get Data from Previous
        reqID = getIntent().getStringExtra("id");
        if (getIntent().getBooleanExtra("accepted?", false)) {
            drive.setVisibility(View.GONE);
            done.setVisibility(View.VISIBLE);
        }
        double dest_Latitute = getIntent().getDoubleExtra("dest_lat", 0);
        double dest_Longitude = getIntent().getDoubleExtra("dest_lon", 0);
        end = new LatLng(dest_Latitute, dest_Longitude);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map2, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Do you want to respond to this request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Actions On Click Yes Button
                                drive.setVisibility(View.GONE);
                                done.setVisibility(View.VISIBLE);
                                // Change status from "REQUESTED" to "ACCEPTED
                                //Update Data
                                if (ConnectionCheck.isOnline(SendHelp_MapActivity.this)) {
                                    databaseReference = FirebaseDatabase.getInstance().getReference(reqID).child("status");
                                    String s = "ACCEPTED";
                                    databaseReference.setValue(s);
                                    Toast.makeText(SendHelp_MapActivity.this, "Request Accepted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change status from "ACCEPTED" to "COMPLETE"
                databaseReference = FirebaseDatabase.getInstance().getReference(reqID).child("status");
                String s = "COMPLETE";
                databaseReference.setValue(s);
                Toast.makeText(v.getContext(), "Thank You For Your Hard Work.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(end, 16));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setTrafficEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 124);
        }

        mMap.addMarker(new MarkerOptions().position(end).title("Location of Incident").draggable(false)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(end, 16));
    }
}
