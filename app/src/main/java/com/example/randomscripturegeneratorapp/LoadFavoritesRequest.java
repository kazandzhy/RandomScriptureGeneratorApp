package com.example.randomscripturegeneratorapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for requesting favorites
 *
 * This Class holds all the data for sending a volley request for the users favorites
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class LoadFavoritesRequest extends StringRequest {
    // URL for connecting user information with the website database (loadFavorites.php)
    private static final String REGISTER_REQUEST_URL = "http://www.randomscriptureverse.com/php/loadFavoritesApp.php";
    // create map params
    private Map<String, String> params;


    /**
     * This function takes a listener and a user ID and generates the request to be send to volley
     *
     * @param user_id
     * @param listener
     */
    public LoadFavoritesRequest(String user_id, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        // Create and Populate the Parameters map to be sent via volley
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