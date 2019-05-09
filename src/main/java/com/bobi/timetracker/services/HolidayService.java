package com.bobi.timetracker.services;

import com.bobi.timetracker.models.Holiday;
import com.bobi.timetracker.models.HolidayRepository;
import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HolidayService {
    private final UserRepository userRepository;
    private final HolidayRepository holidayRepository;

    public HolidayService(UserRepository userRepository, HolidayRepository holidayRepository) {
        this.userRepository = userRepository;
        this.holidayRepository = holidayRepository;
    }

    public List<Holiday> getHolidaysByUser(User user) {
        return holidayRepository.findHolidaysByUser(user);
    }

    public List<Holiday> getHolidaysByUserID(int userid) {
        User user = userRepository.findUserById(userid);
        return holidayRepository.findHolidaysByUser(user);
    }

    public List<Holiday> getAllHolidays() {
        return (List<Holiday>) holidayRepository.findAll();
    }

    public Holiday saveHoliday(Holiday newHoliday) {
        return holidayRepository.save(newHoliday);
    }

    public Holiday changeHolidayStatus(int holidayid) {
        Holiday holiday = holidayRepository.findHolidayById(holidayid);
        holiday.setApproved(!holiday.getApproved());
        return holidayRepository.save(holiday);
    }

    public List<Holiday> getUserHolidays(String jsonString) throws JSONException {
        JSONObject inputJSON = new JSONObject(jsonString);
        User currentUser = userRepository.findUserById(Integer.parseInt(inputJSON.get("user").toString()));
        List<Holiday> userHolidaysList = holidayRepository.findHolidaysByUser(currentUser);
        List<Holiday> allQueries = new ArrayList<>();

        for (Holiday holiday : userHolidaysList) {
            String[] singleHolidayElements = holiday.getStartdate().split("/");
            if (singleHolidayElements[2].equals(inputJSON.getString("year"))) {
                allQueries.add(holiday);
            }
        }
        return allQueries;
    }
}
