package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Project;
import com.bobi.timetracker.models.ProjectRepository;
import com.bobi.timetracker.services.CheckIsAdminService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
//@CrossOrigin(origins = "http://10.10.40.138:8080", maxAge = 3600)
@RestController
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final CheckIsAdminService checkIsAdminService = new CheckIsAdminService();

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects/all")
    List<Project> getAllProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/projects/add", consumes = "application/json", produces = "application/json")
    Project addProject(@RequestBody Project newProject) {
        projectRepository.save(newProject);
        return newProject;
    }

    @RequestMapping(value = "/projects/remove/{id}", method = RequestMethod.DELETE)
    public ModelAndView deleteProject(@PathVariable("id") int projectid, HttpSession session) {
        if (checkIsAdminService.isAdmin(session)) {
            Project project = projectRepository.findProjectById(projectid);
            projectRepository.delete(project);
            return new ModelAndView("/projects");
        } else {
            return null;
        }
    }
}
