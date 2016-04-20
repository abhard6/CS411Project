package edu.illinois.models;

import java.util.List;

/**
 * Created by nprince on 4/19/16.
 */
public class Query {
    public long id;
    public float latitudeTop;
    public float latitudeBottom;
    public float longitudeLeft;
    public float longitudeRight;
    public String trend;
    public String day;

    public Query(float latitudeTop, float latitudeBottom, float longitudeLeft, float longitudeRight, String trend, String day) {this.latitudeBottom = latitudeBottom;
        this.latitudeTop = latitudeTop;
        this.longitudeLeft = longitudeLeft;
        this.longitudeRight = longitudeRight;
        this.trend = trend;
        this.day = day;
    }

    public Query(long id, float latitudeTop, float latitudeBottom, float longitudeLeft, float longitudeRight, String trend, String day) {this.latitudeBottom = latitudeBottom;
        this.latitudeTop = latitudeTop;
        this.longitudeLeft = longitudeLeft;
        this.longitudeRight = longitudeRight;
        this.trend = trend;
        this.day = day;
        this.id = id;
    }

    public Query() {

    }
}
