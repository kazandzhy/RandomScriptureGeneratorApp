package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by nathan on 11/17/17.
 */

public class UserSettings {

    public static void logOut(Context context) {
        SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();

        editor.putString("userId", null);
        editor.apply();

        Toast toast = Toast.makeText(context, "You have successfully logged out.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
