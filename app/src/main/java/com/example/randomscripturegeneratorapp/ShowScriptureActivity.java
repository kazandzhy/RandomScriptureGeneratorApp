package com.example.randomscripturegeneratorapp;

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


public class ShowScriptureActivity extends AppCompatActivity {

    private String verse_id;
    private String randomizeOption;
    private String bookChoice;
    private String verse_url;
    private String activity;
    public static final String APP_PREFS = "APPLICATION_PREFERENCES";
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_scripture_activity);

        sharedPrefs = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        verse_id = sharedPrefs.getString("verse_id", "No verse_id");
        String verse_title = sharedPrefs.getString("verse_title", "No verse");
        String scripture_text = sharedPrefs.getString("scripture_text", "No scripture");
        verse_url = sharedPrefs.getString("url", "No url");
        activity = sharedPrefs.getString("activity", "No activity");
        randomizeOption = sharedPrefs.getString("randomizeOption", "Weighted Random");
        bookChoice = sharedPrefs.getString("book_title", "No book");

        displayScripture(scripture_text, verse_title);

    }

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

    @TargetApi(21)
    public void randomizeAgain(View view) {

        RandomizeVerse randomizeVerse = new RandomizeVerse();
        ScriptureData verse;

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

        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("verse_id", verse_id);
        editor.putString("verse_title", verse_title);
        editor.putString("scripture_text", scripture_text);
        editor.putString("url", verse_url);
        editor.apply();

        displayScripture(scripture_text, verse_title);

    }

    public void addToFavorites(View view) {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    Log.i("jsonResponse is ", message);

                    // user will see different type of message depending on if the verse was successfully added or not
                    Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                    toast.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if (MainActivity.userId == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please log in to save Favorites.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            AddToFavoritesRequest addToFavoritesRequest = new AddToFavoritesRequest(MainActivity.userId, verse_id, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ShowScriptureActivity.this);
            queue.add(addToFavoritesRequest);
        }
    }

    private void displayScripture(String scripture_text, String verse_title) {
        TextView scripture_verse_view = (TextView) findViewById(R.id.scripture_text_view);
        TextView scripture_title_view = (TextView) findViewById(R.id.scripture_title_view);

        // Get the size of the text from the Shared Preferences
        scripture_verse_view.setTextSize(sharedPrefs.getInt("text_size", 15));

        scripture_verse_view.setMovementMethod(new ScrollingMovementMethod());
        scripture_verse_view.setText(scripture_text);
        scripture_title_view.setText(verse_title);
        scripture_verse_view.scrollTo(0,0);
    }

    public void openBrowser(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(verse_url));
        startActivity(browserIntent);
    }
}
