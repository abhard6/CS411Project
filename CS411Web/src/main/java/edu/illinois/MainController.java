package edu.illinois;

import edu.illinois.models.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

import javax.servlet.http.HttpSession;

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

	@Autowired
	private QueryDao queryDao;

	@Autowired
	private UserDao userDao;
	
    @RequestMapping("/")
    public String mainIndex(Model model, HttpSession session) {
		String username = (String) session.getAttribute("login");

		System.out.println("returning main");
    	model.addAttribute("alltrends", _trendDao.findAllTrends());
    	model.addAttribute("timespan",new ArrayList<DateTime>());
		model.addAttribute("favorites", queryDao.findFavorites(username));
    	return "basicmap";
    }
    
    @RequestMapping("/loadtrends")
    @ResponseBody
    public List<String> loadtrends() {    	
    
    	ArrayList<String> words = new ArrayList<String>();
    	List<Trend> trends = _trendDao.findAll();
    	
    	for (Trend t : trends) {
    		words.add(t.getValue());
    	}
    	
    	return words;
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

	@RequestMapping(value="/favorite")
	@ResponseBody
	public void favorite(@RequestBody Query query, HttpSession session, Model model) {
		String username = (String) session.getAttribute("login");

		List<Query> found = queryDao.findByAll(query);
		if (found.size() == 0) {
			queryDao.insert(query);
			found = queryDao.findByAll(query);
		}

		if (userDao.foundUserQuery(username, found.get(0))) {
			return; // do nothing
		}
		userDao.insertUserQuery(username, found.get(0));

		model.addAttribute("favorites", queryDao.findFavorites(username));
	}

	@RequestMapping(value="/remove_favorite")
	@ResponseBody
	public void remove_favorite(@RequestBody Query query, HttpSession session, Model model) {
		String username = (String) session.getAttribute("login");

		List<Query> found = queryDao.findByAll(query);
		if (found.size() == 0) {
			// do nothing
			return;
		}

		userDao.removeUserQuery(username, found.get(0));
		model.addAttribute("favorites", queryDao.findFavorites(username));
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
    @RequestMapping(value = "/daychoose" , method = RequestMethod.GET)
    public @ResponseBody
    String daychoose(@RequestParam("day_chosen") String date, @RequestParam("trend") String trend ) {
    	System.out.println("here in day chosen");
    	System.out.println(date);
    	DateTimeFormatter formatter = DateTimeFormat.forPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (z)");
    	DateTime dt = formatter.withOffsetParsed().parseDateTime(date);
    	System.out.println("After dateformet : " + dt);
    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	try {
			String json = ow.writeValueAsString(_trendDao.wordListForTrendWithinDate(dt, trend));
			System.out.println(json);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }

}
