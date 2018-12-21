package com.drifters.help.MapActivities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.drifters.help.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendHelp_MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference databaseReference;
    private GoogleMap mMap;
    private String reqID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_help_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        final FloatingActionButton drive, done, refresh;
        drive = findViewById(R.id.sh_fab_drive);
        done = findViewById(R.id.sh_fab_done);
        refresh = findViewById(R.id.sh_fab_refresh);

        //Get Data from Previous
        reqID = getIntent().getStringExtra("id");
        if (getIntent().getBooleanExtra("accepted?", false)) {
            drive.setVisibility(View.GONE);
            done.setVisibility(View.VISIBLE);
        }

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
                                databaseReference = FirebaseDatabase.getInstance().getReference(reqID).child("status");
                                String s = "ACCEPTED";
                                databaseReference.setValue(s);
                                Toast.makeText(SendHelp_MapActivity.this, "Starting Driving", Toast.LENGTH_SHORT).show();
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

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Refreshing", Toast.LENGTH_SHORT).show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change status from "ACCEPTED" to "COMPLETE"
                databaseReference = FirebaseDatabase.getInstance().getReference(reqID).child("status");
                String s = "COMPLETE";
                databaseReference.setValue(s);
                Toast.makeText(v.getContext(), "Done", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
    }
}
