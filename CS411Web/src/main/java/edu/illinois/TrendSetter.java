package edu.illinois;

import edu.illinois.models.PostDao;
import edu.illinois.models.TrendDao;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.Trend;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.api.TrendsResources;
import twitter4j.conf.Configuration;

/**
 * Created by nprince on 3/31/16.
 *
 * A helper class to gather current trending terms from twitter and store them in the databse.
 */
public class TrendSetter {

    /**
     * Gathers trending terms and stores them in the db
     * @param conf The twitter configuration. Autowire a TwitterService to the controller and use it's method to get the twitter conf
     * @param _trendDao The trend data access object. This can also be autowired to a controller.
     * @throws TwitterException
     */
    public static void SetTrends(Configuration conf, TrendDao _trendDao) throws TwitterException {
        TrendsResources trends = new TwitterFactory(conf).getInstance().trends();

        Trend[] trendsArr = trends.getPlaceTrends(23424977).getTrends();

        for (Trend t : trendsArr) {

            // Only save new trends
        	List<edu.illinois.models.Trend> existingTrend = _trendDao.findByValue(t.getName());
            if (existingTrend.size() == 0) {
                edu.illinois.models.Trend myTrend = new edu.illinois.models.Trend(t.getName());
                _trendDao.insert(myTrend);
            }
            else
            {
            	Timestamp end_at;
				if(existingTrend.size() == 1)
				{
					end_at = existingTrend.get(0).getEndTimestamp();
					if(end_at != null)
					{	Timestamp now = new Timestamp(System.currentTimeMillis());
						if(end_at.before(now))
							_trendDao.updateEndTime(t.getName(), now);
					}
				}
            }
        }
    }
}
