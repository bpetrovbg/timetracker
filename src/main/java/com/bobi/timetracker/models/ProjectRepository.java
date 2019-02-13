package com.bobi.timetracker.models;

import org.springframework.data.repository.CrudRepository;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    Project findProjectById(Integer id);
}
