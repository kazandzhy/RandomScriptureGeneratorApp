package com.example.randomscripturegeneratorapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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


    // For holding the variables for the scripture in scope for other Activities and methods.
    public String workId = "pgp";
    public String bookId = "moses";
    public String chapId = "1";
    public String verseId= "39";


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


        // This code is necessary to make it so that the READ MORE functionality works
        SharedPreferences sharedPrefs = getSharedPreferences(APP_PREFS, MODE_PRIVATE);
        String activity = sharedPrefs.getString("activity", "No activity");
        RandomizeVerse randomizeVerse = new RandomizeVerse();
        ScriptureData verse;
        if (activity.equals("FilterWorkActivity")) {
            List<Integer> userChoices = FilterWorkActivity.getUserChoices();
            int randomSpot = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                randomSpot = ThreadLocalRandom.current().nextInt(0, userChoices.size());
            }
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

        verse_title = verse.getVerse_title();
        scripture_text = verse.getScripture_text();
        displayScripture(scripture_text, verse_title);

        workId = String.valueOf(verse.volume_lds_url);
        bookId = String.valueOf(verse.book_lds_url);
        chapId = String.valueOf(verse.chapter_number);
        verseId = String.valueOf(verse.verse_number);
        // PLEASE don't touch this code ^^

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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

        workId = String.valueOf(verse.volume_lds_url);
        bookId = String.valueOf(verse.book_lds_url);
        chapId = String.valueOf(verse.chapter_number);
        verseId = String.valueOf(verse.verse_number);

        String verse_title = verse.getVerse_title();
        String scripture_text = verse.getScripture_text();
        displayScripture(scripture_text, verse_title);
    }

    public void addToFavorites(View view) {
        Log.v("added to favorites: ", "scripture");
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

    public void openBrowser(View view) {
        String  work = workId;
        String  book = bookId;
        String  chap = chapId;
        String verse = verseId;
        TextView debug = (TextView) findViewById(R.id.debug);
        debug.setText("https://www.lds.org/scriptures/" + work + "/" + book + "/" + chap + "." + verse + "?lang=eng#38");
        String url = "https://www.lds.org/scriptures/" + work + "/" + book + "/" + chap + "." + verse + "?lang=eng#p"+ verse;
        Log.d("url", url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
