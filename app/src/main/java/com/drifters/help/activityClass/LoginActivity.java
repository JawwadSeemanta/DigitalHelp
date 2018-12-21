package com.drifters.help.activityClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.drifters.help.ConnectionCheck;
import com.drifters.help.R;

public class LoginActivity extends AppCompatActivity {

    private Button button;
    private EditText username, password;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        //Get the login status variable
        SharedPreferences s = getSharedPreferences("Login", Context.MODE_PRIVATE);
        Boolean logged_in = s.getBoolean("login_status", false);

        username = findViewById(R.id.n_login);
        password = findViewById(R.id.p_login);
        spinner = findViewById(R.id.spinner);

        button = findViewById(R.id.login_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionCheck.isOnline(LoginActivity.this)) {
                    final String user = username.getText().toString().trim();
                    final String pass = password.getText().toString().trim();
                    if (!TextUtils.isEmpty(user) || !TextUtils.isEmpty(pass)) {
                        //TODO: In Future use auth services instead of hardcoded string
                        // Hardcoded username and passeord
                        if (user.matches("user") && pass.matches("pass")) {

                            setHelperType(spinner.getSelectedItem().toString().trim());

                            startActivity(new Intent(LoginActivity.this, SendHelp_ViewActivity.class));
                            loggedIn();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect Login Credentials!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Username or Password can not be empty!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Please Enable Internet and Try Again!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (logged_in) {
            startActivity(new Intent(LoginActivity.this, SendHelp_ViewActivity.class));
            finish();
        }

    }

    private void loggedIn() {
        SharedPreferences sp = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("login_status", true);
        editor.commit();
    }

    private void setHelperType(String type) {
        SharedPreferences sPref = this.getSharedPreferences("prefType", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("rType", type);
        editor.commit();
    }
}
