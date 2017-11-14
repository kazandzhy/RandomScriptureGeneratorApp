package com.example.randomscripturegeneratorapp;

/**
 * Created by Vlad on 13.11.2017.
 */

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class SignupRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://www.randomscriptureverse.com/php/registerApp.php";
    // create map params
    private Map<String, String> params;

    public SignupRequest(String email, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        // this map allows volley to pass information from SignupRequest to registerApp.php
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    // Volley accesses this data
    @Override
    public Map<String, String> getParams() {
        // get params returns parameters of user
        return params;
    }
}