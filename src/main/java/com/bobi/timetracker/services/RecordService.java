package com.bobi.timetracker.services;

import com.bobi.timetracker.models.Project;
import com.bobi.timetracker.models.Record;
import com.bobi.timetracker.models.RecordRepository;
import com.bobi.timetracker.models.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RecordService {
    private final RecordRepository recordRepository;

    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public Record getRecordByUserAndprojectAndStarttime(User user,
                                                          Project project,
                                                          Timestamp starttime) {
        return recordRepository.findByUserAndProjectAndStarttime(user, project, starttime);
    }

    public List<Record> getRecordsByUserAndProject(User user, Project project) {
        return recordRepository.findByUserAndProject(user, project);
    }

    public Record saveRecord(Record record) {
        return recordRepository.save(record);
    }

    public List<Record> getAllRecords() {
        return (List<Record>) recordRepository.findAll();
    }

    public long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffMinutes;
    }
}
