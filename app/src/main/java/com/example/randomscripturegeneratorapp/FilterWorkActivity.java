package com.example.randomscripturegeneratorapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This Class randomizes a verse by filtering the standard works
 *
 * the user is able to check boxes of which standard work they
 * wish to receive a random verse from. Unchecked works will not be randomized
 *
 * @ Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */
public class FilterWorkActivity extends AppCompatActivity {

    private static List<Integer> userChoices;
    private Boolean ot_checked;
    private Boolean nt_checked;
    private Boolean bom_checked;
    private Boolean dc_checked;
    private Boolean pogp_checked;

    //load which boxes the user has checked
    public FilterWorkActivity() {
        userChoices = new ArrayList<>();
    }

    //retrieve the boxes the user has checked
    public static List<Integer> getUserChoices() {
        return userChoices;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_filter_activity);
        ot_checked = false;
        nt_checked = false;
        bom_checked = false;
        dc_checked = false;
        pogp_checked = false;
    }

    /**
     * determine if user is logged in or not
     *
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        if (MainActivity.userId == null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_other_loggedout, menu);
        } else {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_other_loggedin, menu);
        }
        return true;
    }

    /**
     * create options in menu toolbar
     * @param item
     * @return
     */
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
     * Randomize a verse from the selected standard works
     * and send new verse to ShowScriptureActivity
     *
     * @param view
     */
    @TargetApi(21)
    public void sendVerseToDisplay(View view) {
        userChoices.clear();
        int volume_id = -1;
        //determine which works the user has checked
        if (ot_checked) {
            userChoices.add(1);
        }
        if (nt_checked) {
            userChoices.add(2);
        }
        if (bom_checked) {
            userChoices.add(3);
        }
        if (dc_checked) {
            userChoices.add(4);
        }
        if (pogp_checked) {
            userChoices.add(5);
        }

        //if all check boxes aren't empty, randomize a verse
        if (!userChoices.isEmpty()) {
            int randomSpot = ThreadLocalRandom.current().nextInt(0, userChoices.size());
            volume_id = userChoices.get(randomSpot);

            //randomize vesre from the selected works
            RandomizeVerse randomizeVerse = new RandomizeVerse();
            ScriptureData verse = randomizeVerse.weightedRandomizeFromWork(volume_id);

            //create intent to showScriptureActivity
            Intent displayIntent = new Intent(this, ShowScriptureActivity.class);

            //set new verse to shared prefs
            SharedPrefs.saveVerseData(verse, "FilterWorkActivity");

            /*
            SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();

            editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
            editor.putString("verse_title", verse.getVerse_title());
            editor.putString("scripture_text", verse.getScripture_text());
            editor.putString("url", GenerateURL.createURL(verse));
            editor.putString("activity", "FilterWorkActivity");
            editor.apply();
            */

            startActivity(displayIntent);

        //if user hasn't selected any works display Toast
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select at least one option.", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /**
     * This is a listener for when a checkbox has been checked/unchecked
     * @param view
     */
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.OT_check:
                if (checked)
                    ot_checked = true;
                else
                    ot_checked = false;
                break;
            case R.id.NT_check:
                if (checked)
                    nt_checked = true;
                else
                    nt_checked = false;
                break;
            case R.id.BOM_check:
                if (checked)
                    bom_checked = true;
                else
                    bom_checked = false;
                break;
            case R.id.DC_check:
                if (checked)
                    dc_checked = true;
                else
                    dc_checked = false;
                break;
            case R.id.POGP_check:
                if (checked)
                    pogp_checked = true;
                else
                    pogp_checked = false;
                break;
        }
    }
}
