package com.example.randomscripturegeneratorapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to send add favorite request to database
 *
 * This class helps with sending user's request to Volley to add one favorite verse, which connects
 * this info with the database used at www.randomscriptureverse.com
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class AddToFavoritesRequest extends StringRequest {
    // URL for connecting user information with the website database
    private static final String ADD_FAVORITE_REQUEST_URL = "http://www.randomscriptureverse.com/php/addVerseToFavoritesApp.php";
    // create map params
    private Map<String, String> params;

    /**
     * This function posts the two necessary variables to the PHP file (user_id and verse_id)
     * using Volley
     *
     * @param user_id
     * @param verse_id
     * @param listener
     */
    public AddToFavoritesRequest(String user_id, String verse_id, Response.Listener<String> listener) {
        super(Method.POST, ADD_FAVORITE_REQUEST_URL, listener, null);
        // this map allows Volley to pass information from AddToFavoritesRequest to addVerseToFavoritesApp.php
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("verse_id", verse_id);
    }

    /**
     * Volley accesses data in map
     *
     * @return parameters of user
     */
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}