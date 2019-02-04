package com.bobi.timetracker.models;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    Project findProjectById(Integer id);
}
