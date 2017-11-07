package com.example.randomscripturegeneratorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public void goToLoginActivity(View view) {
        Intent goToLoginIntent = new Intent(this, LoginActivity.class);
        startActivity(goToLoginIntent);
    }

    public void goToSignupActivity(View view) {
        Intent goToSignupIntent = new Intent(this, SignupActivity.class);
        startActivity(goToSignupIntent);
    }
}
