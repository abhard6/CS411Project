package edu.illinois.models;

import edu.illinois.Favorite;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nprince on 4/19/16.
 */
@Component("QueryDao")
public class QueryDao extends BasicDao<Query> {
    protected Query singleResult(ResultSet r) {
        try {
            return new Query(r.getLong("id"), r.getFloat("latitude_top"), r.getFloat("latitude_bottom"), r.getFloat("longitude_left"), r.getFloat("longitude_right"), r.getString("trend"), r.getString("day_chosen"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Query> findByAll(Query t) {
        String q = "SELECT * from Query WHERE " +
                "day_chosen=\"" + t.day + "\" AND " +
                "trend=\"" + t.trend + "\"";
        if (t.latitudeBottom != t.latitudeTop) {
            q += " AND ABS(latitude_top-" + t.latitudeTop + ") < 0.01 AND " +
                    "ABS(latitude_bottom-" + t.latitudeBottom + ") < 0.01 AND " +
                    "ABS(longitude_right-" + t.longitudeRight + ") < 0.01 AND " +
                    "ABS(longitude_left-" + t.longitudeLeft + ") < 0.01;";
        }
        return fromResultSet(mySql.executeQuery(q));
    }

    public List<Favorite> findFavorites(String username) {
        ArrayList<Favorite> favs = new ArrayList<Favorite>();
        List<Query> results = fromResultSet(mySql.executeQuery("SELECT * FROM Query INNER JOIN user_query ON user_query.query_id=Query.id WHERE username=\"" + username + "\" "));
        for (Query result : results) {
            // sentiment
            if (result.latitudeBottom == result.latitudeTop) {
                String display = "Sentiment Map: " + result.trend;
                if (result.day != null && !result.day.equals("null")) {
                    display += " on " + result.day;
                }

                // TODO: Banu's way of posting senitment queries messes this whole thing up, fix it
                favs.add(new Favorite(display, "{\"trend\":\"" + result.trend + "\"}"));
            } else {
                // Again, TODO: Banu's day thing might mess things up
                favs.add(new Favorite("Wordmap: " + result.trend + " bound by " + result.latitudeBottom + "," +
                        result.latitudeTop + "," + result.longitudeLeft + "," + result.longitudeRight,
                        "{" +
                        "\"trend\":\""+result.trend+"\"" + "," +
                        "\"latitudeTop\":"+result.latitudeTop + "," +
                        "\"latitudeBottom\":"+result.latitudeBottom + "," +
                        "\"longitudeRight\":"+result.longitudeRight + "," +
                        "\"longitudeLeft\":"+result.longitudeLeft +
                        "}"));
            }
        }

        return favs;
    }

    public void insert(Query t) {
        mySql.executeUpdate("INSERT INTO " + tableName +
                "(latitude_top, latitude_bottom, longitude_left, longitude_right,trend,day_chosen) " +
                "VALUES(" + t.latitudeTop + "," + t.latitudeBottom + "," + t.longitudeLeft + "," + t.longitudeRight + ",\"" + t.trend + "\",\"" + t.day + "\")" );
    }
}
