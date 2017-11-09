package com.example.randomscripturegeneratorapp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Vlad on 04.11.2017.
 */

public class WorkWithJSON {
    //array of objects for storing data(verses)
    private static ScriptureData[] scriptureArray;

    //function for converting data from json file to java objects
    public static void deserializeJSON(Context context) {

        //string for loading file
        String JSONData = null;

        // Load file
        try {
            //path to the file
            InputStream is = context.getAssets().open("lds-scriptures.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONData = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //create Gson instance
        Gson gson = new Gson();
        //fill array of objects
        scriptureArray = gson.fromJson(JSONData, ScriptureData[].class);

    }

    //getter for working with private array of objects in other classes
    public ScriptureData[] getScriptureArray() {
        return scriptureArray;
    }
}
