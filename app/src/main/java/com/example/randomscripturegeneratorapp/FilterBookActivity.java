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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * This class allows the user to get a random verse from a specific book of scripture
 *
 * the user can pick a specific work, and a book within that work and receive a random verse from it
 *
 * @author Vlad Kazandzhy, Tyler Braithwaite, Nathan Tagg
 */
public class FilterBookActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Context context;
    RandomizeVerse randomizeVerse;
    ArrayAdapter<CharSequence> workAdapter;
    ArrayAdapter<CharSequence> bookAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_filter_activity);

        randomizeVerse = new RandomizeVerse();

        //Drop down menu for Works
        Spinner works = (Spinner) findViewById(R.id.dropDown_works);
        works.setOnItemSelectedListener(this);


        workAdapter = ArrayAdapter.createFromResource(this,
                R.array.works, android.R.layout.simple_spinner_item);
        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        works.setAdapter(workAdapter);

        works.getOnItemSelectedListener();

    }


    /**
     * Determine if user is logged in or not, and populate the options menu accordingly.
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
     * Create options in menu toolbar
     *
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
     * Send Randomized verse to ShowScriptureActivity
     *
     * @param view
     */
    public void sendVerseToDisplay(View view) {
        //determine what book the user has picked
        Spinner bookSpinner =(Spinner) findViewById(R.id.dropDown_books);
        String bookChoice = bookSpinner.getSelectedItem().toString();

        //get random verse from specified book
        ScriptureData verse = randomizeVerse.randomizeFromBook(bookChoice);

        //create intent to ShowScriptureActivity
        Intent displayIntent = new Intent(this, ShowScriptureActivity.class);

        //Save verse in shared preferences
        SharedPrefs.saveVerseData(verse, "FilterBookActivity");

        //start intent
        startActivity(displayIntent);
    }


    /**
     * If a dropdown menu has changed, update menus accordingly
     *
     * @param parent
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            String work = parent.getItemAtPosition(i).toString();
            Spinner spinner = (Spinner) parent;
            //Differentiate between which spinner was changed
            if(spinner.getId() == R.id.dropDown_works)
            {
                updateBookSpinner(work);
            }
    }

    /**
     * If nothing is selected, drop down menus are empty
     *
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * When a standard work is selected update the BookSpinner
     * BookSpinner will display books within the newly selected
     * standard work
     *
     * The standard work that is currently selected
     * @param position
     */
    public void updateBookSpinner(String position)
    {
        Spinner books = (Spinner) findViewById(R.id.dropDown_books);
        books.setOnItemSelectedListener(this);

        //BookSpinner will update it's array adapter based on which standard work is selected
        switch(position)
        {
            case "Old Testament":
                bookAdapter = ArrayAdapter.createFromResource(this,R.array.ot, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setEnabled(true);
                books.setAdapter(bookAdapter);
                break;
            case "New Testament":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.nt, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setEnabled(true);
                books.setAdapter(bookAdapter);
                break;
            case "Book of Mormon":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.bom, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setEnabled(true);
                books.setAdapter(bookAdapter);
                break;
            case "Doctrine and Covenants":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.doc, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setEnabled(false);
                books.setAdapter(bookAdapter);
                break;
            case "Pearl of Great Price":
                bookAdapter = ArrayAdapter.createFromResource(this,
                        R.array.pogp, android.R.layout.simple_spinner_item);
                bookAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                books.setEnabled(true);
                books.setAdapter(bookAdapter);
                break;
        }
        books.getOnItemSelectedListener();

    }
}

