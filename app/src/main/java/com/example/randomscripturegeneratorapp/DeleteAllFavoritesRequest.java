package com.example.randomscripturegeneratorapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vlad on 01.12.2017.
 */

public class DeleteAllFavoritesRequest extends StringRequest {
    private static final String REMOVE_FAVORITE_REQUEST_URL = "http://www.randomscriptureverse.com/php/deleteAllFavoritesApp.php";
    // create map params
    private Map<String, String> params;

    public DeleteAllFavoritesRequest(String user_id, Response.Listener<String> listener) {
        super(Request.Method.POST, REMOVE_FAVORITE_REQUEST_URL, listener, null);
        // this map allows volley to pass information from DeleteAllFavoritesRequest to deleteAllFavoritesApp.php
        params = new HashMap<>();
        params.put("user_id", user_id);
    }

    // Volley accesses this data
    @Override
    public Map<String, String> getParams() {
        // get params returns parameters of user
        return params;
    }
}
