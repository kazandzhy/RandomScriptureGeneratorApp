package com.example.randomscripturegeneratorapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FilterWorkActivity extends AppCompatActivity {

    private static List<Integer> userChoices;
    private Boolean ot_checked;
    private Boolean nt_checked;
    private Boolean bom_checked;
    private Boolean dc_checked;
    private Boolean pogp_checked;

    public FilterWorkActivity() {
        userChoices = new ArrayList<>();
    }

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

    @TargetApi(21)
    public void sendVerseToDisplay(View view) {
        userChoices.clear();
        int volume_id = -1;
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

        if (!userChoices.isEmpty()) {
            int randomSpot = ThreadLocalRandom.current().nextInt(0, userChoices.size());
            volume_id = userChoices.get(randomSpot);

            RandomizeVerse randomizeVerse = new RandomizeVerse();
            ScriptureData verse = randomizeVerse.randomizeFromWork(volume_id);

            Intent displayIntent = new Intent(this, ShowScriptureActivity.class);
            displayIntent.putExtra("verse_title", verse.getVerse_title());
            displayIntent.putExtra("scripture_text", verse.getScripture_text());

            SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.APP_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putString("activity", "FilterWorkActivity");
            editor.apply();

            startActivity(displayIntent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select at least one option.", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

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
