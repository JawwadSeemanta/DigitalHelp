package com.drifters.help.activityClass;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.drifters.help.ConnectionCheck;
import com.drifters.help.DataModel.RequestModel;
import com.drifters.help.ImageCompression;
import com.drifters.help.MapActivities.RequestHelpUserLocationActivity;
import com.drifters.help.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestHelpActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_LOCATION = 2;
    public Button loc_b;
    ProgressBar progressBar;
    String mCurrentPhotoPath = null;
    String imageFileName = null;
    String mCompressedPhotoPath;
    String ImageURL = null;
    EditText name, phone;
    Button pic_b;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private Button req;
    private LinearLayout p, l;
    private String TAG;
    private double Latitude = 0.0, Longitude = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_help);

        getDatafromPrevious();

        setTitle(TAG);


        req = findViewById(R.id.req_help);
        p = findViewById(R.id.pic_ll);
        l = findViewById(R.id.loc_ll);
        req.setOnClickListener(this);
        p.setOnClickListener(this);
        l.setOnClickListener(this);


        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        loc_b = findViewById(R.id.loc_button);
        pic_b = findViewById(R.id.pic_button);
        progressBar = findViewById(R.id.pb);

        progressBar.setVisibility(View.GONE);

        //Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

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
                startActivityForResult(i2, REQUEST_LOCATION);
                break;

            case R.id.req_help:

                //Dialogue Box
                new AlertDialog.Builder(this).setMessage("Do you want to submit your request?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Upload Data to Firebase Realtime Database
                        if (ConnectionCheck.isOnline(RequestHelpActivity.this)) {
                            addFirebaseEntry();
                        } else {
                            Toast.makeText(RequestHelpActivity.this, "Please Enable Internet and Try Again!", Toast.LENGTH_SHORT).show();
                        }


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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = TAG + "_" + timeStamp;
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
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
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
        //Check if the image was taken
        if (mCurrentPhotoPath != null) {
            File f = new File(mCurrentPhotoPath);
            if (f.length() / 1024 < 50) {
                f.delete();
                mCurrentPhotoPath = null;
                pic_b.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_help_desc_picture, null));

            } else {
                pic_b.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_done_green, null));
            }
        }


        //Get Locations from RequestHelpMapActivity
        if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
            Latitude = data.getDoubleExtra("Lat", 0);
            Longitude = data.getDoubleExtra("Lang", 0);
            if (Latitude > 0 || Longitude > 0)
                Toast.makeText(this, "Lat: " + String.valueOf(Latitude) + " , Lang: " + String.valueOf(Longitude), Toast.LENGTH_SHORT).show();
            loc_b.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_done_green, null));
        }

    }

    public void addFirebaseEntry() {
        final String Name = name.getText().toString().trim();
        String temp_phone = phone.getText().toString().trim();
        final String Phone = "+8801" + temp_phone;
        System.out.println(Phone.length());

        final String timeStamp = new SimpleDateFormat("EEE, d MMM yyyy, KK:mm").format(new Date());
        final String ID = TAG + "_" + new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        ;
        final int uID = getUserID();

        if (TextUtils.isEmpty(Name) || TextUtils.isEmpty(temp_phone) || mCurrentPhotoPath == null || (Latitude == 0 && Longitude == 0)) {
            if (TextUtils.isEmpty(Name) || Name.trim().equals("")) {
                Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(Phone) || Phone.length() < 14) {
                Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
            }
            if (mCurrentPhotoPath == null) {
                Toast.makeText(this, "Please Capture Photo of the Incident", Toast.LENGTH_SHORT).show();
            }
            if (Latitude == 0 && Longitude == 0) {
                Toast.makeText(this, "Please Give Location of the Incident", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(RequestHelpActivity.this, "Uploading.... Please Wait", Toast.LENGTH_LONG).show();
            req.setClickable(false);

            //Compress Image
            mCompressedPhotoPath = ImageCompression.compressImage(mCurrentPhotoPath, imageFileName);
            new File(mCurrentPhotoPath).delete();
            Uri mCompressedPhotoURI = Uri.fromFile(new File(mCompressedPhotoPath));

            //Upload Image File
            final StorageReference fileReference = storageReference.child(TAG + "/" + ID + ".jpg");
            UploadTask uploadTask = fileReference.putFile(mCompressedPhotoURI);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        if (downloadUri != null) {

                            String photoStringLink = downloadUri.toString(); //YOU WILL GET THE DOWNLOAD URL HERE !!!!
                            System.out.println("URL " + photoStringLink);
                            ImageURL = photoStringLink;

                            // Database Upload
                            if (ImageURL != null && ImageURL.contains("firebasestorage.googleapis.com")) {
                                //Upload Data to Database

                                //Preparing Data for Entry
                                RequestModel requestModel = new RequestModel(ID, Name, Phone, Latitude, Longitude, TAG, uID, "REQUESTED", timeStamp, ImageURL);

                                //Uploading Data
                                databaseReference.child(ID).setValue(requestModel);
                                Toast.makeText(RequestHelpActivity.this, "Request Submitted Successfully", Toast.LENGTH_SHORT).show();

                                req.setVisibility(View.GONE);
                            }
                        }

                    }
                }
            });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    req.setVisibility(View.GONE);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            req.setVisibility(View.VISIBLE);
                            req.setClickable(true);

                            Toast.makeText(RequestHelpActivity.this, "Upload Failed", Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            startActivity(new Intent(RequestHelpActivity.this, CheckProgress_ViewActivity.class));
                        }
                    });


        }


    }

    private int getUserID() {
        SharedPreferences sharedPref = this.getSharedPreferences("prefUID", Context.MODE_PRIVATE);

        return sharedPref.getInt("appId", 0);
    }

}