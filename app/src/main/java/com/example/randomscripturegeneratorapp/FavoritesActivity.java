package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    public static ScriptureData[] favoritesArray;
    private static Context context;
    private Boolean noFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity);
        context = this;


        //Populate the listView
        populateFavoritesList();

        //Listen for clicks
        //registerCallBack();

    }

    public void displayFavorite(View view) {

        // Get the verse title
        TextView t = (TextView)view;
        String verseTitle = t.getText().toString();
        Log.i("you selected", verseTitle);

        // Find the verse from the favoritesArray
        int position = 0;
        for (int i = 0; i < favoritesArray.length; i++) {
            if (favoritesArray[i].getVerse_title() == verseTitle){
                position = i;
                break;
            }
        }
        ScriptureData verse = favoritesArray[position];

        // Display the Scripture
        Intent displayIntent = new Intent(this, ShowScriptureActivity.class);
        SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();
        editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
        editor.putString("verse_title", verse.getVerse_title());
        editor.putString("scripture_text", verse.getScripture_text());
        editor.putString("url", URL.createURL(verse));
        editor.putString("activity", "FavoritesActivity");
        editor.apply();
        startActivity(displayIntent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorites, menu);
        return true;
    }

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
                UserSettings.logOut(getApplicationContext());
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

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
                    ListView favorites = (ListView) findViewById(R.id.favorites);
                    favorites.setAdapter(adapter);

                }
            }
        };

        LoadFavoritesRequest loadFavoritesRequest = new LoadFavoritesRequest(userId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(FavoritesActivity.this);
        queue.add(loadFavoritesRequest);

    }

    private void randomizeFromFavorites() {
        if (favoritesArray != null) {
            ScriptureData verse = RandomizeVerse.randomizeFromFavoriteArray(favoritesArray);

            Intent displayIntent = new Intent(this, ShowScriptureActivity.class);

            SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();

            editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
            editor.putString("verse_title", verse.getVerse_title());
            editor.putString("scripture_text", verse.getScripture_text());
            editor.putString("url", URL.createURL(verse));
            editor.putString("activity", "FavoritesActivity");
            editor.apply();

            startActivity(displayIntent);
        } else if (noFavorites) {
            Toast toast = Toast.makeText(getApplicationContext(), "You have no Favorites saved.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please wait while Favorites load.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void deleteFavorite(String verse_title) {

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
                    // for logging purposes only
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

    private void deleteAllFavorites() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String message = jsonResponse.getString("message");
                    Log.i("jsonResponse is ", message);

                    Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
                    toast.show();

                    if (success) {
                        favoritesArray = null;
                        startActivity(new Intent(context, FavoritesActivity.class));
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

    private void confirmDeleteFavorites() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        deleteAllFavorites();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        if (favoritesArray != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you'd like to delete all your Favorites?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        } else {
            Toast toast = Toast.makeText(context, "There are no Favorites to be deleted.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
