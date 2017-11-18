package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

//CHANGE: Hi Vlad!

// jesse

//you are awesome!

//vlad conflict:)

public class MainActivity extends AppCompatActivity {

    private Context context;

    public static final String APP_PREFS = "APPLICATION_PREFERENCES";

    public static String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // we need to pass context to deserializeJSON in order to read file from the assets folder
        context = this.getApplicationContext();
        // deserialize JSON file to Java array
        // the if statement ensures that the JSON is deserialized only the first time MainActivity is opened
        ScriptureData[] scriptureArray = new WorkWithJSON().getScriptureArray();
        if (scriptureArray == null) {
            WorkWithJSON.deserializeJSON(context);
        }

        //Drop down menu for Pure and Weighted random
        Spinner spinner = (Spinner) findViewById(R.id.pure_weighted_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pure_weighted_random, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        // this code recalls the user's randomizing option and sets the spinner accordingly
        SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.APP_PREFS, Context.MODE_PRIVATE);
        String randomizeOption = sharedPrefs.getString("randomizeOption", "No option");
        if (randomizeOption.equals("Weighted Random")) {
            spinner.setSelection(0);
        } else if (randomizeOption.equals("Pure Random")) {
            spinner.setSelection(1);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_loggedout, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.action_signup:
                startActivity(new Intent(this, SignupActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_logout:
                // code for logout goes here
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void sendVerseToDisplay(View view) {
        Spinner mySpinner=(Spinner) findViewById(R.id.pure_weighted_spinner);
        String randomizeOption = mySpinner.getSelectedItem().toString();

        RandomizeVerse randomizeVerse = new RandomizeVerse();

        ScriptureData verse;
        if (randomizeOption.equals("Weighted Random")) {
            verse = randomizeVerse.weightedRandomizeFromAllWorks();
        } else {
            verse = randomizeVerse.pureRandomizeFromAllWorks();
        }

        Intent displayIntent = new Intent(this, ShowScriptureActivity.class);
        displayIntent.putExtra("verse_title", verse.getVerse_title());
        displayIntent.putExtra("scripture_text", verse.getScripture_text());
        displayIntent.putExtra("randomizeOption", randomizeOption);

        SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("activity", "MainActivity");
        editor.putString("randomizeOption", randomizeOption);
        editor.apply();

        startActivity(displayIntent);
    }

    public void goToFilterWorkActivity(View view) {
        Intent goToFilterWorkIntent = new Intent(this, FilterWorkActivity.class);
        startActivity(goToFilterWorkIntent);
    }

    public void goToFilterBookActivity(View view) {
        Intent goToFilterBookIntent = new Intent(this, FilterBookActivity.class);
        startActivity(goToFilterBookIntent);
    }

    public void goToFavoritesActivity(View view) {
        Intent goToFavoritesIntent = new Intent(this, FavoritesActivity.class);
        startActivity(goToFavoritesIntent);
    }

}

