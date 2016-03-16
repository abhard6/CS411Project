package edu.illinois;

import edu.illinois.models.Post;
import edu.illinois.models.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private PostDao _postDao;

    private TweetStream tweetStream;

    @RequestMapping("/")
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
            tweetStream = new TweetStream(_postDao);
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