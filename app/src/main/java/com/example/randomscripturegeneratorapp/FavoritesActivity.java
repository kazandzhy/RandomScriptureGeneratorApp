package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Favorites activity class
 *
 * This Class holds all the functionality associated with
 * the favorites activity. It populates the favorites list,
 * sends verses to the display, and
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class FavoritesActivity extends AppCompatActivity {

    public static ScriptureData[] favoritesArray; // This array will be in use throughout the activity
    private static Context context;
    private Boolean noFavorites = false;
    private ListView favorites;

    /**
     * Calls the Populate Favorites List function
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity);
        context = this;

        //Populate the listView
        populateFavoritesList();
    }

    /**
     * This function is called from the onclick of each individual items
     * in the list. It searches the favorites array for the scripture selected
     * and sends all the data to the ShowScriptureActivity
     *
     * @param view
     */
    public void displayFavorite(View view) {

        // Get the verse title
        TextView t = (TextView)view;
        String verseTitle = t.getText().toString();

        // Log the scripture selected
        Log.i("You selected", verseTitle);

        // Find the verse from the favoritesArray
        int position = 0;
        for (int i = 0; i < favoritesArray.length; i++) {
            if (favoritesArray[i].getVerse_title() == verseTitle){
                position = i;
                break;
            }
        }

        // Highlight the verse we will be sending.
        ScriptureData verse = favoritesArray[position];

        // Display the Scripture
        Intent displayIntent = new Intent(this, ShowScriptureActivity.class);

        // this calls the SharedPrefs witch sets all the preferences for the show scripture activity
        SharedPrefs.saveVerseData(verse, "FavoritesActivity");
        startActivity(displayIntent);
    }

    /**
     * This function creates a toolbar for the favorites activity
     *
     * @param menu
     * @return true to create menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorites, menu);
        return true;
    }

    /**
     * This function handles all possible menu options
     *
     * @param item
     * @return true to handle action
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_randomize:
                randomizeFromFavorites();
                return true;
            case R.id.action_delete:
                //deleteAllFavorites();
                confirmDeleteFavorites();
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
     * This function retrieves all of the users' saved favorites and puts them in the list view.
     *
     */
    private void populateFavoritesList() {
        // Get the USER_ID
        SharedPreferences prefs = getSharedPreferences(MainActivity.APP_PREFS, MODE_PRIVATE);
        String userId = (prefs.getString("userId", ""));

        // Get Items from Database
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("No favorites")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "You have no Favorites saved.", Toast.LENGTH_LONG);
                    toast.show();
                    noFavorites = true;
                } else {
                    //create Gson instance
                    Gson gson = new Gson();
                    //fill array of objects
                    favoritesArray = gson.fromJson(response, ScriptureData[].class);

                    // Make the list of Items
                    ArrayList<String> verseTitles = new ArrayList<>();
                    for (int i = 0; i < favoritesArray.length; i++) {
                        Log.i("favorite is ", favoritesArray[i].getVerse_title());
                        verseTitles.add(favoritesArray[i].getVerse_title());
                    }

                    // Build the adapter
                    FavoritesListArrayAdapter adapter = new FavoritesListArrayAdapter(verseTitles, FavoritesActivity.this);

                    // Configure the list view
                    favorites = (ListView) findViewById(R.id.favorites);
                    favorites.setAdapter(adapter);

                }
            }
        };

        LoadFavoritesRequest loadFavoritesRequest = new LoadFavoritesRequest(userId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(FavoritesActivity.this);
        queue.add(loadFavoritesRequest);
    }

    /**
     * This function is called when the user selects 'randomize from all favorites'
     * it pulls a random verse from the array and sends it to the display.
     *
     */
    private void randomizeFromFavorites() {
        // Make sure that the array isn't empty
        if (favoritesArray != null) {
            ScriptureData verse = RandomizeVerse.randomizeFromFavoriteArray(favoritesArray);

            // Display the Scripture
            Intent displayIntent = new Intent(this, ShowScriptureActivity.class);

            // This calls the SharedPrefs witch sets all the preferences for the show scripture activity
            SharedPrefs.saveVerseData(verse, "FavoritesActivity");
            startActivity(displayIntent);

        } else if (noFavorites) {
            // Alert the user that there are not any favorites to display
            Toast toast = Toast.makeText(getApplicationContext(), "You have no Favorites saved.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            // Demand patience from the user
            Toast toast = Toast.makeText(getApplicationContext(), "Please wait while Favorites load.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * This function sends a request to deletes a single verse
     *
     * @param verse_title
     */
    public static void deleteFavorite(String verse_title) {
        // Create the listener for the response from volley
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        Toast toast = Toast.makeText(context, "Verse removed successfully.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    // For logging purposes only
                    String message = jsonResponse.getString("message");
                    Log.i("jsonResponse is ", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DeleteFavoriteRequest deleteFavoriteRequest = new DeleteFavoriteRequest(MainActivity.userId, verse_title, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(deleteFavoriteRequest);
    }

    /**
     * This function sends a request to delete all of the favorites
     *
     */
    private void deleteAllFavorites() {
        // Create the listener for the response.
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Deserialize the response and get all the data.
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String message = jsonResponse.getString("message");
                    Log.i("jsonResponse is ", message);

                    // Let the user in on what happened.
                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();

                    if (success) {
                        favoritesArray = null;
                        favorites.setAdapter(null);
                        // Since there is nothing left to do in the favorites activity, we might as well take the user home.
                        startActivity(new Intent(context, MainActivity.class));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        DeleteAllFavoritesRequest deleteAllFavoritesRequest = new DeleteAllFavoritesRequest(MainActivity.userId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(deleteAllFavoritesRequest);
    }

    /**
     * This function Confirms weather or not the user really wants to delete all the favorites
     * (it could have been an accidental click)
     */
    private void confirmDeleteFavorites() {
        // Listen for the user's response
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // Delete them all!
                        deleteAllFavorites();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // Do nothing.
                        break;
                }
            }
        };

        // If there are favorites to delete, ask the user to confirm their decision.
        if (favoritesArray != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you'd like to delete all your Favorites?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            Toast toast = Toast.makeText(context, "There are no Favorites to be deleted.", Toast.LENGTH_SHORT);
            toast.show(); // Silly user.
        }
    }
}
