package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Project;
import com.bobi.timetracker.models.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectController {
    private final ProjectRepository projectRepository;

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
}
