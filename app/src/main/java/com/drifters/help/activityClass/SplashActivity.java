package com.drifters.help.activityClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.drifters.help.R;

public class SplashActivity extends Activity {

    private TextView splash_tv1, splash_tv2, splash_tv3;
    private ImageView splash_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /* ---------------No Status Bar--------------------- */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /* ------------------------------------------------------ */

        splash_tv1 = (TextView) findViewById(R.id.tv1);
        splash_tv2 = (TextView) findViewById(R.id.tv2);
        splash_tv3 = (TextView) findViewById(R.id.tv3);

        splash_iv = (ImageView) findViewById(R.id.iv);

        final Intent i = new Intent(this, MainActivity.class);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}