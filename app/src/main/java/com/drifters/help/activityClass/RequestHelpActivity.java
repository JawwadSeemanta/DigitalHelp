package com.drifters.help.activityClass;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.drifters.help.MapActivities.RequestHelpUserLocationActivity;
import com.drifters.help.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RequestHelpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button req;
    private LinearLayout p, l;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath = null;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help);
        setTitle("Help Description");

        getDatafromPrevious();
        req = findViewById(R.id.req_help);
        p = findViewById(R.id.pic_ll);
        l = findViewById(R.id.loc_ll);
        req.setOnClickListener(this);
        p.setOnClickListener(this);
        l.setOnClickListener(this);

    }

    private void getDatafromPrevious() {
        Intent intent = getIntent();
        TAG = intent.getStringExtra("Tag");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pic_ll:
                dispatchTakePictureIntent();
                break;

            case R.id.loc_ll:
                Intent i2 = new Intent(this, RequestHelpUserLocationActivity.class);
                startActivity(i2);
                break;

            case R.id.req_help:
                //Dialogue Box
                new AlertDialog.Builder(this).setMessage("Do you want to submit your request?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i3 = new Intent(RequestHelpActivity.this, CheckProgress_ViewActivity.class);
                        startActivity(i3);
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

    //Needed for Submission Time Generation
    public String getCurrentTime() {

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        return df.format(Calendar.getInstance().getTime());
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = TAG + "_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.drifters.help.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, mCurrentPhotoPath, Toast.LENGTH_LONG).show();
    }
}