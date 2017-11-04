package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//CHANGE: Hi Vlad!

// jesse

//you are awesome!

//vlad conflict:)

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // we need to pass context to deserializeJSON in order to read file from the assets folder
        context = this.getApplicationContext();
        // deserialize JSON file to Java array
        WorkWithJSON.deserializeJSON(context);
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
        Intent goToFilterBookIntent = new Intent(this, BookFilter.class);
        startActivity(goToFilterBookIntent);
    }

    public void goToFavoritesActivity(View view) {
        Intent goToFavoritesIntent = new Intent(this, activity_favorites.class);
        startActivity(goToFavoritesIntent);
    }

    public void goToSettingsActivity(View view) {
        Intent goToSettingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(goToSettingsIntent);
    }

}

