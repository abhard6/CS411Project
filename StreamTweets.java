import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;


public class StreamTweets {
	
	public static Set<Character> blacklist = new HashSet<Character>();
	public static Set <Character>whitespaceBlacklist = new HashSet<Character>();
	public static List<String> TweetText = new ArrayList<String>();
	public static List<String> finalTweet = new ArrayList<String>();
	public static String fileName = "/Users/ashutoshbhardwaj/Desktop/TWEETS/" + ".json";
	
	
	public static void TweetsStream() throws TwitterException, InterruptedException{
		
    	final String ACCESS_TOKEN = "2909068004-RYCWeuz3sNgo5mnWMdi3KpKU6qTij7X10YiOC7T";

			final String ACCESS_TOKEN_SECRET = "u1RcApATLWIVDAsNhtZ9jKKBSY2slk79FczDJ0MLpFuSM";

			final String CONSUMER_KEY = "6wLy7xmTXSopQWpv8SvlUasob";

			final String CONSUMER_SECRET = "DS4QbQzTm4xojUQs6VIMjJae4mK8sBy8Zql9Wt8RV2HJVxk0gh";


			ConfigurationBuilder builder = new ConfigurationBuilder();

			builder.setOAuthAccessToken(ACCESS_TOKEN);

			builder.setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

			builder.setOAuthConsumerKey(CONSUMER_KEY);

			builder.setOAuthConsumerSecret(CONSUMER_SECRET);
			builder.setJSONStoreEnabled(true);
			
			new File("/Users/ashutoshbhardwaj/Desktop/TWEETS").mkdir();
			
			
			TwitterStream twitterStream = new TwitterStreamFactory(builder.build()).getInstance();
		    StatusListener listener = new StatusListener() {

		        public void onStatus(Status status) {
		        	
		        	String rawJSON = TwitterObjectFactory.getRawJSON(status);
		        	try {
						storeJSON(rawJSON, fileName);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	TweetText.add(status.getText()); 	
		        	
		            System.out.println(status.getId() + status.getText() + status.getGeoLocation()+ status.getUser().getLocation());
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

				@Override
				public void onStallWarning(StallWarning arg0) {
					// TODO Auto-generated method stub
					
				}
		    };
		    
		    
		    FilterQuery tweetFilterQuery = new FilterQuery(); // See 
		    String keywords[] = {"Illinois", "Happy","sad"};
		    
		    tweetFilterQuery.track(keywords); // OR on keywords
		    tweetFilterQuery.locations(new double[][]{new double[]{-126.562500,30.448674},
		                    new double[]{-61.171875,44.087585
		                    }}); // See https://dev.twitter.com/docs/streaming-apis/parameters#locations for proper location doc. 
		    //Note that not all tweets have location metadata set.
		    tweetFilterQuery.language(new String[]{"en"}); // Note that language does not work properly
		    
		    
		   // FilterQuery fq = new FilterQuery();
		    

		    //fq.track(keywords);

		    twitterStream.addListener(listener);
		    twitterStream.filter(tweetFilterQuery);
		    //twitterStream.sample();
		    
		    Thread.sleep(50000);
		    twitterStream.cleanUp();
		    twitterStream.shutdown();
	}
	
	
	public static void storeJSON(String rawJSON, String fileName) throws IOException 
	{
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			fos = new FileOutputStream(fileName);
			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			bw.write(rawJSON);
			bw.flush();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ignore) {
				}
			}
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException ignore) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ignore) {
				}
			}
		}
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

		System.out.println("Stripping: "+original);

		//Convert to lowercase
		String originalLC = original.toLowerCase();

		String[] split = originalLC.split("\\s+");

		String tempResult = "";
		for(String tokenWithPunctuation : split) {
			String token = removePunctuationWord(tokenWithPunctuation);

			System.out.println("Considering token "+token);

			if(token.equals("")) {
				System.out.println("Empty token");
				continue; // redundant also
			}

			if(token.equals("rt")) {
				System.out.println("How is rt slipping past? We just caught one");
				continue; //redundant with length check
			}

			//Remove the hash from hashtags
			//TODO: we DEFINITELY want to keep hashtags semantically separate from words!!! TODO TODO TODO
			if(tokenWithPunctuation.charAt(0) == '#') {
				System.out.println("Found a hashtag, keepin the word");
				token = token.subSequence(1, token.length()).toString(); //Now it's subejct to below checks
			}   			

			//Remove links
			//TODO: Maybe we want to look at links!			
			if(token.length() >= 4 && (token.substring(0, 3).equals("www") || token.substring(0, 4).equals("http"))) {
				System.out.println("It's a link - why are these getting through!?");
				continue;
			}

			//Remove usernames
			//TODO: Maybe we want to look at usernames!
			if(tokenWithPunctuation.charAt(0) == '@') {
				System.out.println("Found a username, skipping it");
				continue;
			}

			System.out.println("Adding the token "+token);
			tempResult += token+" ";
		}

		System.out.println("Current result is: "+tempResult);

		return tempResult;
	}
    
    
    public static void stringreadyforsentiment(){

		for( int i = 0; i<TweetText.size();i++){
			
			String withoutEmojis = removeEmojiAndSymbolFromString(TweetText.get(i));

			String strippedtweet = stripTweet(withoutEmojis);
			
			String withoutPunctuation = removePunctuation(strippedtweet);
			
			finalTweet.add(withoutPunctuation);
			
			
		}
    
    }
	    public static void main(String[] args) throws TwitterException, InterruptedException{
	   
	    	TweetsStream();
	    	stringreadyforsentiment();
	    	
	}
	    
	    
	    
	}
