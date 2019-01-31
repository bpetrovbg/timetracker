package com.bobi.timetracker.services;

import com.bobi.timetracker.models.Project;
import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserProjectTime;
import com.bobi.timetracker.models.UserProjectTimeRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class GetUserProjectTimeService {
    private final UserProjectTimeRepository userProjectTimeRepository;

    public GetUserProjectTimeService(UserProjectTimeRepository userProjectTimeRepository) {
        this.userProjectTimeRepository = userProjectTimeRepository;
    }

    public UserProjectTime getUserByUseridAndProjectidAndStarttime(User userid,
                                                                   Project projectid,
                                                                   Timestamp starttime) {
        return userProjectTimeRepository.findByUseridAndProjectidAndStarttime(userid,projectid,starttime);
    }
}
