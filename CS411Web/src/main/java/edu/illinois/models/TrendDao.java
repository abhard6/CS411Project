package edu.illinois.models;

/**
 * Created by nprince on 3/31/16.
 */
import javax.transaction.Transactional;
import javax.xml.transform.Result;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component("TrendDao")
public class TrendDao extends BasicDao<Trend>{
    public void deleteOlderThan(Timestamp olderThan){
        String sql = "DELETE FROM " + tableName + " WHERE createdAt < " + olderThan;
        mySql.executeUpdate(sql);
    }

    protected Trend singleResult(ResultSet r) {
        try {
            String value = r.getString("value");
            return new Trend(value);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Trend> findByPost(long post_id) {
        ResultSet r = mySql.executeQuery("SELECT * FROM " + tableName + " " +
                "INNER JOIN " +
                "(SELECT trend_id FROM trended_post WHERE post_id=\"" + post_id +"\") as t " +
                "ON t.trend_id=" + tableName + ".value");

        return fromResultSet(r);
    }

    public List<Trend> findByValue(String val) {
        ResultSet r = mySql.executeQuery("SELECT * FROM " + tableName + " WHERE value=\"" + val + "\"");
        return fromResultSet(r);
    }

    public void insert(Trend t) {
        mySql.executeUpdate("INSERT INTO " + tableName +
                "(value, created_at) " +
                "VALUES(\"" + t.getValue() + "\",\"" + t.getTimestamp() + "\")");

        // Save all post relations as well
        for (Post p : t.getPosts()) {
            mySql.executeUpdate("INSERT INTO trended_post" +
                    "(post_id, trend_id) " +
                    "VALUES(" +
                    "(SELECT id FROM Post WHERE id=" + p.getId() + ")," +
                    "(SELECT value FROM " + tableName + " WHERE value=\"" + t.getValue() + "\"))");
        }
    }
} // class TrendDao
