package com.example.randomscripturegeneratorapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to send delete favorite request to database
 *
 * This class helps with sending user's request to delete one verse to Volley, which connects this info
 * with the database used at www.randomscriptureverse.com
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class DeleteFavoriteRequest extends StringRequest {
    // URL for connecting user information with the website database
    private static final String REMOVE_FAVORITE_REQUEST_URL = "http://www.randomscriptureverse.com/php/removeVerseFromFavoritesApp.php";
    // create map params
    private Map<String, String> params;

    /**
     * This function posts the two necessary variables to the PHP file (user_id and verse_title)
     * using Volley
     *
     * @param user_id
     * @param verse_title
     * @param listener
     */
    public DeleteFavoriteRequest(String user_id, String verse_title, Response.Listener<String> listener) {
        super(Request.Method.POST, REMOVE_FAVORITE_REQUEST_URL, listener, null);
        // this map allows Volley to pass information from DeleteFavoriteRequest to removeVerseFromFavoritesApp.php
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("verse_title", verse_title);
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
