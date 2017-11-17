package com.example.randomscripturegeneratorapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class FilterBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Context context;
    RandomizeVerse randomizeVerse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_filter_activity);

        randomizeVerse = new RandomizeVerse();

        //Drop down menu for Works
        Spinner works = (Spinner) findViewById(R.id.dropDown_works);
        works.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> workAdapter = ArrayAdapter.createFromResource(this,
                R.array.works, android.R.layout.simple_spinner_item);
        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        works.setAdapter(workAdapter);

        works.getOnItemSelectedListener();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void sendVerseToDisplay(View view) {
        Spinner bookSpinner =(Spinner) findViewById(R.id.dropDown_books);
        String bookChoice = bookSpinner.getSelectedItem().toString();

        ScriptureData verse = randomizeVerse.randomizeFromBook(bookChoice);

        Intent displayIntent = new Intent(this, ShowScriptureActivity.class);

        displayIntent.putExtra("verse_title", verse.getVerse_title());
        displayIntent.putExtra("scripture_text", verse.getScripture_text());
        displayIntent.putExtra("book_title", verse.getBook_title());

        SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("activity", "FilterBookActivity");
        editor.apply();

        startActivity(displayIntent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            String work = parent.getItemAtPosition(i).toString();
            Spinner spinner = (Spinner) parent;
            //Differentiate between which spinner was changed
            if(spinner.getId() == R.id.dropDown_works)
            {
                updateBookSpinner(parent.getItemAtPosition(i).toString());
            }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void updateBookSpinner(String position)
    {
        Spinner books = (Spinner) findViewById(R.id.dropDown_books);
        books.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> bookAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        switch(position)
        {
            case "Old Testament":
                bookAdapter = ArrayAdapter.createFromResource(this,R.array.ot, android.R.layout.simple_spinner_dropdown_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
            case "New Testament":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.nt, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
            case "Book of Mormon":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.bom, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
            case "Doctrine and Covenants":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.doc, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
            case "Pearl of Great Price":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.pogp, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setAdapter(bookAdapter);
                break;
        }
        books.getOnItemSelectedListener();

    }
    public void goHome(View view) {
        Intent goHome = new Intent(this, MainActivity.class);
        startActivity(goHome);
    }
}

