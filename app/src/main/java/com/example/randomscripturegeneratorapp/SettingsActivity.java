package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    private RadioButton radio_weighted;
    private RadioButton radio_pure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        radio_weighted = (RadioButton) findViewById(R.id.radio_weighted);
        radio_pure = (RadioButton) findViewById(R.id.radio_pure);

        String randomizeOption = MainActivity.sharedPrefs.getString("randomizeOption", "Weighted Random");
        Log.i("randomize option is ", randomizeOption);
        if (randomizeOption.equals("Weighted Random")) {
            radio_weighted.setChecked(true);
        } else {
            radio_pure.setChecked(true);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        if (MainActivity.userId == null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_settings_loggedout, menu);
        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_settings_loggedin, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.action_signup:
                startActivity(new Intent(this, SignupActivity.class));
                return true;
            case R.id.action_logout:
                UserSettings.logOut(getApplicationContext());
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onRadioButtonClicked(View view) {

        SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();

        if (radio_weighted.isChecked()) {
            editor.putString("randomizeOption", "Weighted Random");
        } else {
            editor.putString("randomizeOption", "Pure Random");
        }

        editor.apply();
    }

    public void onDailyScriptureClicked(View view)
    {
        startActivity(new Intent( this, Alarm_clock.class));
    }
}