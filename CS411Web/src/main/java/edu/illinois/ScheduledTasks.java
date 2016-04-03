package edu.illinois;

/**
 * Created by nprince on 3/31/16.
 *
 * Contains scheduled events (using cron style)
 *
 * Current events include:
 *  deleting outdated posts and trends daily
 *  adding new posts and trends daily
 */
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.illinois.models.PostDao;
import edu.illinois.models.Trend;
import edu.illinois.models.TrendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;

@Component
public class ScheduledTasks {
    @Autowired
    private PostDao _postDao;

    @Autowired
    private TrendDao _trendDao;

    @Autowired
    private TwitterService twitterService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron="0 0 9 * * *" )
    public void clearOldTweets() {
        _postDao.deleteOlderThan(getAWeekAgo());

        System.out.println("DELETED SOME SHIT TWEETS");
    }

    private Timestamp getAWeekAgo() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);

        Timestamp t =  new java.sql.Timestamp(cal.getTime().getTime());
        return t;
    }

    @Scheduled(cron="0 0 9 * * *" )
    public void deleteOldTrends() {
        _trendDao.deleteOlderThan(getAWeekAgo());

        System.out.println("DELETED SOME SHIT TRENDS");
    }

    @Scheduled(cron="0 0 9 * * *" )
    public void getTodaysTrends() {
        try {
            TrendSetter.SetTrends(twitterService.getConf(), _trendDao);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }
}
