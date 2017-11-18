package com.example.randomscripturegeneratorapp;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nathan on 11/13 /17.
 */

public class LoginRequest extends StringRequest{

    private static final String LOGIN_REQUEST_URL = "http://www.randomscriptureverse.com/php/loginApp.php";
    // create map params
    private Map<String, String> params;

    public LoginRequest(String email, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
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
