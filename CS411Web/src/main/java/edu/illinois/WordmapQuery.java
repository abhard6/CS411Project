package edu.illinois;

import java.util.List;

/**
 * Created by nprince on 4/16/16.
 */
public class WordmapQuery {
    public float latitudeTop;
    public float latitudeBottom;
    public float longitudeLeft;
    public float longitudeRight;
    public List<String> trends;

    public WordmapQuery(float latitudeTop, float latitudeBottom, float longitudeLeft, float longitudeRight, List<String> trends) {
        this.latitudeBottom = latitudeBottom;
        this.latitudeTop = latitudeTop;
        this.longitudeLeft = longitudeLeft;
        this.longitudeRight = longitudeRight;
        this.trends = trends;
    }

    public WordmapQuery() {

    }
}
