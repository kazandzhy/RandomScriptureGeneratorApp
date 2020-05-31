package com.kazandzhy.randomscripture;

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
     * Correct format should be: https://www.lds.org/study/scriptures/nt/matt/1?id=p1-p12#p1
     *
     * @param verse
     * @return the generated verse URL
     */
    public static String createURL(ScriptureData verse) {
        String workId = String.valueOf(verse.getVolume_lds_url());
        String bookId = String.valueOf(verse.getBook_lds_url());
        String chapId = String.valueOf(verse.getChapter_number());
        String verseId = String.valueOf(verse.getVerse_number());
        String url = "https://www.lds.org/study/scriptures/" + workId + "/" + bookId + "/" + chapId + "?id=p" + verseId + "#p"+ verseId;

        return url;
    }
}
