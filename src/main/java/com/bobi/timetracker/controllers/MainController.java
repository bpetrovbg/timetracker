package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.UserRepository;
import com.bobi.timetracker.services.CheckIsAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    private final CheckIsAdminService isAdminService;

    public MainController(UserRepository userRepository, CheckIsAdminService isAdminService) {
        this.isAdminService = isAdminService;
    }

    @GetMapping("/main")
    public ModelAndView getMainPage(HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            return new ModelAndView("main");
        } else return null;
    }

    @GetMapping("/admin")
    public ModelAndView getMainPageAdmin(HttpSession session) {
        if (isAdminService.isAdmin(session)) {
            return new ModelAndView("admin");
        } else {
            return null;
        }
    }
}
