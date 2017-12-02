package com.example.randomscripturegeneratorapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vlad on 01.12.2017.
 */

public class DeleteFavoriteRequest extends StringRequest {
    private static final String REMOVE_FAVORITE_REQUEST_URL = "http://www.randomscriptureverse.com/php/removeVerseFromFavoritesApp.php";
    // create map params
    private Map<String, String> params;

    public DeleteFavoriteRequest(String user_id, String verse_title, Response.Listener<String> listener) {
        super(Request.Method.POST, REMOVE_FAVORITE_REQUEST_URL, listener, null);
        // this map allows volley to pass information from DeleteFavoriteRequest to removeVerseFromFavoritesApp.php
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("verse_title", verse_title);
    }

    // Volley accesses this data
    @Override
    public Map<String, String> getParams() {
        // get params returns parameters of user
        return params;
    }
}
