package com.drifters.help.activityClass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

import com.drifters.help.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout nav_Drawer;
    private ActionBarDrawerToggle nav_toggle;
    private String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Welcome");

        CardView fire = findViewById(R.id.fire_cv);
        CardView ambulance = findViewById(R.id.ambulance_cv);
        CardView police = findViewById(R.id.police_cv);
        CardView login = findViewById(R.id.login_cv);
        CardView about = findViewById(R.id.about_cv);
        CardView check = findViewById(R.id.check_cv);


        fire.setOnClickListener(this);
        ambulance.setOnClickListener(this);
        police.setOnClickListener(this);
        login.setOnClickListener(this);
        about.setOnClickListener(this);
        check.setOnClickListener(this);

        //For Navigation Drawer
        nav_Drawer = findViewById(R.id.nav_drawer);
        nav_toggle = new ActionBarDrawerToggle(this, nav_Drawer, R.string.nav_open, R.string.nav_close);
        nav_Drawer.addDrawerListener(nav_toggle);
        nav_toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NavigationView navigation_view = findViewById(R.id.nav_view);
        navigation_view.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return nav_toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.fire_cv:
                i = new Intent(this, RequestHelpActivity.class);
                TAG = "Fire";
                i.putExtra("Tag", TAG);
                startActivity(i);
                break;
            case R.id.ambulance_cv:
                i = new Intent(this, RequestHelpActivity.class);
                TAG = "Ambulance";
                i.putExtra("Tag", TAG);
                startActivity(i);
                break;
            case R.id.police_cv:
                i = new Intent(this, RequestHelpActivity.class);
                TAG = "Police";
                i.putExtra("Tag", TAG);
                startActivity(i);
                break;
            case R.id.login_cv:
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.about_cv:
                i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;

            case R.id.check_cv:
                i = new Intent(this, CheckProgress_ViewActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        nav_Drawer = findViewById(R.id.nav_drawer);
        if (nav_Drawer.isDrawerOpen(GravityCompat.START)) {
            nav_Drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent i;

        switch (item.getItemId()) {
            case R.id.fire_nav:
                i = new Intent(this, RequestHelpActivity.class);
                startActivity(i);
                break;
            case R.id.ambulance_nav:
                i = new Intent(this, RequestHelpActivity.class);
                startActivity(i);
                break;
            case R.id.police_nav:
                i = new Intent(this, RequestHelpActivity.class);
                startActivity(i);
                break;
            case R.id.login_nav:
                i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.about_nav:
                i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;

            case R.id.progress_nav:
                i = new Intent(this, CheckProgress_ViewActivity.class);
                startActivity(i);
                break;

            case R.id.exit_nav:
                finish();
                break;
            default:
                break;
        }

        nav_Drawer = findViewById(R.id.nav_drawer);
        nav_Drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
