package com.kazandzhy.randomscripture;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class to work with the JSON file
 *
 * This class deserializes a JSON file, which includes scriptures from all standard works, into
 * a Java format in order to use the data in our app
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class WorkWithJSON {
    // array of objects for storing data (verses)
    private static ScriptureData[] scriptureArray;

    /**
     * Function for converting data from JSON file to Java objects
     *
     * @param context
     */
    public static void deserializeJSON(Context context) {

        // string for loading file contents
        String JSONData = null;

        // Load file
        try {
            // path to the file in assets folder
            InputStream is = context.getAssets().open("lds-scriptures.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONData = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // create Gson instance
        Gson gson = new Gson();
        // fill array of objects
        scriptureArray = gson.fromJson(JSONData, ScriptureData[].class);

    }

    /**
     * Getter for working with private array of objects in other classes
     *
     * @return array of scripture verses
     */
    public ScriptureData[] getScriptureArray() {
        return scriptureArray;
    }
}
