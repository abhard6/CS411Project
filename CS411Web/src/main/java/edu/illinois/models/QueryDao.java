package edu.illinois.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nprince on 4/19/16.
 */
public class QueryDao extends BasicDao<Query> {
    protected Query singleResult(ResultSet r) {
        try {
            return new Query(r.getFloat("latitude_top"), r.getFloat("latitude_bottom"), r.getFloat("longitude_left"), r.getFloat("longitude_right"), r.getString("trend"), r.getString("day"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
