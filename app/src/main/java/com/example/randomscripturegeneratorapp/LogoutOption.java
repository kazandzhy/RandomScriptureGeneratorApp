package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Class to log user out of app
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class LogoutOption {

    /**
     * This function logs a user out by setting userId to null in SharedPreferences
     * @param context
     */
    public static void logOut(Context context) {
        SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();

        // set userId to null
        editor.putString("userId", null);
        editor.apply();

        // Inform user that he or she has been logged out
        Toast toast = Toast.makeText(context, "You have successfully logged out.", Toast.LENGTH_SHORT);
        toast.show();
    }
}
