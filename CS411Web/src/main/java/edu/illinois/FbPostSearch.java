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
import org.springframework.beans.factory.annotation.Autowired;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.conf.Configuration;
import twitter4j.FilterQuery;
import twitter4j.StatusListener;
public class FbPostSearch {

	private PostDao _postDao;
	private TrendDao _trendDao;

	private Facebook facebook;
	private FilterQuery tweetFilterQuery;
	private StatusListener listener;
	
	private RefinePost refinePost = new RefinePost();

	public void startFbSearch(PostDao dao, TrendDao dao2, Configuration conf) 
	{

		_trendDao = dao2;
		_postDao = dao;
		ArrayList<Trend> postTrends = new ArrayList<Trend>();

		Iterable<Trend> trends = _trendDao.findAll();
		if(trends != null)
			System.out.println("NOT NULL trends");
		facebook = new FacebookFactory(conf).getInstance();

		// Need to update!! 
		String ats = "EAACEdEose0cBADqkAfdC7AzSbl9wtrDSJGWUka3ZBZCZC2kei2wSDqaQWcV98DmZBlqhPzHZA2XAhhJPTTmPmhkPebSrazmWHkg45kJ8ZC2ZBUl4ZA3rtm2EfeZBkZAsN9QAPnmFKo1LQgl2S9huak0hyHEpF5vdnVLlo7vX1tGJ0AngZDZD";
		
		String APP_ID = "1032340876803396"; 
		String APP_SECRET = "86256309150d448a82abeead78cfc5f7";
		facebook.setOAuthAppId(APP_ID, APP_SECRET);
		//AccessToken at = facebook.getOAuthAccessToken();
		//facebook.setOAuthAppId("", "");
		AccessToken at = new AccessToken(ats);

		// Set access token.
		facebook.setOAuthAccessToken(at);
		
		//For every trend, search Facebook
		for (Trend trend : trends) {
			System.out.println("Working on trend for fb : " + trend.getValue());
			
			//Figure out if the trend is new.
			//If it is less than a day old, find posts from a week back
			//Else find only new posts
			java.util.Date dt = new Date();
			Calendar c = Calendar.getInstance(); 
			c.setTime(dt); 
			c.add(Calendar.DATE, -1);
			dt = c.getTime();
			Boolean newTrend = false;
			if(trend.getTimestamp().compareTo(dt) > 0)
			{
				newTrend = true;
			}
			if(!newTrend)
			{
				c.add(Calendar.DATE, -6);
				dt = c.getTime();
			}

			//Get all pages for a trend
			try {
				ResponseList<facebook4j.Page> resultsPages = facebook.searchPages(trend.getValue());
				if(resultsPages != null)
				{
					for(facebook4j.Page page : resultsPages)
					{
						ResponseList<facebook4j.Post> results = facebook.getFeed(page.getId(), new Reading().since(dt));
						
						if(results != null)
						{
							for (facebook4j.Post post : results)
							{
								if(post.getMessage() != null)
								{
									//Find all trends for the post
									for (Trend trend2 : trends) {
										if (post.getMessage().contains(trend2.getValue())) {
											postTrends.add(trend2);
										}
									}

									// Only save if it had some trends
									if (postTrends.size() >= 0) {

										java.sql.Timestamp timestamp = new java.sql.Timestamp(post.getCreatedTime().getTime());

									
										//String post_id = post.getId();
										double latitude = 0;
										double longitude = 0;

										if(post.getPlace() != null)
											if(post.getPlace().getLocation() != null)
											{
												latitude = post.getPlace().getLocation().getLatitude();
												longitude = post.getPlace().getLocation().getLongitude();
											}
										if (latitude != 0 || longitude != 0){
											String withoutEmojis = refinePost.removeEmojiAndSymbolFromString(post.getMessage());
											String strippedPost = refinePost.stripPost(withoutEmojis);
											String withoutPunctuation = refinePost.removePunctuation(strippedPost);


											int sentimentscore = refinePost.findSentiment(withoutPunctuation);

											Post p = new Post(timestamp, withoutPunctuation, sentimentscore, (float) latitude, (float) longitude, "facebook", postTrends);
											_postDao.save(p);
											System.out.println(post.getId() + post.getMessage() + post.getPlace() + post.getMessage());
											System.out.println("Created a post!!!");
										}
									}
								}
							}
							System.out.println("One set of posts done in a page");

						}
						}
					System.out.println("One set of pages done for a trend");
				} 
				}
			
			catch (FacebookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}

	//	WHAT DOES THIS DO??

	/*		tweetFilterQuery = new FilterQuery(); // See
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
	 */

}
