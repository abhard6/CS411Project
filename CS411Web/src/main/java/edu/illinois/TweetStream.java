package edu.illinois;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import edu.illinois.models.Post;
import edu.illinois.models.PostDao;
import edu.illinois.models.Trend;
import edu.illinois.models.TrendDao;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;

import org.springframework.beans.factory.annotation.Autowired;
import twitter4j.*;
import twitter4j.api.PlacesGeoResources;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.ConfigurationBuilder;

public class TweetStream {
	private PostDao _postDao;
	private TrendDao _trendDao;

	private TwitterStream twitterStream;
	private GeoApiContext geoApiContext;
	private FilterQuery tweetFilterQuery;
	private StatusListener listener;
	
	private RefinePost refinePost = new RefinePost();


	public TweetStream(Configuration conf, PostDao dao, TrendDao dao2) throws TwitterException, InterruptedException{

		_postDao = dao;
		_trendDao = dao2;

		twitterStream = new TwitterStreamFactory(conf).getInstance();
		this.geoApiContext = new GeoApiContext().setApiKey("AIzaSyDPj7izY2iXndIdHy1H4n4DNk6HfmXBR-w");
		listener = new StatusListener() {
			int count = 0;


			public void onStatus(Status status) {
				String withoutEmojis = refinePost.removeEmojiAndSymbolFromString(status.getText());

				String strippedtweet = refinePost.stripPost(withoutEmojis);

				String withoutPunctuation = refinePost.removePunctuation(strippedtweet);

				int sentimentscore = refinePost.findSentiment(withoutPunctuation);

				int tweet_id = (int) status.getId();

				// Get timestamp
				java.sql.Timestamp timestamp = new java.sql.Timestamp(status.getCreatedAt().getTime());

				ArrayList<Trend> postTrends = new ArrayList<Trend>();

				Iterable<Trend> trends = _trendDao.findAll();
				for (Trend trend : trends) {
					if (status.getText().contains(trend.getValue())) {
						postTrends.add(trend);
					}
				}
				
				//Getting info from Facebook based on trends
				
							// Only save if it had some trends
				if (postTrends.size() > 0) {
					double latitude = 0;
					double longitude = 0;

					if(status.getGeoLocation() != null) {
						latitude = status.getGeoLocation().getLatitude();
						longitude = status.getGeoLocation().getLongitude();
					} else {
						try {
							GeocodingResult[] results =  GeocodingApi.geocode(geoApiContext,
                                    status.getUser().getLocation()).await();
							latitude = results[0].geometry.location.lat;
							longitude = results[0].geometry.location.lng;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (latitude != 0 || longitude != 0){
						Post p = new Post(timestamp, withoutPunctuation, sentimentscore, (float) latitude, (float) longitude, "twitter", postTrends);
						_postDao.insert(p);
						System.out.println(status.getId() + status.getText() + status.getGeoLocation() + status.getUser().getLocation());
					}
				}
			}
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}

			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}
		};


		tweetFilterQuery = new FilterQuery(); // See
		String keywords[] = {};

		tweetFilterQuery.track(keywords); // OR on keywords
		tweetFilterQuery.locations(new double[][]{new double[]{-126.562500,30.448674},
				new double[]{-61.171875,44.087585
		}}); 
		tweetFilterQuery.language(new String[]{"en"}); // Note that language does not work properly


		// FilterQuery fq = new FilterQuery();
		//fq.track(keywords);
		//twitterStream.sample();
	}

	public void start() {
		twitterStream.addListener(listener);
		twitterStream.filter(tweetFilterQuery);
	}

	public void stop() {
		twitterStream.cleanUp();
		twitterStream.shutdown();
	}


}
