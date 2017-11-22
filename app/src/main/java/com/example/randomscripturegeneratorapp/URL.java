package com.example.randomscripturegeneratorapp;

/**
 * Created by Vlad on 21.11.2017.
 */

public class URL {

    public static String createURL(ScriptureData verse) {
        String workId = String.valueOf(verse.volume_lds_url);
        String bookId = String.valueOf(verse.book_lds_url);
        String chapId = String.valueOf(verse.chapter_number);
        String verseId = String.valueOf(verse.verse_number);
        String url = "https://www.lds.org/scriptures/" + workId + "/" + bookId + "/" + chapId + "." + verseId + "?lang=eng#p"+ verseId;

        return url;
    }
}
