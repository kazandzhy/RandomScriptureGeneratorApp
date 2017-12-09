package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Class to save verse data in SharedPreferences
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class SharedPrefs {

    public static void saveVerseData(ScriptureData verse, String activity) {
        // save all necessary verse attributes to use them in ShowScriptureActivity
        SharedPreferences.Editor editor = MainActivity.sharedPrefs.edit();
        editor.putString("verse_id", Integer.toString(verse.getVerse_id()));
        editor.putString("verse_title", verse.getVerse_title());
        editor.putString("scripture_text", verse.getScripture_text());
        editor.putString("book_title", verse.getBook_title());
        editor.putString("url", GenerateURL.createURL(verse));
        editor.putString("activity", activity);
        editor.apply();
    }

}
