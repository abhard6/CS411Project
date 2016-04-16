package edu.illinois;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class RefinePost {
	
	public static Set<Character> blacklist = new HashSet<Character>();
	public static Set <Character>whitespaceBlacklist = new HashSet<Character>();


	public String removeEmojiAndSymbolFromString(String content){
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

	public String removePunctuation(String original) {
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
	public String removePunctuationWord(String original) {
		String result = "";
		char[] chars = original.toCharArray();
		for(char character : chars) {
			if(character >= 'a' && character <= 'z') {
				result += character;
			}
		}
		return result;
	}

	public void initBlacklist() {

		Character[] blackChars = {'\'','’'};
		Character[] whitespaceBlackChars = {'.',',','\"',';',':','!','£','$','%','^','&','*','(',')','+','=','?','<','>','/','\\','|','{','}','[',']','?','~','`','€','¬','¦','-','_','ã','©'};
		for(Character character : blackChars) {
			blacklist.add(character);
		}
		for(Character character : whitespaceBlackChars) {
			whitespaceBlacklist.add(character);
		}
	} 	

	public String stripPost(String original) {

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
	public int findSentiment(String line) {

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
