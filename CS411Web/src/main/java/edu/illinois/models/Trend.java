package edu.illinois.models;

/**
 * Created by nprince on 3/31/16.
 */

import org.apache.tomcat.jni.Time;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class Trend {
    private String value;

    private Timestamp createdAt;
    
    private Timestamp trendingTill;	

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public Timestamp getEndTimestamp() {
    	return trendingTill;
    }
    
    public Timestamp getTimestamp() {
    	return createdAt;
    }

    public Trend(String value) {
        this.value = value;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdAt);
        cal.add(Calendar.DAY_OF_WEEK, 1);
        trendingTill = new Timestamp(cal.getTime().getTime());
    }

    public Trend() {}

    /**
     * Get all of the posts associated with this trend
     *    NOTE: This is pulled from the DB to save speed, as not every time we need a trend do we need all of its posts
     * @return
     */
    public Collection<Post> getPosts() {
        PostDao dao = new PostDao();

        return dao.findByTrend(value);
    }
}
