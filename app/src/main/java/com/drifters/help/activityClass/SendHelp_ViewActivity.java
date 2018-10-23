package com.drifters.help.activityClass;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.drifters.help.R;
import com.drifters.help.adapter.SendHelp_ViewAdapter;
import com.drifters.help.viewModel.SendHelp_ViewModel;

import java.util.ArrayList;

public class SendHelp_ViewActivity extends AppCompatActivity {

    private RecyclerView rv;
    private SendHelp_ViewAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_help_view);
        setTitle("Dashboard");

        rv = (RecyclerView) findViewById(R.id.view_recycler_view);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ad = new SendHelp_ViewAdapter(this, getData());

        rv.setAdapter(ad);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sh);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Reload Complete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<SendHelp_ViewModel> getData() {
        ArrayList<SendHelp_ViewModel> mod = new ArrayList<>();
        SendHelp_ViewModel m;

        m = new SendHelp_ViewModel();
        m.setReq_id("F-01");
        m.setName("Seemanta");
        m.setPhone("01714417008");
        m.setDistance("2.1 km");
        m.setReq_time("Sun, 9 Sep 2018, 13:45");
        m.setStatus("Pending");
        m.setImg(R.drawable.send_help_demo_background);
        mod.add(m);

        return mod;
    }

}
