package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Project;
import com.bobi.timetracker.services.CheckIsAdminService;
import com.bobi.timetracker.services.ProjectService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
//@CrossOrigin(origins = "http://192.168.0.103:8080", maxAge = 3600)
@RestController
public class ProjectController {
    private final CheckIsAdminService checkIsAdminService;
    private final ProjectService projectService;

    public ProjectController(CheckIsAdminService checkIsAdminService, ProjectService projectService) {
        this.checkIsAdminService = checkIsAdminService;
        this.projectService = projectService;
    }

    @GetMapping("/projects/all")
    List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/projects/add", consumes = "application/json", produces = "application/json")
    ModelAndView addProject(@RequestBody Project newProject, HttpSession session) {
        if(checkIsAdminService.isAdmin(session)) {
            projectService.addProject(newProject);
            return new ModelAndView("/projects");
        }
        return null;
    }

    /*@RequestMapping(value = "/projects/remove/{id}", method = RequestMethod.DELETE)
    public ModelAndView deleteProject(@PathVariable("id") int projectid, HttpSession session) {
        if (checkIsAdminService.isAdmin(session)) {
            projectService.removeProject(projectid);
            return new ModelAndView("/projects");
        } else {
            return null;
        }
    }*/
}
