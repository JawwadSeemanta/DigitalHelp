package com.drifters.help.activityClass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.drifters.help.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");
    }
}
