package com.drifters.help.activityClass;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.drifters.help.MapActivities.RequestHelpUserLocationActivity;
import com.drifters.help.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestHelpActivity extends AppCompatActivity implements View.OnClickListener {

    Intent i;
    private Button req;
    private LinearLayout p, l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help);
        setTitle("Help Description");

        req = (Button) findViewById(R.id.req_help);

        p = (LinearLayout) findViewById(R.id.pic_ll);
        l = (LinearLayout) findViewById(R.id.loc_ll);

        req.setOnClickListener(this);

        p.setOnClickListener(this);
        l.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pic_ll:
                i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //Opens Default Camera
                startActivityForResult(i, 0);
                break;

            case R.id.loc_ll:
                i = new Intent(this, RequestHelpUserLocationActivity.class);
                startActivity(i);
                break;

            case R.id.req_help:
                //Dialogue Box
                new AlertDialog.Builder(this).setMessage("Do you want to submit your request?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        i = new Intent(RequestHelpActivity.this, CheckProgress_ViewActivity.class);
                        startActivity(i);
                        Toast.makeText(RequestHelpActivity.this, "Request Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

                break;

            default:
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this, "Image Captured", Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getCurrentTime(){

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");

        return df.format(Calendar.getInstance().getTime());

    }
}
