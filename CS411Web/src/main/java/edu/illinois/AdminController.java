package edu.illinois;

import edu.illinois.models.Post;
import edu.illinois.models.PostDao;
import edu.illinois.models.TrendDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import twitter4j.conf.Configuration;

import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private PostDao _postDao;

    @Autowired
    private TrendDao _trendDao;

    @Autowired
    @Qualifier("TwitterService")
    private TwitterService twitterService;

    private TweetStream tweetStream;

    @RequestMapping("/admin")
    public String index(Model model) {
        model.addAttribute("entries", _postDao.findAll());
        return "index";
    }

    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    public @ResponseBody
    boolean delete(@RequestBody Map<String,String> body) {
        long id = Long.parseLong(body.get("id"));
        _postDao.delete(id);

        return true;
    }

    @RequestMapping(value = "/update" , method = RequestMethod.POST)
    public @ResponseBody
    boolean update(@RequestBody Map<String,String> body) {
        long id = Long.parseLong(body.get("id"));
        Post p = _postDao.findOne(id);

        p.setContent(body.get("content"));
        p.setLatitude(Float.parseFloat(body.get("latitude")));
        p.setLongitude(Float.parseFloat(body.get("longitude")));
        p.setSentiment(Integer.parseInt(body.get("sentiment")));
        p.setSource(body.get("source"));

        _postDao.save(p);

        return true;
    }

    @RequestMapping(value = "/collect" , method = RequestMethod.POST)
    public @ResponseBody
    boolean collect(@RequestBody Map<String,String> body) {
        System.out.println("Received POST request:" + body);

        try {
            tweetStream = new TweetStream(twitterService.getConf(), _postDao, _trendDao);
            tweetStream.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @RequestMapping(value = "/stop" , method = RequestMethod.POST)
    public @ResponseBody
    boolean stop(@RequestBody Map<String,String> body) {
        System.out.println("Received POST request:" + body);
        if (tweetStream != null) {
            tweetStream.stop();
        }
        return true;
    }
}