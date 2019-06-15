package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Holiday;
import com.bobi.timetracker.models.User;
import com.bobi.timetracker.services.CheckIsAdminService;
import com.bobi.timetracker.services.HolidayService;
import com.bobi.timetracker.utilities.ExcelViewHolidays;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class HolidayController {
    private final CheckIsAdminService isAdminService;
    private final HolidayService holidayService;

    public HolidayController(CheckIsAdminService isAdminService, HolidayService holidayService) {
        this.isAdminService = isAdminService;
        this.holidayService = holidayService;
    }

    @GetMapping(value = "/holiday")
    public ModelAndView getHolidayPage(HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            ModelAndView modelAndView = new ModelAndView("holiday");
            List<Holiday> holidaysList = holidayService.getHolidaysByUser((User) session.getAttribute("currentuser"));
            modelAndView.addObject("holidaysList", holidaysList);
            return modelAndView;
        } else return null;
    }

    @GetMapping(value = "/holiday/{user}")
    public List<Holiday> getUserHolidays(@PathVariable("user") int userid) {
        return holidayService.getHolidaysByUserID(userid);
    }

    @GetMapping(value = "/holiday/all")
    public List<Holiday> getAllHolidays() {
        return holidayService.getAllHolidays();
    }


    @PostMapping(value = "holiday/add", consumes = "application/json", produces = "application/json")
    public Holiday addUserHoliday(@RequestBody Holiday newHoliday, HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            if (newHoliday.getUser() != null && !newHoliday.getDescription().trim().equals("") && newHoliday.getStartdate() != null && newHoliday.getEnddate() != null) {
                newHoliday.setApproved(false);
                return holidayService.saveHoliday(newHoliday);
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
            return holidayService.getUserHolidays(jsonString);
        }
        return null;
    }

    @PutMapping(value = "holidayadmin/{holidayid}")
    public Holiday changeHolidayStatus(@PathVariable("holidayid") Long holidayid, HttpSession session) {
        if (isAdminService.isAdmin(session)) {
            return holidayService.changeHolidayStatus(holidayid);
        }
        return null;
    }

    @GetMapping(value = "/holiday/exportall")
    public ModelAndView exportAllHistory() {
        return new ModelAndView(new ExcelViewHolidays(), "holidays", holidayService.getAllHolidays());
    }

    @GetMapping(value = "/holiday/{user}/{year}")
    public ModelAndView exportSingleUserHolidays(@PathVariable("user") int user,
                                                 @PathVariable("year") int year, HttpSession session) throws JSONException {

        JSONObject inputJSON = new JSONObject();
        inputJSON.put("user", user);
        inputJSON.put("year", year);
        List<Holiday> holidayList = holidayService.getUserHolidays(inputJSON.toString());
        return new ModelAndView(new ExcelViewHolidays(), "holidays", holidayList);

    }
}
