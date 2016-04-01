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

	public static Set<Character> blacklist = new HashSet<Character>();
	public static Set <Character>whitespaceBlacklist = new HashSet<Character>();

	public TweetStream(Configuration conf, PostDao dao, TrendDao dao2) throws TwitterException, InterruptedException{

		_postDao = dao;
		_trendDao = dao2;

		twitterStream = new TwitterStreamFactory(conf).getInstance();
		this.geoApiContext = new GeoApiContext().setApiKey("AIzaSyDPj7izY2iXndIdHy1H4n4DNk6HfmXBR-w");
		listener = new StatusListener() {
			int count = 0;


			public void onStatus(Status status) {
				String withoutEmojis = removeEmojiAndSymbolFromString(status.getText());

				String strippedtweet = stripTweet(withoutEmojis);

				String withoutPunctuation = removePunctuation(strippedtweet);

				int sentimentscore = findSentiment(withoutPunctuation);

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
						_postDao.save(p);
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


	public static String removeEmojiAndSymbolFromString(String content){
		String resultStr = "";
		String utf8tweet = "";

		try {
			byte[] utf8Bytes = content.getBytes("UTF-8");
			utf8tweet = new String(utf8Bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		Pattern unicodeOutliers =
				Pattern.compile(
						"[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
						Pattern.UNICODE_CASE |
						Pattern.CANON_EQ |
						Pattern.CASE_INSENSITIVE
						);
		Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(utf8tweet);
		utf8tweet = unicodeOutlierMatcher.replaceAll(" ");

		resultStr = utf8tweet.replaceAll("'", "''");

		return resultStr;
	}

	public static String removePunctuation(String original) {
		String result = "";
		char[] chars = original.toCharArray();
		for(char character : chars) {
			//Check if we need to add whitespace due to a banned char
			if(whitespaceBlacklist.contains(character)) {
				result += " ";
			} //Now check if our char is a non-whitespace banned one (mostly just apostrophes) 
			else if(!blacklist.contains(character)) {
				result += character;
			} 
		}
		return result;
	}


	//Don't have to add in the spaces if it's just an individual word - can do it like this!
	public static String removePunctuationWord(String original) {
		String result = "";
		char[] chars = original.toCharArray();
		for(char character : chars) {
			if(character >= 'a' && character <= 'z') {
				result += character;
			}
		}
		return result;
	}

	public static void initBlacklist() {

		Character[] blackChars = {'\'','’'};
		Character[] whitespaceBlackChars = {'.',',','\"',';',':','!','£','$','%','^','&','*','(',')','+','=','?','<','>','/','\\','|','{','}','[',']','?','~','`','€','¬','¦','-','_','ã','©'};
		for(Character character : blackChars) {
			blacklist.add(character);
		}
		for(Character character : whitespaceBlackChars) {
			whitespaceBlacklist.add(character);
		}
	} 	

	public static String stripTweet(String original) {

		//System.out.println("Stripping: "+original);

		//Convert to lowercase
		String originalLC = original.toLowerCase();

		String[] split = originalLC.split("\\s+");

		String tempResult = "";
		for(String tokenWithPunctuation : split) {
			String token = removePunctuationWord(tokenWithPunctuation);

			//System.out.println("Considering token "+token);

			if(token.equals("")) {
				System.out.println("Empty token");
				continue; // redundant also
			}

			if(token.equals("rt")) {
				//System.out.println("How is rt slipping past? We just caught one");
				continue; //redundant with length check
			}

			//Remove the hash from hashtags
			//TODO: we DEFINITELY want to keep hashtags semantically separate from words!!! TODO TODO TODO
			if(tokenWithPunctuation.charAt(0) == '#') {
				//System.out.println("Found a hashtag, keepin the word");
				token = token.subSequence(1, token.length()).toString(); //Now it's subejct to below checks
			}   			

			//Remove links
			//TODO: Maybe we want to look at links!			
			if(token.length() >= 4 && (token.substring(0, 3).equals("www") || token.substring(0, 4).equals("http"))) {
				//System.out.println("It's a link - why are these getting through!?");
				continue;
			}

			//Remove usernames
			//TODO: Maybe we want to look at usernames!
			if(tokenWithPunctuation.charAt(0) == '@') {
				//System.out.println("Found a username, skipping it");
				continue;
			}

			//System.out.println("Adding the token "+token);
			tempResult += token+" ";
		}

		//System.out.println("Current result is: "+tempResult);

		return tempResult;
	}
	public static int findSentiment(String line) {

		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		props.put("sentiment.model", "model.ser.gz");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		int mainSentiment = 0;
		if (line != null && line.length() > 0) {
			int longest = 0;
			Annotation annotation = pipeline.process(line);
			for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence.get(SentimentAnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String partText = sentence.toString();
				if (partText.length() > longest) {
					mainSentiment = sentiment;
					longest = partText.length();
				}

			}
		}
		//			        if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
			//			            return 0 ;
			//			        }

		return mainSentiment;

	}
}
