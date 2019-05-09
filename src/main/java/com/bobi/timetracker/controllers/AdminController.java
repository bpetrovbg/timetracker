package com.bobi.timetracker.controllers;

import com.bobi.timetracker.services.CheckIsAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
public class AdminController {
    private final CheckIsAdminService isAdminService;

    public AdminController(CheckIsAdminService checkIsAdminService) {
        this.isAdminService = checkIsAdminService;
    }

    @GetMapping(value = "/projects")
    private ModelAndView getProjectsPage(HttpSession session) {
        if (isAdminService.isAdmin(session)) {
            return new ModelAndView("projects");
        } else {
            return null;
        }
    }

    @GetMapping(value = "/users")
    private ModelAndView getUsersPage(HttpSession session) {
        if (isAdminService.isAdmin(session)) {
            return new ModelAndView("users");
        } else {
            return null;
        }
    }

    /*@GetMapping(value = "/roles")
    private ModelAndView getRolesPage(HttpSession session) {
        if(isAdminService.isAdmin(session)) {
            return new ModelAndView("roles");
        } else {
            return null;
        }
    }*/
}
