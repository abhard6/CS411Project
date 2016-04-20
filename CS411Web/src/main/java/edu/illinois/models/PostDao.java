package edu.illinois.models;

/**
 * Created by nprince on 3/16/16.
 */
import javax.transaction.Transactional;

import edu.illinois.WordmapQuery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Component("PostDao")
public class PostDao extends BasicDao<Post>{
    protected Post singleResult(ResultSet r) {
        try {
            long id = r.getLong("id");
            TrendDao trendDao = new TrendDao();

            return new Post(
                    id,
                    r.getTimestamp("timestamp"),
                    r.getString("content"),
                    r.getInt("sentiment"),
                    r.getFloat("latitude"),
                    r.getFloat("longitude"),
                    r.getString("source"),
                    trendDao.findByPost(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Post> findByTrend(String trend_id) {
        ResultSet r = mySql.executeQuery("SELECT * FROM " + tableName + " " +
                "INNER JOIN " +
                "(SELECT post_id FROM trended_post WHERE trend_id=\"" + trend_id +"\") as t " +
                "ON t.post_id=" + tableName + ".id");

        return fromResultSet(r);
    }

    public List<Post> findByTrends(List<String> trend_ids) {
        String sql = "SELECT * FROM " + tableName + " " +
                "INNER JOIN " +
                "(SELECT post_id FROM trended_post WHERE ";


        for (int i = 0; i < trend_ids.size(); i++) {
            sql += "trend_id=\"" + trend_ids.get(i) + "\"";

            if (i == trend_ids.size()-1) {
                sql += ") as t ";
            } else {
                sql += " AND ";
            }
        }
        sql += "ON t.post_id=" + tableName + ".id";
        ResultSet r = mySql.executeQuery(sql);

        return fromResultSet(r);
    }

    public List<Post> findMatchingWordmapQuery(WordmapQuery query) {
        String sql = "select * from trended_post" +
                " inner join " +
                "(select * from Post where " +
                "latitude>" + query.latitudeBottom + " and " +
                "latitude<" + query.latitudeTop + " and " +
                "longitude>" + query.longitudeLeft + " and " +
                "longitude<" + query.longitudeRight + ") as p " +
                "on trended_post.post_id=p.id " +
                "where trended_post.trend_id=\"" + query.trend + "\";";

        ResultSet r = mySql.executeQuery(sql);

        return fromResultSet(r);
    }

    public Post getLastPost() {
       return fromResultSet(
               mySql.executeQuery("SELECT * FROM Post WHERE id=(SELECT MAX(id) FROM " + tableName + ")")
       ).get(0);
    }

    public void insert(Post p) {
        mySql.executeUpdate("INSERT INTO " + tableName +
                "(content, latitude, longitude, sentiment, source, timestamp) " +
                "VALUES(\"" +
                        p.getContent() + "\",\"" +
                        p.getLatitude() + "\",\"" +
                        p.getLongitude() + "\",\"" +
                        p.getSentiment() + "\",\"" +
                        p.getSource() + "\",\"" +
                        p.getTimestamp() + "\")"
        );


        // Posts created by the main tweet constructor don't actually have an ID. The db auto puts it in
        long id = getLastPost().getId();
        p.setId(id);

        // Save all trend relations as well
        for (Trend t : p.getTrends()) {
            mySql.executeUpdate("INSERT INTO trended_post" +
                    "(post_id, trend_id) " +
                    "VALUES(" +
                    "(SELECT id FROM " + tableName + " WHERE id=" + p.getId() + ")," +
                    "(SELECT value FROM Trend WHERE value=\"" + t.getValue() + "\"))");
        }
    }

    public void update(Post p) {
        mySql.executeUpdate("UPDATE " + tableName + " " +
                "SET content=\"" + p.getContent() + "\", " +
                "timestamp=\"" + p.getTimestamp() + "\", " +
                "sentiment=" + p.getSentiment() + ", " +
                "latitude="+ p.getLatitude() + ", " +
                "longitude=" + p.getLongitude() + ", " +
                "source=\"" + p.getSource() + "\" WHERE " +
                "id=" + p.getId());
    }

    public void delete(Post p) {
        // Delete all associated trended
        for (Trend t : p.getTrends()) {
            mySql.executeUpdate("DELETE FROM trended_post WHERE trend_id=\"" + t.getValue() + "\" AND post_id=" + p.getId());
        }

        mySql.executeUpdate("DELETE FROM " + tableName + " WHERE id=" + p.getId());
    }

//    public void update(Post p) {
//        // Delete all associated trended
//        mySql.executeUpdate("UPDATE Post");
//        for (Trend t : p.getTrends()) {
//            mySql.executeUpdate("DELETE FROM Trend WHERE value"=t.getValue());
//        }
//
//        mySql.executeUpdate("DELETE FROM " + tableName + " WHERE id=" + p.getId());
//    }
}

//@Transactional
//public interface PostDao extends Repository<Post, Long> {
//    @Modifying
//    @Query("DELETE FROM Post WHERE timestamp < :olderThan")
//    Long deleteOlderThan(@Param("olderThan") Timestamp olderThan);
//} // class UserDao
