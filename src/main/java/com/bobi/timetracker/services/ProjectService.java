package com.bobi.timetracker.services;

import com.bobi.timetracker.models.Project;
import com.bobi.timetracker.models.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    /*public void removeProject(int projectid) {
        Project project = projectRepository.findProjectById(projectid);
        projectRepository.delete(project);
    }*/
}
