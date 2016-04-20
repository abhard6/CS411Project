package edu.illinois.models;

import java.util.List;

/**
 * Created by nprince on 4/19/16.
 */
public class Query {
    private long id;
    private float latitudeTop;
    private float latitudeBottom;
    private float longitudeLeft;
    private float longitudeRight;
    private String trend;
    private String day;

    public Query(float latitudeTop, float latitudeBottom, float longitudeLeft, float longitudeRight, String trend, String day) {this.latitudeBottom = latitudeBottom;
        this.latitudeTop = latitudeTop;
        this.longitudeLeft = longitudeLeft;
        this.longitudeRight = longitudeRight;
        this.trend = trend;
        this.day = day;
    }

    public Query() {

    }
}
