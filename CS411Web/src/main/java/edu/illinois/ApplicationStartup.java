package edu.illinois;

import edu.illinois.TrendSetter;
import edu.illinois.models.TrendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import twitter4j.TwitterException;
import twitter4j.conf.Configuration;

@Component
public class ApplicationStartup
        implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private TrendDao _trendDao;

    @Autowired
    @Qualifier("TwitterService")
    private TwitterService twitterService;

    /*
     * This method is called during Spring's startup.
     *
     * @param event Event raised when an ApplicationContext gets initialized or
     * refreshed.
     */
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        try {
            TrendSetter.SetTrends(twitterService.getConf(), _trendDao);
        } catch(TwitterException ex) {
            ex.printStackTrace();
        }

        return;
    }

} // class ApplicationStartup