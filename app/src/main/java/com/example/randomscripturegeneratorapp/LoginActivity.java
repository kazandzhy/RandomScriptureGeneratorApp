package com.example.randomscripturegeneratorapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail_login);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword_login);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void goHome(View view) {
        Intent goHome = new Intent(this, MainActivity.class);
        startActivity(goHome);
    }

    public void login(View view) {
        final EditText etEmail    = (EditText) findViewById(R.id.etEmail_login);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword_login);
        final String email    = etEmail.getText()    + ""; // to cast to Sting type
        final String password = etPassword.getText() + "";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {
                        String email = jsonResponse.getString("email");
                        String userId = jsonResponse.getString("user_id");
                        MainActivity.userId = userId;
                        Toast toast = Toast.makeText(getApplicationContext(), "Welcome back, " + email + "!", Toast.LENGTH_LONG);
                        toast.show();

                    } else {
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

        LoginRequest loginRequest = new LoginRequest(email, password, responseListener);

    }

}
