package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.*;
import com.bobi.timetracker.services.CheckIsAdminService;
import com.bobi.timetracker.services.HistoryService;
import com.bobi.timetracker.utilities.ExcelView;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class HistoryController {
    private final CheckIsAdminService isAdminService;
    private final HistoryService historyService;

    public HistoryController(CheckIsAdminService isAdminService, HistoryService historyService) {
        this.isAdminService = isAdminService;
        this.historyService = historyService;
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
        return historyService.getUserRecords(jsonString);
    }


    @PostMapping(value = "/historyadmin")
    public List<Record> getUserHistoryForMonthAdmin(@RequestBody String jsonString, HttpSession session) throws JSONException {
        if (isAdminService.isAdmin(session)) {
            return historyService.getUserRecords(jsonString);
        }
        return null;
    }

    @GetMapping(value = "/history/exportall")
    public ModelAndView exportAllHistory() {
        return new ModelAndView(new ExcelView(), "userprojecttime", historyService.getAllRecords());
    }

    @GetMapping(value = "/history/{user}/{month}/{year}")
    public ModelAndView exportSingleUserHistory(@PathVariable("user") int user,
                                                @PathVariable("month") int month,
                                                @PathVariable("year") int year, HttpSession session) throws JSONException {

        JSONObject inputJSON = new JSONObject();
        inputJSON.put("user", user);
        inputJSON.put("month", month);
        inputJSON.put("year", year);
        List<Record> recordList = historyService.getUserRecords(inputJSON.toString());
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
        return historyService.getSingleRecord(inputJSON.toString());
    }
}
