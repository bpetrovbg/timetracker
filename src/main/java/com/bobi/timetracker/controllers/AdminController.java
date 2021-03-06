package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.ProjectRepository;
import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserProjectTimeRepository;
import com.bobi.timetracker.models.UserRepository;
import com.bobi.timetracker.services.CheckIsAdminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@RestController
public class AdminController {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UserProjectTimeRepository userProjectTimeRepository;
    private final CheckIsAdminService checkIsAdminService = new CheckIsAdminService();

    public AdminController(UserRepository userRepository, ProjectRepository projectRepository, UserProjectTimeRepository userProjectTimeRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.userProjectTimeRepository = userProjectTimeRepository;
    }

    @GetMapping(value = "/projects")
    private ModelAndView getProjectsPage(HttpSession session) {
        if (checkIsAdminService.isAdmin(session)) {
            return new ModelAndView("projects");
        } else {
            return null;
        }
    }

    @GetMapping(value = "/users")
    private ModelAndView getUsersPage(HttpSession session) {
        if (checkIsAdminService.isAdmin(session)) {
            return new ModelAndView("users");
        } else {
            return null;
        }
    }
}
