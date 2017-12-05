package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

/**
 * Activity to show user loading screen
 *
 * This activity deserializes the scripture JSON file in a background thread
 * and shows the user a pleasant loading screen while this occurs
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */
public class LoadJSONActivity extends AppCompatActivity {

    // declare variables
    private static Context context;
    private ProgressBar pb;
    CountDownTimer countDownTimer;

    /**
     * This function shows a spinning progress circle and a loading message for the user
     * on the screen
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_json);

        // save activity's context to pass it to deserializeJSON later
        LoadJSONActivity.context = this;

        // find ProgressBar pbJSON in the activity_load_json layout
        pb = (ProgressBar) findViewById(R.id.pbJSON);
        // set ProgressBar to 0%
        pb.setProgress(0);

        // create timer that keeps spinner continually moving on screen
        countDownTimer = new CountDownTimer(11000, 1000) {
            // start with 0 progress
            int progressValue = 0;

            /**
             * On every tick, progress on the ProgressBar increases by 10% until it gets
             * to 100% and starts over
             *
             * @param l
             */
            @Override
            public void onTick(long l) {
                progressValue += 10;
                pb.setProgress(progressValue);
                if (progressValue >= 100) {
                    progressValue = 0;
                }
            }

            /**
             * Function required for implementation of CountDownTimer class
             */
            @Override
            public void onFinish() {
            }
        };

        // call AsyncTask background task of loading JSON
        new loadJSON().execute("");
    }

    /**
     * loadJSON class uses AsyncTask to perform deserialization of the JSON file
     * in a background thread and proceed to MainActivity when finished
     */
    private class loadJSON extends AsyncTask<String, Integer, String> {

        @Override
        // deserialize JSON in background thread
        protected String doInBackground(String... params) {

            ScriptureData[] scriptureArray = new WorkWithJSON().getScriptureArray();

            // we want to do this only one time at the beginning of loading the app
            if (scriptureArray == null) {
                WorkWithJSON.deserializeJSON(context);
            }

            return "";
        }

        // proceed to MainActivity when finished
        protected void onPostExecute(String string) {
            Intent goToMainActivity = new Intent(LoadJSONActivity.context, MainActivity.class);
            startActivity(goToMainActivity);
        }

    }
}
