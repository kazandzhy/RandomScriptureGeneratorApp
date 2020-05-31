package com.kazandzhy.randomscripture;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Show Scripture Activity
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */
public class ShowScriptureActivity extends AppCompatActivity {

    private String verse_id;
    private String randomizeOption;
    private String bookChoice;
    private String verse_url;
    private String activity;
    public static final String APP_PREFS = "APPLICATION_PREFERENCES";
    private SharedPreferences sharedPrefs;
    public static String userId;

    /**
     * Grabs the Verse from the shared preferences
     * and sends it to the display.
     *
     * @param savedInstanceState saved current state for unpredictable circumstances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_scripture_activity);

        // Get the Scripture from the shared preferences
        sharedPrefs = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        verse_id = sharedPrefs.getString("verse_id", "No verse_id");
        String verse_title = sharedPrefs.getString("verse_title", "No verse");
        String scripture_text = sharedPrefs.getString("scripture_text", "No scripture");
        verse_url = sharedPrefs.getString("url", "No url");
        activity = sharedPrefs.getString("activity", "No activity");
        randomizeOption = sharedPrefs.getString("randomizeOption", "Weighted Random");
        bookChoice = sharedPrefs.getString("book_title", "No book");
        userId = sharedPrefs.getString("userId", null);

        // Send the verse to the textViews
        displayScripture(scripture_text, verse_title);
    }

    /**
     * Creates the Options Menu, based on weather or not the user is logged in
     *
     * @param menu menu that is created on the toolbar
     * @return true to create menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        if (activity.equals("AlarmReceiver")) {
            // show special menu that only has home option
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_home_only, menu);
        } else {
            // Check to see if the user is logged in or not
            if (userId == null) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_other_loggedout, menu);
            } else {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_other_loggedin, menu);
            }
        }
        return true;
    }

    /**
     * Starts the proper activity based on the user's selection
     * from the options menu
     *
     * @param item selected menu item
     * @return true to handle action
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Determine what activity the user wants to navigate to
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
     * Randomizes a new scripture, saves it in shared preferences, and displays it
     *
     * @param view base class for widgets
     */
    @TargetApi(21)
    public void randomizeAgain(View view) {
        // Get a new verse
        RandomizeVerse randomizeVerse = new RandomizeVerse();
        ScriptureData verse;

        // Determine the parameters from witch we need to randomize the scripture
        if (activity.equals("FilterWorkActivity")) {
            List<Integer> userChoices = FilterWorkActivity.getUserChoices();
            int randomSpot = ThreadLocalRandom.current().nextInt(0, userChoices.size());
            int volume_id = userChoices.get(randomSpot);

            verse = randomizeVerse.weightedRandomizeFromWork(volume_id);
        } else if (activity.equals("FilterBookActivity")) {

            verse = randomizeVerse.randomizeFromBook(bookChoice);
        } else if (activity.equals("FavoritesActivity")) {
            verse = RandomizeVerse.randomizeFromFavoriteArray(FavoritesActivity.favoritesArray);
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
        verse_url = GenerateURL.createURL(verse);
        verse_id = Integer.toString(verse.getVerse_id());

        // Save the scripture in shared preferences
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("verse_id", verse_id);
        editor.putString("verse_title", verse_title);
        editor.putString("scripture_text", scripture_text);
        editor.putString("url", verse_url);
        editor.apply();

        displayScripture(scripture_text, verse_title);

    }

    /**
     * Sends a request via volley to add a scripture to the favorites.
     *
     * @param view base class for widgets
     */
    public void addToFavorites(View view) {
        // Create the listener for the response
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    Log.i("jsonResponse is ", message);

                    // Show the user the message from the database
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // Determine if we should to send the request based on weather or not the user is logged in or not.
        if (userId == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please log in to save Favorites.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            AddToFavoritesRequest addToFavoritesRequest = new AddToFavoritesRequest(userId, verse_id, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ShowScriptureActivity.this);
            queue.add(addToFavoritesRequest);
        }
    }

    /**
     * Populates the textView's with the passed information
     *
     * @param scripture_text the full text of the verse
     * @param verse_title the title of the verse
     */
    private void displayScripture(String scripture_text, String verse_title) {
        // Identify the text views we need to populate
        TextView scripture_verse_view = findViewById(R.id.scripture_text_view);
        TextView scripture_title_view = findViewById(R.id.scripture_title_view);

        // Get the size of the text from the Shared Preferences
        scripture_verse_view.setTextSize(sharedPrefs.getInt("text_size", 15));

        // Set the textView's
        scripture_verse_view.setMovementMethod(new ScrollingMovementMethod());
        scripture_verse_view.setText(scripture_text);
        scripture_title_view.setText(verse_title);
        scripture_verse_view.scrollTo(0,0);
    }

    /**
     * Opens to the scripture in lds.org to continue reading
     *
     * @param view base class for widgets
     */
    public void openBrowser(View view) {
        // Start Chrome
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(verse_url));// Constructs the URL based on the Scripture
        startActivity(browserIntent);
    }
}
