package com.example.randomscripturegeneratorapp;

import android.content.Context;
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
import android.widget.ListView;
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
        registerCallBack();

    }

    private void registerCallBack() {
        final ListView favoritesList = (ListView) findViewById(R.id.favorites);
        favoritesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList =(String) (favoritesList.getItemAtPosition(myItemInt));
                registerForContextMenu(myView);
                Log.i("selected is ", selectedFromList);

                ScriptureData verse = favoritesArray[myItemInt];



                SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();

                editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
                editor.putString("verse_title", verse.getVerse_title());
                editor.putString("scripture_text", verse.getScripture_text());
                editor.putString("book_title", verse.getBook_title());
                editor.putString("url", URL.createURL(verse));
                editor.putString("activity", "FavoritesActivity");
                editor.apply();

                Intent displayIntent = new Intent(FavoritesActivity.this, ShowScriptureActivity.class);
                startActivity(displayIntent);

                return true;

            }
        });
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
                // code to delete all favorites
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
                    Toast toast = Toast.makeText(getApplicationContext(), "You have no Favorites saved yet.", Toast.LENGTH_LONG);
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

                    String[] verseList = new String[verseTitles.size()];
                    verseTitles.toArray(verseList);

                    //Fill the List

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
            Toast toast = Toast.makeText(getApplicationContext(), "You have no Favorites saved yet.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please wait while Favorites load.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
