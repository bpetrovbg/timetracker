package com.bobi.timetracker.models;

import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;

public interface UserProjectTimeRepository extends CrudRepository<UserProjectTime, Integer> {
    List<UserProjectTime> findByUseridAndProjectid(User userid, Project projectid);

    UserProjectTime findByUseridAndProjectidAndStarttime(User userid, Project projectid, Timestamp starttime);

    List<UserProjectTime> findByUserid(User id);
}
