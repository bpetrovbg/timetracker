package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.*;
import com.bobi.timetracker.utilities.ExcelView;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HolidayController {
    private final UserRepository userRepository;
    private final HolidayRepository holidayRepository;

    public HolidayController(UserRepository userRepository, HolidayRepository holidayRepository) {
        this.userRepository = userRepository;
        this.holidayRepository = holidayRepository;
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

    @GetMapping(value = "/holiday/all")
    public List<Holiday> getUserHolidays(HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            User user = (User) session.getAttribute("currentuser");
            return holidayRepository.findHolidaysByUserid(user);
        } else return null;
    }

    @GetMapping(value = "/holiday/{userid}")
    public List<Holiday> getUserHolidays(@PathVariable("userid") int userid) {
        User user = userRepository.findUserById(userid);
        List<Holiday> holidayList = holidayRepository.findHolidaysByUserid(user);
        return holidayList;
    }

    /*@GetMapping(value = "/holiday/all")
    public List<Holiday> getAll(HttpSession session) {
            List<Holiday> holidayList = new ArrayList<>();
            holidayList = holidayRepository.findHolidaysByUserid();
            ModelAndView modelAndView = new ModelAndView("holiday");
            modelAndView.addObject("holidayslist", holidayList);
            return holidayList;
    }*/


    @PostMapping(value = "holiday/add", consumes = "application/json", produces = "application/json")
    public Holiday addUserHoliday(@RequestBody Holiday newHoliday, HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            if (newHoliday.getUserid() != null && !newHoliday.getDescription().trim().equals("") && newHoliday.getStartdate() != null && newHoliday.getEnddate() != null) {
                newHoliday.setApproved(false);
                holidayRepository.save(newHoliday);
                return newHoliday;
            } else return null;
        } return null;
    }
}
