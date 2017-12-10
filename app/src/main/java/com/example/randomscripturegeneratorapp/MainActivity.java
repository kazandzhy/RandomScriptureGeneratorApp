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
import android.widget.Toast;

/**
 * Activity to let user choose different options
 *
 * This activity gives the user the following options: Random Scripture (from all works) or go to
 * Filter By Work, Filter By Book, or Favorites activities.
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */
public class MainActivity extends AppCompatActivity {

    // declare variables
    public static final String APP_PREFS = "APPLICATION_PREFERENCES";
    public static SharedPreferences sharedPrefs;
    public static String userId;

    /**
     * This function shows the layout on the screen and retrieves SharedPreferences
     * to know if a user is logged in and retrieves userId to identify the user.
     *
     * @param savedInstanceState saved current state for unpredictable circumstances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        sharedPrefs = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        userId = sharedPrefs.getString("userId", null);
    }

    /**
     * This function creates a toolbar menu depending on user's status (logged in or logged out)
     *
     * @param menu menu that is created on the toolbar
     * @return true to create menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // if user is logged out
        if (userId == null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_home_loggedout, menu);
        } else { // if user is logged in
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_home_loggedin, menu);
        }
        return true;
    }

    /**
     * This function handles all possible menu options
     *
     * @param item selected menu item
     * @return true to handle action
     */
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
                LogoutOption.logOut(getApplicationContext());
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * This function directs user from MainActivity to ShowScriptureActivity. Depending on
     * pure or weighted randomizing option, it shows a random scripture from all standard works.
     *
     * @param view base class for widgets
     */
    public void sendVerseToDisplay(View view) {

        // save current randomizing option
        String randomizeOption = sharedPrefs.getString("randomizeOption", "Weighted Random");
        RandomizeVerse randomizeVerse = new RandomizeVerse();
        ScriptureData verse;

        // generate random verse depending on randomizing option
        if (randomizeOption.equals("Weighted Random")) {
            verse = randomizeVerse.weightedRandomizeFromAllWorks();
        } else {
            verse = randomizeVerse.pureRandomizeFromAllWorks();
        }

        SharedPrefs.saveVerseData(verse, "MainActivity");

        // direct user to ShowScriptureActivity
        Intent displayIntent = new Intent(this, ShowScriptureActivity.class);
        startActivity(displayIntent);
    }

    /**
     * Direct user from MainActivity to FilterWorkActivity
     *
     * @param view base class for widgets
     */
    public void goToFilterWorkActivity(View view) {
        Intent goToFilterWorkIntent = new Intent(this, FilterWorkActivity.class);
        startActivity(goToFilterWorkIntent);
    }

    /**
     * Direct user from MainActivity to FilterBookActivity
     *
     * @param view base class for widgets
     */
    public void goToFilterBookActivity(View view) {
        Intent goToFilterBookIntent = new Intent(this, FilterBookActivity.class);
        startActivity(goToFilterBookIntent);
    }

    /**
     * If user logged in, direct user from MainActivity to FavoritesActivity
     * If user logged out, display toast inviting user to log in to use feature
     *
     * @param view base class for widgets
     */
    public void goToFavoritesActivity(View view) {
        // If user logged out, display toast inviting user to log in to use feature
        if (userId == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please log in to access this feature.", Toast.LENGTH_SHORT);
            toast.show();
        } else { // If user logged in, direct user from MainActivity to FavoritesActivity
            Intent goToFavoritesIntent = new Intent(this, FavoritesActivity.class);
            startActivity(goToFavoritesIntent);
        }
    }


}

