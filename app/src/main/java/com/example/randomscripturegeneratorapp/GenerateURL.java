package com.example.randomscripturegeneratorapp;

/**
 * Class to generate URL
 *
 * This class uses verse properties and generates a URL format in order to simplify the code
 * in other classes. This URL opens the selected verse in Gospel Library or LDS.org.
 *
 * @author Vlad Kazandzhy, Nathan Tagg, Tyler Braithwaite
 */

public class GenerateURL {

    /**
     * This function uses workId, bookId, chapId, and verseId to generate a specific URL
     *
     * @param verse
     * @return the generated verse URL
     */
    public static String createURL(ScriptureData verse) {
        String workId = String.valueOf(verse.volume_lds_url);
        String bookId = String.valueOf(verse.book_lds_url);
        String chapId = String.valueOf(verse.chapter_number);
        String verseId = String.valueOf(verse.verse_number);
        String url = "https://www.lds.org/scriptures/" + workId + "/" + bookId + "/" + chapId + "." + verseId + "?lang=eng#p"+ verseId;

        return url;
    }
}
