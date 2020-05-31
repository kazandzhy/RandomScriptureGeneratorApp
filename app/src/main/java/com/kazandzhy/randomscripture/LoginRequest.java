package com.kazandzhy.randomscripture;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for requesting Login
 *
 * This Class holds all the data for sending a volley request for logging in
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class LoginRequest extends StringRequest{

    // Here is the URL for the databases' PHP file
    private static final String LOGIN_REQUEST_URL = "https://www.randomscriptureverse.com/php/loginApp.php";
    // create map params
    private Map<String, String> params;

    /**
     * This function holds all the data necessary for sending a volley request to the database
     *
     * @param email
     * @param password
     * @param listener
     */
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
