package com.example.randomscripturegeneratorapp;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
    }

    public void goHome(View view) {
        Intent goHome = new Intent(this, MainActivity.class);
        startActivity(goHome);
    }

    public void signupRequest(View view) {
        final EditText etEmail = (EditText) findViewById(R.id.etEmail_signup);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword_signup);
        final EditText etConfirm = (EditText) findViewById(R.id.etConfirm);

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String confirm = etConfirm.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String errorMsg = jsonResponse.getString("errorMsg");

                    if (success) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Successfully registered! You may log in now.", Toast.LENGTH_LONG);
                        toast.show();

                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
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

        if (password.equals(confirm)) {
            SignupRequest signupRequest = new SignupRequest(email, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(SignupActivity.this);
            queue.add(signupRequest);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setMessage("Passwords don't match. Please try again.")
                    .setNegativeButton("Retry", null)
                    .create()
                    .show();
        }
    }
}
