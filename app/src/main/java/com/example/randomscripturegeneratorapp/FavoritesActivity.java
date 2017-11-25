package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

    private static ScriptureData[] favoritesArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites_activity);


        //Populate the listView
        populateFavoritesList();

    }







    private void populateFavoritesList() {
        // Get the USER_ID
        SharedPreferences prefs = getSharedPreferences(MainActivity.APP_PREFS, MODE_PRIVATE);
        String userId = (prefs.getString("userId", ""));

        // Get Items from Database
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //create Gson instance
                Gson gson = new Gson();
                //fill array of objects
                favoritesArray = gson.fromJson(response, ScriptureData[].class);
            }
        };

        LoadFavoritesRequest loadFavoritesRequest = new LoadFavoritesRequest(userId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(FavoritesActivity.this);
        queue.add(loadFavoritesRequest);


        // Make the list of Items
        List<String> verseTitles = new ArrayList<String>();
        for (int i = 0; i < favoritesArray.length; i++) {
            verseTitles.add(favoritesArray[i].getVerse_title());
        }

        String[] verseList = new String[verseTitles.size()];
        verseTitles.toArray(verseList);

        // Build the adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item,
                verseList);

        // Configure the list view
        ListView favorites = (ListView) findViewById(R.id.favorites);
        favorites.setAdapter(adapter);
    }



















    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_other_loggedin, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
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

}
