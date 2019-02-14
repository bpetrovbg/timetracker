package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.*;
import com.bobi.timetracker.services.CheckIsAdminService;
import com.bobi.timetracker.utilities.ExcelView;
import com.bobi.timetracker.utilities.ExcelViewHolidays;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HolidayController {
    private final UserRepository userRepository;
    private final HolidayRepository holidayRepository;
    private final CheckIsAdminService isAdminService;

    public HolidayController(UserRepository userRepository, HolidayRepository holidayRepository, CheckIsAdminService isAdminService) {
        this.userRepository = userRepository;
        this.holidayRepository = holidayRepository;
        this.isAdminService = isAdminService;
    }

    @GetMapping(value = "/holiday")
    public ModelAndView getHolidayPage(HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            ModelAndView modelAndView = new ModelAndView("holiday");
            List<Holiday> holidaysList = holidayRepository.findHolidaysByUserid((User) session.getAttribute("currentuser"));
            modelAndView.addObject("holidaysList", holidaysList);
            return modelAndView;
        } else return null;
    }

    @GetMapping(value = "/holiday/{userid}")
    public List<Holiday> getUserHolidays(@PathVariable("userid") int userid) {
        User user = userRepository.findUserById(userid);
        List<Holiday> holidayList = holidayRepository.findHolidaysByUserid(user);
        return holidayList;
    }

    @GetMapping(value = "/holiday/all")
    public List<Holiday> getAllHolidays() {
        return (List<Holiday>) holidayRepository.findAll();
    }


    @PostMapping(value = "holiday/add", consumes = "application/json", produces = "application/json")
    public Holiday addUserHoliday(@RequestBody Holiday newHoliday, HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            if (newHoliday.getUserid() != null && !newHoliday.getDescription().trim().equals("") && newHoliday.getStartdate() != null && newHoliday.getEnddate() != null) {
                newHoliday.setApproved(false);
                holidayRepository.save(newHoliday);
                return newHoliday;
            } else return null;
        }
        return null;
    }

    @GetMapping(value = "holidayadmin")
    public ModelAndView getAdminHolidayPage(HttpSession session) {
        if (isAdminService.isAdmin(session)) {
            return new ModelAndView("holidayadmin");
        }
        return null;
    }

    @PostMapping(value = "holidayadmin")
    public List<Holiday> getUserHolidays(@RequestBody String jsonString, HttpSession session) throws JSONException {
        if (isAdminService.isAdmin(session)) {
            return getUserHolidays(jsonString);
        }
        return null;
    }

    @PutMapping (value="holidayadmin/{holidayid}")
    public Holiday changeHolidayStatus(@PathVariable("holidayid") int holidayid, HttpSession session) {
        if (isAdminService.isAdmin(session)) {
            Holiday holiday = holidayRepository.findHolidayById(holidayid);
            holiday.setApproved(!holiday.getApproved());
            holidayRepository.save(holiday);
            return holiday;
        }
        return null;
    }

    @GetMapping(value = "/holiday/exportall")
    public ModelAndView exportAllHistory() {
        List<Holiday> allHolidaysList = (List<Holiday>) holidayRepository.findAll();
        return new ModelAndView(new ExcelViewHolidays(), "holidays", allHolidaysList);
    }

    @GetMapping(value = "/holiday/{userid}/{year}")
    public ModelAndView exportSingleUserHolidays(@PathVariable("userid") int userid,
                                                @PathVariable("year") int year, HttpSession session) throws JSONException {

        JSONObject inputJSON = new JSONObject();
        inputJSON.put("userid", userid);
        inputJSON.put("year", year);
        List<Holiday> holidayList = getUserHolidays(inputJSON.toString());
        return new ModelAndView(new ExcelViewHolidays(), "holidays", holidayList);

    }

    private List<Holiday> getUserHolidays(String jsonString) throws JSONException {
        JSONObject inputJSON = new JSONObject(jsonString);
        User currentUser = userRepository.findUserById(Integer.parseInt(inputJSON.get("userid").toString()));
        List<Holiday> userHolidaysList= holidayRepository.findHolidaysByUserid(currentUser);
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
