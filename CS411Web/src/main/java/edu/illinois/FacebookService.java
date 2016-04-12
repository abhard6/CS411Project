package edu.illinois;

import org.springframework.stereotype.Component;

import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

/**
 * Created by banumuthukumar on 4/2/16.
 */
@Component("FacebookService")

public class FacebookService {
	 public Configuration getConf() {
	        return conf;
	    }

	    private Configuration conf;

	    public FacebookService() {

	        final String USER_TOKEN = "EAAOq6JcZCvUQBAEhkTJuLbjcgZB3PH0wwejgCCIBRRmBZC3Jia3IRLyGvZCoveUkw88eZClqUZAuDh2gTnD7S68QgCixUCQCRh8KMGQeXevZA9VcccjKZAAZBeWVzF01vI15XClrNkMElcpuRnPQRUZC5a0I6m4V5iM8kZD";
	        final String APP_ID = "1032340876803396"; 
	        final String ACCESS_TOKEN = "1032340876803396|DYI4qx_9HzAdZHxvvTSVVzkzims";
	        final String APP_SECRET = "86256309150d448a82abeead78cfc5f7";
	        
	        ConfigurationBuilder cb = new ConfigurationBuilder();
	        cb.setDebugEnabled(true);
	        //cb.setOAuthAppId(APP_ID);
	        //cb.setOAuthAppSecret(APP_SECRET);
	        cb.setOAuthPermissions("email,publish_stream,id, name, first_name, last_name, generic");
	        cb.setUseSSL(true);
	        cb.setJSONStoreEnabled(true);
	        
	        
	        //FacebookFactory ff = new FacebookFactory(cb.build());
	        //Facebook facebook = ff.getInstance();

	     
	       
	   	    this.conf = cb.build();
	    }
}
