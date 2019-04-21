package com.bobi.timetracker.services;

import com.bobi.timetracker.models.Project;
import com.bobi.timetracker.models.Record;
import com.bobi.timetracker.models.RecordRepository;
import com.bobi.timetracker.models.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class GetRecordService {
    private final RecordRepository recordRepository;

    public GetRecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Record getUserByuserAndprojectAndStarttime(User user,
                                                          Project project,
                                                          Timestamp starttime) {
        return recordRepository.findByUserAndProjectAndStarttime(user, project, starttime);
    }
}
