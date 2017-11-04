package com.example.randomscripturegeneratorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class FilterWorkActivity extends AppCompatActivity {

    Boolean ot_checked;
    Boolean nt_checked;
    Boolean bom_checked;
    Boolean dc_checked;
    Boolean pogp_checked;

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

    public void sendVerseToDisplay(View view) {
        int volume_id = -1;
        if (ot_checked) {
            volume_id = 1;
        }
        if (nt_checked) {
            volume_id = 2;
        }
        if (bom_checked) {
            volume_id = 3;
        }
        if (dc_checked) {
            volume_id = 4;
        }
        if (pogp_checked) {
            volume_id = 5;
        }
        Log.i("volume_id is: ", Integer.toString(volume_id));

        if (volume_id > 0) {
            RandomizeVerse randomizeVerse = new RandomizeVerse();
            ScriptureData verse = randomizeVerse.randomizeFromWork(volume_id);

            Intent displayIntent = new Intent(this, ShowScriptureActivity.class);
            displayIntent.putExtra("verse_title", verse.getVerse_title());
            displayIntent.putExtra("scripture_text", verse.getScripture_text());
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
