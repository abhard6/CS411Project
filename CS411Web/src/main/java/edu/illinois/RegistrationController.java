package edu.illinois;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.illinois.models.Login;
import edu.illinois.models.UserDao;




@Controller
//@SessionAttributes("login");


public class RegistrationController {
	  @RequestMapping(value="/registration", method=RequestMethod.GET)
	    public String greetingForm(Model model) {
	        model.addAttribute("registration", new Registration());
	        return "registration";
	    }

	    /*@RequestMapping(value="/greeting", method=RequestMethod.GET)
	    public String greetingSubmit(@ModelAttribute Greeting greeting, Model model) {
	        model.addAttribute("greeting", greeting);
	        return "result";
	    }*/
	    
	    @RequestMapping(value="/registration", method=RequestMethod.POST)
	    public String registerUser(@ModelAttribute Registration greeting, Model model) {
	        model.addAttribute("greeting", greeting);
	        UserDao userdao = new UserDao();
	        greeting.setUsername(greeting.getUsername());
	        greeting.setPassword(greeting.getPassword());
	        userdao.insert(greeting);
	        return "index";
	    }
	    
	    @RequestMapping(value="/login", method=RequestMethod.GET)
	    public String validateUsr(Model model) {	
	        model.addAttribute("login", new Login());
	        return "login";
	    }
	    
	    @RequestMapping(value="/login", method=RequestMethod.POST)
	    public String validateUsr(@ModelAttribute Login login, Model model, HttpSession session) {
	        model.addAttribute("login", login);
	        UserDao userdao = new UserDao();
	        login.setUserName(login.getUserName());
	        login.setPassword(login.getPassword());
	        session.setAttribute("login", login.getUserName()); 
	        System.out.println(session.getId());
	        System.out.println(session.getAttribute("login"));
	        // saving username
	        boolean flag = userdao.validateUser(login);
	        if(flag)
	        {
	        	//login.setMessage("Welcome!! "+login.getUserName() );
	        	//login.setValidUser(true);
	        	return "basicmap";
	        }
	        else
	        {
	        	login.setValidUser(false);
	        	login.setmessage("Invalid credentials. Try again");
	        	return "login";
	        }
	    }
	    
	    
	    @RequestMapping(value = "/logout", method = RequestMethod.POST)
	      public String logout(HttpSession session ) {
	    	System.out.println("I'm here !!");
	    	System.out.println(session.getAttribute("login"));
	    	if(session != null){
	    		session.invalidate();
	    	}
	    	
	    	
	       return "redirect:/login";
	      }

	}
