package edu.illinois;

import edu.illinois.models.PostDao;
import edu.illinois.models.TrendDao;
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
            if (_trendDao.findByValue(t.getName()).size() == 0) {
                edu.illinois.models.Trend myTrend = new edu.illinois.models.Trend(t.getName());
                _trendDao.insert(myTrend);
            }
        }
    }
}
