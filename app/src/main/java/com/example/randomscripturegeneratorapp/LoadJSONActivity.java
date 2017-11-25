package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class LoadJSONActivity extends AppCompatActivity {

    private static Context context;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_json);

        LoadJSONActivity.context = this;

        pb = (ProgressBar) findViewById(R.id.pbJSON);
        pb.setProgress(0);

        CountDownTimer countDownTimer = new CountDownTimer(11000, 1000) {
            int progressValue = 0;

            @Override
            public void onTick(long l) {
                progressValue += 10;
                pb.setProgress(progressValue);
                if (progressValue >= 100) {
                    progressValue = 0;
                }
            }

            @Override
            public void onFinish() {
            }
        };

        new loadJSON().execute("");
    }

    private class loadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            WorkWithJSON.deserializeJSON(context);

            return "";
        }

        protected void onPostExecute(String string) {
            Intent goToMainActivity = new Intent(LoadJSONActivity.context, MainActivity.class);
            startActivity(goToMainActivity);
        }

    }
}
