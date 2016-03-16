package edu.illinois;

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