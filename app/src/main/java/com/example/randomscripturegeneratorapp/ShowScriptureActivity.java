package com.example.randomscripturegeneratorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.randomscripturegeneratorapp.MainActivity.APP_PREFS;

public class ShowScriptureActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_scripture_activity);

        Intent intent = getIntent();
        String verse_title = intent.getStringExtra("verse_title");
        String scripture_text = intent.getStringExtra("scripture_text");
        displayScripture(scripture_text, verse_title);


    }

    public void randomizeAgain(View view) {

        SharedPreferences sharedPrefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        String activity = sharedPrefs.getString("activity", "No activity");
        Integer volume_id = sharedPrefs.getInt("volume_id", 0);

        RandomizeVerse randomizeVerse = new RandomizeVerse();
        ScriptureData verse;

        if (activity == "FilterWorkActivity") {
            verse = randomizeVerse.randomizeFromWork(volume_id);
        } else {
            verse = randomizeVerse.randomizeFromAllWorks();
        }

        String verse_title = verse.getVerse_title();
        String scripture_text = verse.getScripture_text();
        displayScripture(scripture_text, verse_title);
    }

    private void displayScripture(String scripture_text, String verse_title) {
        TextView scripture_verse_view = (TextView) findViewById(R.id.scripture_text_view);
        TextView scripture_title_view = (TextView) findViewById(R.id.scripture_title_view);
        scripture_verse_view.setMovementMethod(new ScrollingMovementMethod());
        scripture_verse_view.setText(scripture_text);
        scripture_title_view.setText(verse_title);
    }
}
