package com.example.randomscripturegeneratorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

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

        // Change the text size settings
        listenForTextSize();

    }

    // Changes the text size of the scripture
    private void listenForTextSize() {

        // Find the Views we want to mess with
        final TextView abc = (TextView) findViewById(R.id.ABC);
        final SeekBar seekBar = (SeekBar) findViewById(R.id.textSize);

        // Set those views to whatever the previously specified values were
        seekBar.setProgress(MainActivity.sharedPrefs.getInt("text_size", 30) * 2);
        abc.setTextSize(MainActivity.sharedPrefs.getInt("text_size", 15));

        // Listen for changes with the SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Change the shared Preferences
                SharedPreferences sharedPreferences = MainActivity.sharedPrefs;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("text_size", progress / 2);
                editor.apply();

                // Show the user what the text size looks like
                abc.setTextSize(sharedPreferences.getInt("text_size", 15));
                editor.apply();
            }

            // these are useless for the most part but must be included.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void  onStopTrackingTouch(SeekBar seekBar) {}
        });
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
        startActivity(new Intent( this, AlarmClockActivity.class));
    }
}