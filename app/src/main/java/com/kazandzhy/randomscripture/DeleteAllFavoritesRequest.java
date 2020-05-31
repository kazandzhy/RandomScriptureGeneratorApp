package com.kazandzhy.randomscripture;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to send delete all favorites request to database
 *
 * This class helps with sending user's delete all verses request to Volley, which connects this info
 * with the database used at www.randomscriptureverse.com
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class DeleteAllFavoritesRequest extends StringRequest {
    // URL for connecting user information with the website database
    private static final String REMOVE_FAVORITES_REQUEST_URL = "https://www.randomscriptureverse.com/php/deleteAllFavoritesApp.php";
    // create map params
    private Map<String, String> params;

    /**
     * This function posts the one necessary variable (user_id) to the PHP file
     * using Volley
     *
     * @param user_id
     * @param listener
     */
    public DeleteAllFavoritesRequest(String user_id, Response.Listener<String> listener) {
        super(Request.Method.POST, REMOVE_FAVORITES_REQUEST_URL, listener, null);
        // this map allows Volley to pass information from DeleteAllFavoritesRequest to deleteAllFavoritesApp.php
        params = new HashMap<>();
        params.put("user_id", user_id);
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
