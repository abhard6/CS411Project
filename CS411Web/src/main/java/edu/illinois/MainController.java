package edu.illinois;

import edu.illinois.models.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by nprince on 4/3/16.
 * This is the controller for the main interface of the website.
 * This includes the heatmap, wordmap, topbar with user login, and search interface.
 */
@Controller
public class MainController {
    @Autowired
    private PostDao postDao;

    @RequestMapping("/")
    public String mainIndex(Model model) {
        return "basicmap";
    }

    @RequestMapping("/generate-wordmap")
    @ResponseBody
    public Hashtable<String, Integer> generateWordmap(@RequestBody WordmapQuery query) {
        return new WordmapGenerator(query, postDao).getResult();
    }
}
