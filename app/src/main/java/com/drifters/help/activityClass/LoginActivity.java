package com.drifters.help.activityClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.drifters.help.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        SharedPreferences s = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Boolean logged_in = s.getBoolean("login_status", false);

        button = findViewById(R.id.login_btn);
        button.setOnClickListener(this);

        if (logged_in) {
            startActivity(new Intent(LoginActivity.this, SendHelp_ViewActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        Intent i;
        i = new Intent(this, SendHelp_ViewActivity.class);
        startActivity(i);
        loggedIn();
        finish();
    }

    private void loggedIn() {
        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("login_status", true);
        editor.commit();
    }
}
