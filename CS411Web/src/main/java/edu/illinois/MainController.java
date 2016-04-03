package edu.illinois;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by nprince on 4/3/16.
 * This is the controller for the main interface of the website.
 * This includes the heatmap, wordmap, topbar with user login, and search interface.
 */
@Controller
public class MainController {
    @RequestMapping("/")
    public String mainIndex(Model model) {
        return "basicmap";
    }
}
