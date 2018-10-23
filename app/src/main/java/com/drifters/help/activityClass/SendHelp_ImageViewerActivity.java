package com.drifters.help.activityClass;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.drifters.help.R;

public class SendHelp_ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
