package com.example.randomscripturegeneratorapp;

/**
 * Class to hold scripture data
 *
 * This class contains different verse properties which will be used in other classes through
 * getters.
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class ScriptureData {

    /* example properties for 1 Nephi 3:7
    {"book_id":67,"verse_id":31153,"book_title":"1 Nephi","volume_lds_url":"bofm",
    "book_lds_url":"1-ne","chapter_number":3,"verse_number":7,"scripture_text":"And it came
    to... which he commandeth them.","verse_title":"1 Nephi 3:7"} */
    private int book_id;
    private int verse_id;
    private String book_title;
    private String volume_lds_url;
    private String book_lds_url;
    private int chapter_number;
    private int verse_number;
    private String scripture_text;
    private String verse_title;

    public int getBook_id() {
        return book_id;
    }

    public int getVerse_id() {
        return verse_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getVolume_lds_url() {
        return volume_lds_url;
    }

    public String getBook_lds_url() {
        return book_lds_url;
    }

    public int getChapter_number() {
        return chapter_number;
    }

    public int getVerse_number() {
        return verse_number;
    }

    public String getScripture_text() {
        return scripture_text;
    }

    public String getVerse_title() {
        return verse_title;
    }

}
