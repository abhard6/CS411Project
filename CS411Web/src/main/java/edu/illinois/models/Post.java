package edu.illinois.models;

/**
 * Created by nprince on 3/16/16.
 */
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Collection;

public class Post {

    private long id;

    private Timestamp timestamp;

    private String content;

    private int sentiment;

    public float latitude;
    public float longitude;


    public String source;
    public Collection<Trend> trends;

    public Post() { }

    public Post(long id) {
        this.id = id;
    }

    public Post(long id, Timestamp timestamp, String content, int sentiment, float latitude, float longitude, String source, Collection<Trend> trends) {
        this.id = id;
        this.timestamp = timestamp;
        this.content = content;
        this.sentiment = sentiment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
        this.trends = trends;
    }

    public Post(Timestamp timestamp, String content, int sentiment, float latitude, float longitude, String source, Collection<Trend> trends) {
        this.timestamp = timestamp;
        this.content = content;
        this.sentiment = sentiment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
        this.trends = trends;
    }


    public void setId(long id) { this.id = id; }
    public long getId() {
        return id;
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


    public int getSentiment() {
        return sentiment;
    }

    public void setSentiment(int sentiment) {
        this.sentiment = sentiment;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Collection<Trend> getTrends() {
        return trends;
    }

    public void addTrend(Trend t) {
        trends.add(t);
    }

} // class Post