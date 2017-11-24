package com.example.randomscripturegeneratorapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vlad on 23.11.2017.
 */

public class AddToFavoritesRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://www.randomscriptureverse.com/php/addVerseToFavoritesApp.php";
    // create map params
    private Map<String, String> params;

    public AddToFavoritesRequest(String user_id, String verse_id, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        // this map allows volley to pass information from AddToFavoritesRequest to addVerseToFavoritesApp.php
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("verse_id", verse_id);
    }

    // Volley accesses this data
    @Override
    public Map<String, String> getParams() {
        // get params returns parameters of user
        return params;
    }
}