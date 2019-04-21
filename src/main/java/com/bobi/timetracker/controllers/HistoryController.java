package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.*;
import com.bobi.timetracker.services.CheckIsAdminService;
import com.bobi.timetracker.services.GetRecordService;
import com.bobi.timetracker.utilities.ExcelView;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HistoryController {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final CheckIsAdminService isAdminService;

    public HistoryController(RecordRepository recordRepository, UserRepository userRepository, CheckIsAdminService isAdminService) {
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
        this.isAdminService = isAdminService;
    }

    @GetMapping(value = "/history")
    public ModelAndView getMainPage(HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            return new ModelAndView("history");
        } else return null;
    }

    @GetMapping(value = "/historyadmin")
    public ModelAndView getMainPageAdmin(HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            User currentUser = (User) session.getAttribute("currentuser");
            if (currentUser.getUserrole().getName().equals("admin")) {
                return new ModelAndView("historyadmin");
            }
            return null;
        } else return null;
    }

    @PostMapping(value = "/history")
    public List<Record> getUserHistoryForMonth(@RequestBody String jsonString) throws JSONException {
        return getUserProjectTimes(jsonString);
    }


    @PostMapping(value = "/historyadmin")
    public List<Record> getUserHistoryForMonthAdmin(@RequestBody String jsonString, HttpSession session) throws JSONException {
        if (isAdminService.isAdmin(session)) {
            return getUserProjectTimes(jsonString);
        }
        return null;
    }

    @GetMapping(value = "/history/exportall")
    public ModelAndView exportAllHistory() {
        List<Record> allUserPRojectTimeList = (List<Record>) recordRepository.findAll();
        return new ModelAndView(new ExcelView(), "userprojecttime", allUserPRojectTimeList);
    }

    @GetMapping(value = "/history/{user}/{month}/{year}")
    public ModelAndView exportSingleUserHistory(@PathVariable("user") int user,
                                                @PathVariable("month") int month,
                                                @PathVariable("year") int year, HttpSession session) throws JSONException {

        JSONObject inputJSON = new JSONObject();
        inputJSON.put("user", user);
        inputJSON.put("month", month);
        inputJSON.put("year", year);
        List<Record> recordList = getUserProjectTimes(inputJSON.toString());
        return new ModelAndView(new ExcelView(), "userprojecttime", recordList);
    }

    @GetMapping(value = "history/{user}/{project}/{day}/{month}/{year}")
    public Record getSingleUserProjectTime(@PathVariable("user") int user, @PathVariable("project") int project,
                                           @PathVariable("day") int day, @PathVariable("month") int month,
                                           @PathVariable("year") int year) throws JSONException {
        JSONObject inputJSON = new JSONObject();
        inputJSON.put("user", user);
        inputJSON.put("project", project);
        inputJSON.put("day", day);
        inputJSON.put("month", month);
        inputJSON.put("year", year);
        return getSingleUserProjectTime(inputJSON.toString());
    }

    private List<Record> getUserProjectTimes(String jsonString) throws JSONException {
        JSONObject inputJSON = new JSONObject(jsonString);
        User currentUser = userRepository.findUserById(Integer.parseInt(inputJSON.get("user").toString()));
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

    private Record getSingleUserProjectTime(String jsonString) throws JSONException {
        JSONObject inputJSON = new JSONObject(jsonString);
        User currentUser = userRepository.findUserById(Integer.parseInt(inputJSON.get("user").toString()));
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
