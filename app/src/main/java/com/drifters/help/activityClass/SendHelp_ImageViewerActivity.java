package com.drifters.help.activityClass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.drifters.help.ConnectionCheck;
import com.drifters.help.R;
import com.squareup.picasso.Picasso;

public class SendHelp_ImageViewerActivity extends AppCompatActivity {

    private String url;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        url = getIntent().getStringExtra("url");
        imageView = findViewById(R.id.iv_imageViewer);
        if (ConnectionCheck.isOnline(this)) {
            Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.loading_indicator)
                    .fit()
                    .into(imageView);
        } else {
            Toast.makeText(this, "Please Connect to Internet and Try again!!", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
