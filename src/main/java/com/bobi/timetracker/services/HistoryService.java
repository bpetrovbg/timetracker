package com.bobi.timetracker.services;

import com.bobi.timetracker.models.Record;
import com.bobi.timetracker.models.RecordRepository;
import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public HistoryService(RecordRepository recordRepository, UserRepository userRepository) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    public List<Record> getAllRecords() {
        return (List<Record>) recordRepository.findAll();
    }

    public List<Record> getUserRecords(String jsonString) throws JSONException {
        JSONObject inputJSON = new JSONObject(jsonString);
        User currentUser = userRepository.findUserById(Long.parseLong(inputJSON.get("user").toString()));
        List<Record> recordList = recordRepository.findByUser(currentUser);
        List<Record> allQueries = new ArrayList<Record>();

        for (Record record : recordList) {
            if (record.getStarttime().toLocalDateTime().getMonth().getValue() == inputJSON.getInt("month")
                    && record.getStarttime().toLocalDateTime().getYear() == inputJSON.getInt("year")) {
                allQueries.add(record);
            }
        }
        return allQueries;
    }

    public Record getSingleRecord(String jsonString) throws JSONException {
        JSONObject inputJSON = new JSONObject(jsonString);
        User currentUser = userRepository.findUserById(Long.parseLong(inputJSON.get("user").toString()));
        List<Record> recordList = recordRepository.findByUser(currentUser);

        for (Record record : recordList) {
            if (record.getStarttime().toLocalDateTime().getDayOfMonth() == inputJSON.getInt("day") &&
                    record.getStarttime().toLocalDateTime().getMonth().getValue() == inputJSON.getInt("month") &&
                    record.getStarttime().toLocalDateTime().getYear() == inputJSON.getInt("year") &&
                    record.getProject().getId() == inputJSON.getInt("project")) {
                return record;
            }
        }
        return new Record();
    }
}
