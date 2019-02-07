package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserProjectTime;
import com.bobi.timetracker.models.UserProjectTimeRepository;
import com.bobi.timetracker.models.UserRepository;
import com.bobi.timetracker.services.CheckIsAdminService;
import com.bobi.timetracker.services.GetUserProjectTimeService;
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
    private final UserProjectTimeRepository userProjectTimeRepository;
    private final UserRepository userRepository;
    private final CheckIsAdminService isAdminService;
    private final GetUserProjectTimeService userProjectTimeService;

    public HistoryController(UserProjectTimeRepository userProjectTimeRepository, UserRepository userRepository, CheckIsAdminService isAdminService, GetUserProjectTimeService userProjectTimeService) {
        this.userProjectTimeRepository = userProjectTimeRepository;
        this.userRepository = userRepository;
        this.isAdminService = isAdminService;
        this.userProjectTimeService = userProjectTimeService;
    }

    @GetMapping(value = "/history")
    public ModelAndView getMainPage(HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            return new ModelAndView("history");
        } else return null;
    }

    @GetMapping(value = "historyadmin")
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
    public List<UserProjectTime> getUserHistoryForMonth(@RequestBody String jsonString) throws JSONException {
        return getUserProjectTimes(jsonString);
    }


    @PostMapping(value = "/historyadmin")
    public List<UserProjectTime> getUserHistoryForMonthAdmin(@RequestBody String jsonString, HttpSession session) throws JSONException {
        if (isAdminService.isAdmin(session)) {
            return getUserProjectTimes(jsonString);
        }
        return null;
    }

    @GetMapping(value = "/history/exportall")
    public ModelAndView exportAllHistory() {
        List<UserProjectTime> allUserPRojectTimeList = (List<UserProjectTime>) userProjectTimeRepository.findAll();
        return new ModelAndView(new ExcelView(), "userprojecttime", allUserPRojectTimeList);
    }

    @GetMapping(value = "/history/{userid}/{month}/{year}")
    public ModelAndView exportSingleUserHistory(@PathVariable("userid") int userid,
                                                @PathVariable("month") int month,
                                                @PathVariable("year") int year, HttpSession session) throws JSONException {

        JSONObject inputJSON = new JSONObject();
        inputJSON.put("userid", userid);
        inputJSON.put("month", month);
        inputJSON.put("year", year);
        List<UserProjectTime> userProjectTimeList = getUserProjectTimes(inputJSON.toString());
        return new ModelAndView(new ExcelView(), "userprojecttime", userProjectTimeList);

    }

    private List<UserProjectTime> getUserProjectTimes(String jsonString) throws JSONException {
        JSONObject inputJSON = new JSONObject(jsonString);
        User currentUser = userRepository.findUserById(Integer.parseInt(inputJSON.get("userid").toString()));
        List<UserProjectTime> userProjectTimeList = userProjectTimeRepository.findByUserid(currentUser);
        List<UserProjectTime> allQueries = new ArrayList<UserProjectTime>();

        for (UserProjectTime userProjectTime : userProjectTimeList) {
            if (userProjectTime.getStarttime().toLocalDateTime().getMonth().getValue() == inputJSON.getInt("month")
                    && userProjectTime.getStarttime().toLocalDateTime().getYear() == inputJSON.getInt("year")) {
                allQueries.add(userProjectTime);
            }
        }
        return allQueries;
    }
}
