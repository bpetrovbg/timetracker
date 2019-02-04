package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserProjectTime;
import com.bobi.timetracker.models.UserProjectTimeRepository;
import com.bobi.timetracker.models.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HistoryController {
    private final UserProjectTimeRepository userProjectTimeRepository;
    private final UserRepository userRepository;

    public HistoryController(UserProjectTimeRepository userProjectTimeRepository, UserRepository userRepository) {
        this.userProjectTimeRepository = userProjectTimeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/history")
    public ModelAndView getMainPage(HttpSession session) {
        if(session.getAttribute("currentuser") != null) {
            return new ModelAndView("history");
        }
        else return null;
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
        JSONObject inputJSON = new JSONObject(jsonString);
        User currentUser = userRepository.findUserById((Integer) inputJSON.get("userid"));
        List<UserProjectTime> userProjectTimeList = userProjectTimeRepository.findByUserid(currentUser);
        List<UserProjectTime> allQueries = new ArrayList<UserProjectTime>();

        for (UserProjectTime userProjectTime : userProjectTimeList) {
            if(userProjectTime.getStarttime().toLocalDateTime().getMonth().getValue() == inputJSON.getInt("month"))
            allQueries.add(userProjectTime);
        }
        return allQueries;
    }
}
