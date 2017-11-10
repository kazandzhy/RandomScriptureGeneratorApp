package com.example.randomscripturegeneratorapp;

/**
 * Created by Vlad on 04.11.2017.
 */

public class ScriptureData {
    private int volume_id; // needed
    private int book_id; // needed
    int chapter_id;
    private int verse_id;
    String volume_title;
    private String book_title;
    String volume_long_title;
    String book_long_title;
    String volume_subtitle;
    String book_subtitle;
    String volume_short_title;
    String book_short_title;
    String volume_lds_url;
    String book_lds_url;
    int chapter_number;
    int verse_number;
    private String scripture_text; // needed
    private String verse_title; // needed
    String verse_short_title;

    public ScriptureData(int volume_id, int book_id, String scripture_text, String verse_title, String book_title) {
        this.volume_id = volume_id;
        this.book_id = book_id;
        this.scripture_text = scripture_text;
        this.verse_title = verse_title;
        this.book_title = book_title;
    }

    public int getVolume_id() {
        return volume_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getScripture_text() {
        return scripture_text;
    }

    public String getVerse_title() {
        return verse_title;
    }

    public int getVerse_id() {
        return verse_id;
    }

    // [{"volume_id":1,"book_id":1,"chapter_id":1,"verse_id":1,"volume_title":"Old Testament","book_title":"Genesis","volume_long_title":"The Old Testament","book_long_title":"The First Book of Moses called Genesis","volume_subtitle":"","book_subtitle":"","volume_short_title":"OT","book_short_title":"Gen.","volume_lds_url":"ot","book_lds_url":"gen","chapter_number":1,"verse_number":1,"scripture_text":"IN the beginning God created the heaven and the earth.","verse_title":"Genesis 1:1","verse_short_title":"Gen. 1:1"},
}
