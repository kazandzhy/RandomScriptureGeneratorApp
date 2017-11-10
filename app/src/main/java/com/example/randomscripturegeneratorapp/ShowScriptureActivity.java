package com.example.randomscripturegeneratorapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.randomscripturegeneratorapp.MainActivity.APP_PREFS;

public class ShowScriptureActivity extends AppCompatActivity {

    private String randomizeOption;
    private String bookChoice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_scripture_activity);

        Intent intent = getIntent();
        String verse_title = intent.getStringExtra("verse_title");
        String scripture_text = intent.getStringExtra("scripture_text");
        randomizeOption = intent.getStringExtra("randomizeOption");
        bookChoice = intent.getStringExtra("book_title");

        displayScripture(scripture_text, verse_title);

    }

    @TargetApi(21)
    public void randomizeAgain(View view) {

        SharedPreferences sharedPrefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        String activity = sharedPrefs.getString("activity", "No activity");

        RandomizeVerse randomizeVerse = new RandomizeVerse();
        ScriptureData verse;

        if (activity.equals("FilterWorkActivity")) {
            List<Integer> userChoices = FilterWorkActivity.getUserChoices();
            int randomSpot = ThreadLocalRandom.current().nextInt(0, userChoices.size());
            int volume_id = userChoices.get(randomSpot);

            verse = randomizeVerse.weightedRandomizeFromWork(volume_id);
        } else if (activity.equals("FilterBookActivity")) {
            verse = randomizeVerse.randomizeFromBook(bookChoice);
        } else {
            if (randomizeOption.equals("Weighted Random")) {
                verse = randomizeVerse.weightedRandomizeFromAllWorks();
                Log.i("if option is ", randomizeOption);
            } else {
                verse = randomizeVerse.pureRandomizeFromAllWorks();
                Log.i("else option is ", randomizeOption);
            }
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

    public void goHome(View view) {
        Intent goHome = new Intent(this, MainActivity.class);
        startActivity(goHome);
    }
}
