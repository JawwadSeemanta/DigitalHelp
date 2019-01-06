package com.drifters.help.activityClass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.drifters.help.ConnectionCheck;
import com.drifters.help.DataModel.RequestModel;
import com.drifters.help.R;
import com.drifters.help.adapter.SendHelp_ViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SendHelp_ViewActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;

    private TextView textView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SendHelp_ViewAdapter adapter;
    private ArrayList<RequestModel> viewModels;
    private RequestModel m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_help_view);
        setTitle("Dashboard");

        textView = findViewById(R.id.tv_sh);
        progressBar = findViewById(R.id.progressbar_sendhelp_activity);
        recyclerView = findViewById(R.id.view_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1234);
        }

        //Create Database References
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FloatingActionButton fab = findViewById(R.id.fab_sh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loggedOut();
                startActivity(new Intent(SendHelp_ViewActivity.this, LoginActivity.class));
                Toast.makeText(SendHelp_ViewActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (ConnectionCheck.isOnline(this)) {
            viewModels = new ArrayList<>();
            adapter = new SendHelp_ViewAdapter(SendHelp_ViewActivity.this, viewModels);
            recyclerView.setAdapter(adapter);

            Query firebaseQuery = databaseReference.orderByChild("request_type").startAt(getHelperType()).endAt(getHelperType());
            valueEventListener = firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    viewModels.clear();
                    for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                        m = reqSnapshot.getValue(RequestModel.class);
                        viewModels.add(m);
                    }
                    if (viewModels.isEmpty()) {
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        textView.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SendHelp_ViewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(SendHelp_ViewActivity.this, "Please Enable Internet and Try Again!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void loggedOut() {
        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("login_status", false);
        editor.commit();
    }

    private String getHelperType() {
        SharedPreferences sharedPref = this.getSharedPreferences("prefType", Context.MODE_PRIVATE);

        return sharedPref.getString("rType", null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
}
