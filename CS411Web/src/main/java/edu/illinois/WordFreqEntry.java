package edu.illinois;

import edu.stanford.nlp.ling.Word;

/**
 * Created by nprince on 4/16/16.
 */
public class WordFreqEntry implements Comparable<WordFreqEntry>{
    public String text;
    public int size;

    public WordFreqEntry() {

    }

    public WordFreqEntry(String text, int size) {
        this.text = text;
        this.size = size;
    }

    public int compareTo(WordFreqEntry to) {
        return to.size - this.size;
    }
}
