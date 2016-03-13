import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;


public  class SentimentAnalysis {
	
	public static List<Integer> sentimentlist = new ArrayList<Integer>();

    public int findSentiment(String line) {

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
       props.put("sentiment.model", "/Users/ashutoshbhardwaj/desktop/stanford-corenlp-full-2015-04-20/src/edu/stanford/nlp/sentiment/model.ser.gz");
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
//        if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
//            return 0 ;
//        }
      
        return mainSentiment;

    }

    public static void main(String[] args) {
        SentimentAnalysis sentimentAnalyzer = new SentimentAnalysis(); 
        StreamTweets object = new StreamTweets();
        for(int i=0; i<object.finalTweet.size();i++){
        	
        	
        	int _Sentimentscore = sentimentAnalyzer.findSentiment(object.finalTweet.get(i));
        	
        	sentimentlist.add(_Sentimentscore);
        }
        
        
    }
}