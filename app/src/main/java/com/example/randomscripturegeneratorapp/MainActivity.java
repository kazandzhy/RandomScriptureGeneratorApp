package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // we need to pass context to deserializeJSON in order to read file from the assets folder
        context = this.getApplicationContext();
        // deserialize JSON file to Java array
        WorkWithJSON.deserializeJSON(context);

        //Drop down menu for Pure and Weighted random
        Spinner spinner = (Spinner) findViewById(R.id.pure_weighted_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pure_weighted_random, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    public void sendVerseToDisplay(View view) {
        RandomizeVerse randomizeVerse = new RandomizeVerse();

        ScriptureData verse = randomizeVerse.randomizeFromAllWorks();

        Intent displayIntent = new Intent(this, ShowScriptureActivity.class);
        displayIntent.putExtra("verse_title", verse.getVerse_title());
        displayIntent.putExtra("scripture_text", verse.getScripture_text());
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

    public void goToSettingsActivity(View view) {
        Intent goToSettingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(goToSettingsIntent);
    }

}

