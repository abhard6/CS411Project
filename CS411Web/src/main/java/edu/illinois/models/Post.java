package edu.illinois.models;

/**
 * Created by nprince on 3/16/16.
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Entity
@Table(name="Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @NotNull
    private Timestamp timestamp;

    @NotNull
    @Size(min=1, max=500)
    private String content;

    @NotNull
    private int sentiment;

    public float latitude;
    public float longitude;


    @NotNull
    public String source;


    public Post() { }

    public Post(long id) {
        this.id = id;
    }

    public Post(Timestamp timestamp, String content, int sentiment, float latitude, float longitude, String source) {
        this.id = id;
        this.timestamp = timestamp;
        this.content = content;
        this.sentiment = sentiment;
        this.latitude = latitude;
        this.longitude = longitude;
        this.source = source;
    }

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

} // class Post