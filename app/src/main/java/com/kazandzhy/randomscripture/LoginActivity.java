package com.kazandzhy.randomscripture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class for logging in
 *
 * This Class holds all the functions related to the login activity.
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class LoginActivity extends AppCompatActivity {

    /**
     * This Function is run at the initialization of the activity.
     * It finds the password and email fields to be used later in
     * the activity
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail_login);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword_login);
    }

    /**
     * This Function is run at the initialization of the options menu.
     * It will determine what needs to be in the menu
     *
     * @param menu
     */

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login_loggedout, menu);
        return true;
    }

    /**
     * This Function is run when the user selects an item from the
     * Options menu. It starts the activity associated with the
     * item that the user selected.
     *
     * @param item
     */

    public boolean onOptionsItemSelected(MenuItem item) {
        // Determine witch activity must start, and start it.
        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_signup:
                startActivity(new Intent(this, SignupActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    /**
     * This function sends a LoginRequest via volley to get the userID and
     * other user information.
     *
     * @param view
     */
    public void login(View view) {

        // Identify the views we will need, (email and password)
        final EditText etEmail    = (EditText) findViewById(R.id.etEmail_login);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword_login);
        // Extract the text from the views as a String
        final String email    = etEmail.getText()    + ""; // to cast to Sting type
        final String password = etPassword.getText() + "";

        // Listen for the response from volley
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            /**
             * On Response
             *
             * This Function gets the response from volley in the form of a JSON string.
             * Here, we must deserialize the string, and get the data from it.
             *
             * @param response
             */
            @Override
            public void onResponse(String response) {
                try {
                    // Load the string into a JSON object
                    JSONObject jsonResponse = new JSONObject(response);
                    // First things first, check for success
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        // Here we will deserialize the information
                        String email = jsonResponse.getString("email");
                        String userId = jsonResponse.getString("user_id");

                        // Save userId to sharedPreferences
                        SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.APP_PREFS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPrefs.edit();
                        editor.putString("userId", userId);
                        editor.apply();

                        // Run the welcome Toast
                        Toast toast = Toast.makeText(getApplicationContext(), "Welcome back, " + email + "!", Toast.LENGTH_LONG);
                        toast.show();

                        // And finally return to the home activity
                        Intent goHome = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(goHome);

                    } else {
                        // Here will will prompt the user to try again
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Login failed, please try again.")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };

        // Run the listener
        LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

    }

}
