package com.example.randomscripturegeneratorapp;

import android.annotation.TargetApi;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Vlad on 04.11.2017.
 */


public class RandomizeVerse {
    //create instance of array of objects
    ScriptureData[] scriptureArray;
    //WorkWithJSON work;
    //create instance of Scripture object
    ScriptureData verse;

    //default range from min to max
    int min = -1;
    int max = -1;

    //constructor
    public RandomizeVerse() {
        //work = new WorkWithJSON();
        //call array of objects from WorkWithJson class
        scriptureArray = new WorkWithJSON().getScriptureArray();
    }

    @TargetApi(21)
    //function which give random verse from all standard works
    public ScriptureData randomizeFromAllWorks() {

        //random number in the range from 0 to the length of array of objects
        int number = ThreadLocalRandom.current().nextInt(0, scriptureArray.length);

        //random verse from all standard works
        verse = scriptureArray[number];

        return verse;
    }

    @TargetApi(21)
    //function which give random verse from one work chosen by user
    public ScriptureData randomizeFromWork(int input) {

        //smart for loop through all array objects
        for (ScriptureData verse : scriptureArray) {

            //if min not found yet and volume_id and input are the same
            if (min == -1 && verse.getVolume_id() == input) {

                //assign min to the first element of volume_id = (verse_id - 1) because 0 position in array
                min = verse.getVerse_id() - 1;

                //if max not found yet - search for  next volume_id
            } else if (max == -1 && verse.getVolume_id() == input + 1) {

                //assign max to the last element of volume_id = (verse_id - 2) because 0 position in array
                max = verse.getVerse_id() - 2;

                //stop for loop
                break;
            }
        }

        //last object(verse) in array
        if (max == -1) {

            //assign max to the length of array - 1
            max = scriptureArray.length - 1;
        }

        //random number in the range from 0 to the length of volume objects
        int number = ThreadLocalRandom.current().nextInt(min, max + 1);

        //random verse from one work
        verse = scriptureArray[number];


        return verse;

    }

    @TargetApi(21)
    //function which give random verse from one book chosen by user
    public ScriptureData randomizeFromBook(int input) {

        //smart for loop through all array objects
        for (ScriptureData verse : scriptureArray) {

            //if min not found yet and book_id and input are the same
            if (min == -1 && verse.getBook_id() == input) {

                //assign min to the first element of book_id = (verse_id - 1) because 0 position in array
                min = verse.getVerse_id() - 1;

                //if max not found yet - search for  next book_id
            } else if (max == -1 && verse.getBook_id() == input + 1) {

                //assign max to the last element of book_id = (verse_id - 2) because 0 position in array
                max = verse.getVerse_id() - 2;

                //stop for loop
                break;
            }
        }

        //last object(verse) in array
        if (max == -1) {

            //assign max to the length of array - 1
            max = scriptureArray.length - 1;
        }

        //random number in the range from 0 to the length of book objects
        int number = ThreadLocalRandom.current().nextInt(min, max + 1);

        //random verse from one book
        verse = scriptureArray[number];

        return verse;
    }
}