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
    public List<WordFreqEntry> getResult() {
        return result;
    }

    private List<WordFreqEntry> result;

    public WordmapGenerator(WordmapQuery query, PostDao postDao) {
        this.result = getWordCounts(postDao.findMatchingWordmapQuery(query));
    }

    private List<WordFreqEntry> getWordCounts(List<Post> posts) {
        Hashtable<String, Integer> wordFreqHash = new Hashtable<String, Integer>();
        List<WordFreqEntry> ret = new ArrayList<WordFreqEntry>();

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

        // Put these in the form of wordfreq entries
        for (String word : wordFreqHash.keySet()) {
            ret.add(new WordFreqEntry(word, wordFreqHash.get(word)));
        }

        // Only get the top 10
        PriorityQueue<WordFreqEntry> pq = new PriorityQueue<WordFreqEntry>();
        for (WordFreqEntry entry : ret) {
            pq.add(entry);
        }

        // Now only give the top 50
        ret.clear();
        for (int i = 0; i < 50; i++) {
            ret.add(pq.poll());
        }

        return ret;
    }
}
