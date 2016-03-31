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
 */
public class TrendSetter {
    public static void SetTrends(Configuration conf, TrendDao _trendDao) throws TwitterException {
        _trendDao.deleteAll();

        TrendsResources trends = new TwitterFactory(conf).getInstance().trends();

        Trend[] trendsArr = trends.getPlaceTrends(23424977).getTrends();

        for (Trend t : trendsArr) {
            edu.illinois.models.Trend myTrend = new edu.illinois.models.Trend(t.getName());

            _trendDao.save(myTrend);
        }
    }
}
