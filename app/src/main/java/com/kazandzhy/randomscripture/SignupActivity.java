package com.kazandzhy.randomscripture;

import android.content.Intent;
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
 * Activity to sign user up for account
 *
 * This activity takes a user's information and uses SignupRequest to register the user in
 * the database. Signing up allows the user to add/view/manage favorite scriptures.
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class SignupActivity extends AppCompatActivity {

    /**
     * This function shows the layout on the screen and allows user to interact with the
     * app by inputting information
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
    }

    /**
     * This function creates a toolbar menu
     *
     * @param menu
     * @return true to create menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_signup_loggedout, menu);
        return true;
    }

    /**
     * This function handles all possible menu options
     *
     * @param item
     * @return true to handle action
     */
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Function to work with SignupRequest
     *
     * This function is called when user clicks the Sign Up button. It uses an instance of
     * SignupRequest to send user information to database. Then a response is received from
     * the database about success of user registration.
     *
     *
     * @param view
     */
    public void signupRequest(View view) {
        // find elements in the signup_activity layout
        final EditText etEmail = (EditText) findViewById(R.id.etEmail_signup);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword_signup);
        final EditText etConfirm = (EditText) findViewById(R.id.etConfirm);

        // retrieve user input
        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String confirm = etConfirm.getText().toString();

        // handle response received from database
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // convert String into JSON object
                    JSONObject jsonResponse = new JSONObject(response);
                    // retrieve success and message properties from JSON object
                    boolean success = jsonResponse.getBoolean("success");
                    String errorMsg = jsonResponse.getString("errorMsg");

                    // inform user about success
                    if (success) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Successfully registered! You may log in now.", Toast.LENGTH_LONG);
                        toast.show();

                        // go to LoginActivity
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else { // show error message and ask user to make another attempt
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                        builder.setMessage(errorMsg)
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        // compare password and confirm password fields
        if (password.equals(confirm)) { // if they match
            // send info to database through SignupRequest
            SignupRequest signupRequest = new SignupRequest(email, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
            queue.add(signupRequest);
        } else { // if they don't match
            // ask user to make another attempt
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("Passwords don't match. Please try again.")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        }
    }
}
