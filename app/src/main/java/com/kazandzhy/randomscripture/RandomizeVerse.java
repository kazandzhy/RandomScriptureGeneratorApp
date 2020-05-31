package com.kazandzhy.randomscripture;

import android.annotation.TargetApi;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Class to handle verse randomization
 *
 * This class contains different methods for randomizing scriptures in order to use
 * them in other classes
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class RandomizeVerse {

    // create instance of array of objects
    private ScriptureData[] scriptureArray;
    // create instance of Scripture object
    ScriptureData verse;

    /**
     * This constructor initializes the scripture array by calling WorkWithJSON class
     */
    public RandomizeVerse() {
        // call array of objects from WorkWithJson class
        scriptureArray = new WorkWithJSON().getScriptureArray();
    }

    /**
     * This function randomizes a number from min (inclusive) to max (inclusive).
     *
     * @return random number
     */
    public static int randomizeNumber(int min, int max) {
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min;
        return number;
    }

    /**
     * This function generates a random verse from all standard works. Pure means that each
     * verse has an equal chance of being selected.
     *
     * @return random verse
     */
    @TargetApi(21)
    public ScriptureData pureRandomizeFromAllWorks() {

        // random number from beginning to end of array
        //int number = ThreadLocalRandom.current().nextInt(0, scriptureArray.length);
        int number = randomizeNumber(0, scriptureArray.length - 1);

        // random verse from all standard works
        verse = scriptureArray[number];

        return verse;
    }

    /**
     * This function generates a random verse from all standard works. Weighted means that each
     * standard work, then book, has an equal chance of being selected.
     *
     * @return random verse
     */
    @TargetApi(21)
    public ScriptureData weightedRandomizeFromAllWorks() {

        // random number from 1 to 5 to select which standard work
        //int number = ThreadLocalRandom.current().nextInt(1, 6);
        int number = randomizeNumber(1, 5);
        // random verse from selected standard work
        verse = weightedRandomizeFromWork(number);

        return verse;
    }

    /**
     * This function generates a random verse from a specific standard work. Weighted means
     * that each book in the standard work has an equal chance of being selected.
     *
     * @param input number represents standard work, chosen by user or randomly
     * @return random verse
     */
    @TargetApi(21)
    public ScriptureData weightedRandomizeFromWork(int input) {
        // default range from min to max
        int min = -1;
        int max = -1;

        switch (input) {
            case 1: // books in Old Testament
                min = 1;
                max = 39;
                break;
            case 2: // books in New Testament
                min = 40;
                max = 66;
                break;
            case 3: // books in Book of Mormon
                min = 67;
                max = 81;
                break;
            case 4: // books in Doctrine and Covenants
                min = 82;
                max = 82;
                break;
            case 5: // books in Pearl of Great Price
                min = 83;
                max = 87;
                break;
        }

        // choose book in selected standard work
        //int number = ThreadLocalRandom.current().nextInt(min, max + 1);
        int number = randomizeNumber(min, max);

        // declare book_title with default value
        String book_title = "";
        for (ScriptureData verse : scriptureArray) {
            // find book_title for the book_id that was randomly selected
            if (verse.getBook_id() == number) {
                book_title = verse.getBook_title();
                // end loop once found
                break;
            }
        }
        // random verse from selected book
        verse = randomizeFromBook(book_title);

        return verse;
    }

    /**
     * This function generates a random verse from a specific book of scripture.
     *
     * @param book_title title of specific book, chosen by user or randomly
     * @return random verse
     */
    @TargetApi(21)
    public ScriptureData randomizeFromBook(String book_title) {
        // default range from min to max
        int min = -1;
        int max = -1;
        int book_id = -1;

        // smart for loop through scriptures to find where selected book begins and ends
        for (ScriptureData verse : scriptureArray) {

            // if min not found yet and book_id and input are the same
            if (min == -1 && verse.getBook_title().equals(book_title)) {

                // mark book_id of the first verse of the selected book
                book_id = verse.getBook_id();

                // assign min to the first element of book_id = (verse_id - 1) because 0 position in array
                min = verse.getVerse_id() - 1;

                // if max not found yet - search for  next book_id
            } else if (max == -1 && verse.getBook_id() == book_id + 1) {

                // assign max to the last element of book_id = (verse_id - 2) because 0 position in array
                max = verse.getVerse_id() - 2;

                // stop for loop once we find what we need
                break;
            }
        }

        // if last object(verse) in array
        if (max == -1) {
            // assign max to the length of array - 1
            max = scriptureArray.length - 1;
        }

        // random number in the range from 0 to the length of book objects
        //int number = ThreadLocalRandom.current().nextInt(min, max + 1);
        int number = randomizeNumber(min, max);

        // random verse from selected book
        verse = scriptureArray[number];

        return verse;
    }

    /**
     * This function generates a random verse from a user's Favorites.
     *
     * @param favoritesArray list of user's favorite verses
     * @return random favorite verse
     */
    @TargetApi(21)
    public static ScriptureData randomizeFromFavoriteArray(ScriptureData[] favoritesArray) {

        // random number from beginning to end of array
        //int number = ThreadLocalRandom.current().nextInt(0, favoritesArray.length);
        int number = randomizeNumber(0, favoritesArray.length - 1);

        // random verse from all favorites
        ScriptureData verse = favoritesArray[number];

        return verse;
    }
}
