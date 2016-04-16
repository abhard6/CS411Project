package edu.illinois;

import edu.illinois.models.Post;
import edu.illinois.models.PostDao;
import edu.stanford.nlp.ling.Word;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by nprince on 4/16/16.
 * Creates a list of WordFreqs for a given WordmapQuery
 */
public class WordmapGenerator {
    public Hashtable<String, Integer> getResult() {
        return result;
    }

    private Hashtable<String, Integer> result;

    public WordmapGenerator(WordmapQuery query, PostDao postDao) {
        this.result = getWordCounts(postDao.findMatchingWordmapQuery(query));
    }

    private Hashtable<String, Integer> getWordCounts(List<Post> posts) {
        Hashtable<String, Integer> wordFreqHash = new Hashtable<String, Integer>();

        // Extract word by word from all posts
        for (Post p : posts) {
            for (String word : p.getContent().split("\\s+")) {
                Integer currentFreq = wordFreqHash.get(word);

                if (currentFreq == null) {
                    wordFreqHash.put(word, new Integer(1));
                } else {
                    wordFreqHash.put(word, new Integer(currentFreq.intValue() + 1));
                }
            }
        }

        return wordFreqHash;
    }
}
