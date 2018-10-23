package com.drifters.help.activityClass;

import android.content.Intent;
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

        button = (Button) findViewById(R.id.login_btn);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        i = new Intent(this, SendHelp_ViewActivity.class);
        startActivity(i);
        finish();
    }
}
