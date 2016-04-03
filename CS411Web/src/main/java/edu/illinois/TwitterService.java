package edu.illinois;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by nprince on 3/31/16.
 *
 * Allows the twitter configuration, and any other necessary twitter dependencies
 * to be autowired.
 */
@Component("TwitterService")
public class TwitterService {
    public Configuration getConf() {
        return conf;
    }

    private Configuration conf;

    public TwitterService() {

        final String ACCESS_TOKEN = "2909068004-RYCWeuz3sNgo5mnWMdi3KpKU6qTij7X10YiOC7T";

        final String ACCESS_TOKEN_SECRET = "u1RcApATLWIVDAsNhtZ9jKKBSY2slk79FczDJ0MLpFuSM";

        final String CONSUMER_KEY = "6wLy7xmTXSopQWpv8SvlUasob";

        final String CONSUMER_SECRET = "DS4QbQzTm4xojUQs6VIMjJae4mK8sBy8Zql9Wt8RV2HJVxk0gh";


        ConfigurationBuilder builder = new ConfigurationBuilder();

        builder.setDebugEnabled(true);

        builder.setOAuthAccessToken(ACCESS_TOKEN);

        builder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

        builder.setOAuthConsumerKey(CONSUMER_KEY);

        builder.setOAuthConsumerSecret(CONSUMER_SECRET);
        builder.setJSONStoreEnabled(true);

        this.conf = builder.build();
    }
}
