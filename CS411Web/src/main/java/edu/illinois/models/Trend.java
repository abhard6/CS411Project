package edu.illinois.models;

/**
 * Created by nprince on 3/31/16.
 */

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="Trend")
public class Trend {
    @Id
    private String value;

    @ManyToMany(mappedBy = "trends")
    private Collection<Post> posts;


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Trend(String value) {
        this.value = value;
        this.posts = new ArrayList<Post>();
    }

    public Trend() {}

    public Collection<Post> getPosts() {
        return posts;
    }

}
