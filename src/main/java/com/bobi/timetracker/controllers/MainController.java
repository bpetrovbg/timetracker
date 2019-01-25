package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    private final UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/main")
    public ModelAndView getMainPage(HttpSession session) {
        User user = userRepository.findUserByUsername("bpetrov");
        session.setAttribute("currentuser", user);
        if(session.getAttribute("currentuser") != null) {
            return new ModelAndView("main");
        }
        else return null;
    }
}
