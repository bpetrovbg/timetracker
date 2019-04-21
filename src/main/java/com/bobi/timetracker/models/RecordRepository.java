package com.bobi.timetracker.models;

import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface RecordRepository extends CrudRepository<Record, Integer> {
    List<Record> findByUserAndProject(User user, Project project);

    Record findByUserAndProjectAndStarttime(User user, Project project, Timestamp starttime);

    List<Record> findByUser(User user);
}
