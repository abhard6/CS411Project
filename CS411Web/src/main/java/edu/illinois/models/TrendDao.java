package edu.illinois.models;

/**
 * Created by nprince on 3/31/16.
 */
import javax.transaction.Transactional;
import javax.xml.transform.Result;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("TrendDao")
public class TrendDao extends BasicDao<Trend>{
    public void deleteOlderThan(Timestamp olderThan){
        String sql = "DELETE FROM " + tableName + " WHERE createdAt < " + olderThan;
        mySql.executeUpdate(sql);
    }

    protected Trend singleResult(ResultSet r) {
        try {
            String value = r.getString("value");
            Timestamp created = r.getTimestamp("created_at");
            Timestamp end = r.getTimestamp("trending_till");
            return new Trend(value,created,end); // Update constructor
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Trend> findAllTrends() {
    	System.out.println("Finding all trends");
    	ResultSet r = mySql.executeQuery("SELECT * FROM Trend");
        return fromResultSet(r);
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
                "(value, created_at, trending_till) " +
                "VALUES(\"" + t.getValue() + "\",\"" + t.getTimestamp() + "\",\"" + t.getEndTimestamp() + "\")" );

        // Save all post relations as well
        for (Post p : t.getPosts()) {
            mySql.executeUpdate("INSERT INTO trended_post" +
                    "(post_id, trend_id) " +
                    "VALUES(" +
                    "(SELECT id FROM Post WHERE id=" + p.getId() + ")," +
                    "(SELECT value FROM " + tableName + " WHERE value=\"" + t.getValue() + "\"))");
        }
    }
    
    public List<MapDetail> wordListForTrend(String val)
    {
    	ResultSet r = mySql.executeQuery("SELECT id, latitude, longitude, sentiment " +
    						"FROM post JOIN trended_post ON post.id = trended_post.post_id " +
    						"WHERE trended_post.trend_id = \"" + val + "\";");
    	List<MapDetail> mapDetailResult = fromResultSetMap(r);
    	return mapDetailResult;
    }
    
    private List<MapDetail> fromResultSetMap(ResultSet r) {
        ArrayList<MapDetail> results = new ArrayList<MapDetail>();
        try {
            while (r.next()) {
                results.add(singleResultMap(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
    
    protected MapDetail singleResultMap(ResultSet r) {
        try {
            long value = r.getLong(1);
            float lat = r.getFloat(2);
            float lon = r.getFloat(3);
            int sentiment = r.getInt(4);
            return new MapDetail(value,lat,lon,sentiment); 
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

	public void updateEndTime(String name, Timestamp now) {
        mySql.executeUpdate("UPDATE" + tableName +
                "SET  trending_till = \"" + now + "\"" +
                "WHERE value = \"" + name + "\")" );
		
	}

	public List<DateTime> daySpanForTrend(String val) {
		// TODO Auto-generated method stub
		ResultSet r = mySql.executeQuery("SELECT * FROM " + tableName + " WHERE value=\"" + val + "\"");

		List<Trend> result = fromResultSet(r);
		if(result.size() > 0)
		{
			ArrayList<DateTime> dayspan = new ArrayList<DateTime>();

			DateTime startDate = new DateTime(result.get(0).getTimestamp());
			int days = Days.daysBetween(startDate, new DateTime(result.get(0).getEndTimestamp())).getDays();
			for (int i=0; i < days; i++) {
				DateTime d = startDate.withFieldAdded(DurationFieldType.days(), i);
				dayspan.add(d);
				System.out.println("added day" + d);
			}
			return dayspan;
		}

		return null;
	}
} // class TrendDao
