package edu.illinois;

import edu.illinois.models.Post;
import edu.illinois.models.PostDao;
import edu.illinois.models.Trend;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.illinois.models.TrendDao;

/**
 * Created by nprince on 4/3/16.
 * This is the controller for the main interface of the website.
 * This includes the heatmap, wordmap, topbar with user login, and search interface.
 */
@Controller
public class MainController {
    @Autowired
    private PostDao postDao;

    @Autowired
    private TrendDao _trendDao;
	
    @RequestMapping("/")
    public String mainIndex(Model model) {
    	
    	System.out.println("returning main");
    	model.addAttribute("alltrends", _trendDao.findAllTrends());
    	model.addAttribute("timespan",new ArrayList<DateTime>());        
    	return "basicmap";

    }
    
    @RequestMapping("/loadtrends")
    public String loadtrends() {    	
    
    	System.out.println("Displaying all trends");
    	/*ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	try {
    		List<Trend> results = _trendDao.findAllTrends();
    		System.out.println("got trends back");
			String json = ow.writeValueAsString(results);
			System.out.println("Printing all trends : " + json);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	*/
    	return null;
    	
    }

    @RequestMapping("/generate-wordmap")
    @ResponseBody
    public List<WordFreqEntry> generateWordmap(@RequestBody WordmapQuery query) {
        return new WordmapGenerator(query, postDao).getResult();
    }
    
    @RequestMapping(value = "/select" , method = RequestMethod.GET)
    public @ResponseBody
    String select(@RequestParam("trend_chosen") String val) {
    	System.out.println("here in select");
    	System.out.println(val);
    	//model.addAttribute("mapdetail",_trendDao.wordListForTrend(val));
    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	try {
			String json = ow.writeValueAsString(_trendDao.wordListForTrend(val));
			System.out.println(json);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return "Result from query"; //Return result from query
        //Query the least created time and the maximum end time from all the posts that belong to the trend
        //Return the timespan
        //Also make a new attribute for the posts
    }
    
    @RequestMapping(value = "/timespan" , method = RequestMethod.GET)
    public @ResponseBody
    String timespan(@RequestParam("trend_chosen") String val) {
    	System.out.println("here in timespan");
    	System.out.println(val);
    	//model.addAttribute("timespan",_trendDao.daySpanForTrend(val));
    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	try {
			String json = ow.writeValueAsString(_trendDao.daySpanForTrend(val));
			System.out.println(json);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
}
