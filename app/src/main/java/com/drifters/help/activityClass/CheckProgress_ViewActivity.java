package com.drifters.help.activityClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.drifters.help.MapActivities.CheckProgress_MapsActivity;
import com.drifters.help.R;
import com.drifters.help.adapter.CheckProgressAdapter;
import com.drifters.help.viewModel.CheckProgressModel;

import java.util.ArrayList;

public class CheckProgress_ViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CheckProgressAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_progress);
        setTitle("Progress");

        recyclerView = (RecyclerView) findViewById(R.id.progress_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CheckProgressAdapter(this, get_Data_for_Model());

        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_cp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Reload Complete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private ArrayList<CheckProgressModel> get_Data_for_Model() {
        ArrayList<CheckProgressModel> array_of_models = new ArrayList<>();
        CheckProgressModel m;

        m = new CheckProgressModel();
        m.setName("Seemanta");
        m.setPhone("01714417008");
        m.setHelp_type("Fire Service");
        m.setStatus("Requested");
        m.setSub_time("Sun, 9 Sep 2018, 13:30");
        m.setImg(R.drawable.send_help_demo_background);
        array_of_models.add(m);

        return array_of_models;
    }

    @Override
    public void onBackPressed() {
        Intent i;
        i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}


