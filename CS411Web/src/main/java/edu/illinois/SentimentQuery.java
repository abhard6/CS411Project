package edu.illinois;

import java.util.List;

/**
 * Created by nprince on 4/16/16.
 */
public class SentimentQuery {
    public List<String> trends;

    public SentimentQuery() {

    }

    public SentimentQuery(List<String> trends) {
        this.trends = trends;
    }
}
