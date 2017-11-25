package com.example.randomscripturegeneratorapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nathan on 24.11.2017.
 */

public class LoadFavoritesRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://www.randomscriptureverse.com/php/loadFavoritesApp.php";
    private Map<String, String> params;

    public LoadFavoritesRequest(String user_id, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}