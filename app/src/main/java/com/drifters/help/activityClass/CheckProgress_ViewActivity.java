package com.drifters.help.activityClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.drifters.help.adapter.CheckProgressAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CheckProgress_ViewActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private ValueEventListener valueEventListener;

    private TextView textView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private CheckProgressAdapter adapter;
    private ArrayList<RequestModel> array_of_models;
    private RequestModel m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_progress);
        setTitle("Progress");

        //Create Database References
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();

        progressBar = findViewById(R.id.progressbar_check_activity);
        recyclerView = (RecyclerView) findViewById(R.id.progress_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textView = findViewById(R.id.tv_prog);


    }

    @Override
    public void onBackPressed() {
        Intent i;
        i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private int getUserID() {
        SharedPreferences sharedPref = this.getSharedPreferences("prefUID", Context.MODE_PRIVATE);
        return sharedPref.getInt("appId", 0);
    }

    //Use this onStart to get some data
    @Override
    protected void onStart() {
        super.onStart();

        int uID = getUserID();

        if (ConnectionCheck.isOnline(this)) {
            array_of_models = new ArrayList<>();
            adapter = new CheckProgressAdapter(CheckProgress_ViewActivity.this, array_of_models);
            recyclerView.setAdapter(adapter);

            //For Querying the Database
            Query firebaseQuery = databaseReference.orderByChild("userID").startAt(uID).endAt(uID);

            valueEventListener = firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    array_of_models.clear();

                    for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                        m = reqSnapshot.getValue(RequestModel.class);
                        array_of_models.add(m);
                    }
                    if (array_of_models.isEmpty()) {
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
                    Toast.makeText(CheckProgress_ViewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(CheckProgress_ViewActivity.this, "Please Enable Internet and Try Again!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }

    //Use this onStart if you need all the data in the database
    /*@Override
    protected void onStart() {
        super.onStart();

        if (ConnectionCheck.isOnline(this)) {
            array_of_models = new ArrayList<>(); //To keep all the models

            //Getting "All" Data for Models from Database
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                // If Data in Database changes, this method runs
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    array_of_models.clear();

                    //Get data from Database
                    for (DataSnapshot reqSnapshot : dataSnapshot.getChildren()) {
                        //This gets a child of the database as a model class
                        m = reqSnapshot.getValue(RequestModel.class);

                        //This adds the gotten model in the list of models
                        array_of_models.add(m);
                    }

                    //Set the data using adapter
                    adapter = new CheckProgressAdapter(CheckProgress_ViewActivity.this, array_of_models);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CheckProgress_ViewActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            Toast.makeText(CheckProgress_ViewActivity.this, "Please Enable Internet and Try Again!", Toast.LENGTH_SHORT).show();
        }

    }*/


}


