package com.kazandzhy.randomscripture;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to send register request to database
 *
 * This class helps with sending user registration info to Volley, which connects this info
 * with the database used at www.randomscriptureverse.com
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class SignupRequest extends StringRequest {
    // URL for connecting user information with the website database
    private static final String REGISTER_REQUEST_URL = "https://www.randomscriptureverse.com/php/registerApp.php";
    // create map params
    private Map<String, String> params;

    /**
     * This function posts the two necessary variables to the PHP file (email and password)
     * using Volley
     *
     * @param email
     * @param password
     * @param listener
     */
    public SignupRequest(String email, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        // this map allows Volley to pass information from SignupRequest to registerApp.php
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
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