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
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {

    private Context context;

    public static final String APP_PREFS = "APPLICATION_PREFERENCES";
    public static SharedPreferences sharedPrefs;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        sharedPrefs = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        userId = sharedPrefs.getString("userId", null);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (userId == null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_home_loggedout, menu);
        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_home_loggedin, menu);
        }
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
                UserSettings.logOut(getApplicationContext());
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void sendVerseToDisplay(View view) {

        String randomizeOption = sharedPrefs.getString("randomizeOption", "Weighted Random");
        RandomizeVerse randomizeVerse = new RandomizeVerse();

        ScriptureData verse;

        if (randomizeOption.equals("Weighted Random")) {
            verse = randomizeVerse.weightedRandomizeFromAllWorks();
        } else {
            verse = randomizeVerse.pureRandomizeFromAllWorks();
        }

        Intent displayIntent = new Intent(this, ShowScriptureActivity.class);

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
        editor.putString("verse_title", verse.getVerse_title());
        editor.putString("scripture_text", verse.getScripture_text());
        editor.putString("url", URL.createURL(verse));
        editor.putString("activity", "MainActivity");
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
        if (userId == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please log in to access this feature.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent goToFavoritesIntent = new Intent(this, FavoritesActivity.class);
            startActivity(goToFavoritesIntent);
        }
    }

}

