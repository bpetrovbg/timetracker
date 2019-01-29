package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.UserProjectTime;
import com.bobi.timetracker.models.UserProjectTimeRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class TimerController {
    private final UserProjectTimeRepository userProjectTimeRepository;

    public TimerController(UserProjectTimeRepository userProjectTimeRepository) {
        this.userProjectTimeRepository = userProjectTimeRepository;
    }

    @PostMapping(value = "/timer/start", consumes = "application/json")
    public void saveStartTime(@RequestBody UserProjectTime userProjectTime) {
        List<UserProjectTime> userProjectTimeFromDBList = userProjectTimeRepository.findByUseridAndProjectid(userProjectTime.getUserid(), userProjectTime.getProjectid());

        if(userProjectTimeFromDBList.size() > 0) {
            for (UserProjectTime userProjectTimeFromDB:userProjectTimeFromDBList) {
                if(userProjectTimeFromDB.getStarttime() == null) {
                    userProjectTimeRepository.save(userProjectTime);
                }
            }
        } else {
            userProjectTimeRepository.save(userProjectTime);
        }
    }

        /*for (UserProjectTime userProjectTimeFromDB:userProjectTimeFromDBList) {
            if(userProjectTimeFromDB == null) {
                userProjectTimeRepository.save(userProjectTime);
            } else if (!toDate(userProjectTimeFromDB.getStarttime().getTime()).equals(toDate(userProjectTime.getStarttime().getTime()))) {
                userProjectTimeRepository.save(userProjectTime);
            } else {
                //do nothing
            }
        }*/

    @PutMapping(value = "/timer/stop", consumes = "application/json")
    public void saveStopTime(@RequestBody UserProjectTime userProjectTime) {
        //UserProjectTime userProjectTimeFromDB = userProjectTimeRepository.findByUseridAndProjectid(userProjectTime.getUserid(), userProjectTime.getProjectid());
        UserProjectTime userProjectTimeFromDB = userProjectTimeRepository.findByUseridAndProjectidAndStarttime(
                userProjectTime.getUserid(), userProjectTime.getProjectid(), userProjectTime.getStarttime()
        );
        if(userProjectTimeFromDB == null) {
            //not started => do nothing
            //userProjectTimeRepository.save(userProjectTime);
        } else if (userProjectTimeFromDB.getEndtime() == null) {
            userProjectTimeFromDB.setEndtime(userProjectTime.getEndtime());
            userProjectTimeRepository.save(userProjectTimeFromDB);
        } else {
            //do nothing => veche e prikluchil za denq...
        }
    }

    @PutMapping(value = "/timer/newpause", consumes = "application/json")
    public void savePauseTime(@RequestBody UserProjectTime userProjectTime) {
        UserProjectTime userProjectTimeFromDB = userProjectTimeRepository.findByUseridAndProjectidAndStarttime(
                userProjectTime.getUserid(), userProjectTime.getProjectid(), userProjectTime.getStarttime()
        );
        if(userProjectTimeFromDB == null) {
            //not started => do nothing
        } else  {
            userProjectTimeFromDB.setNewpausetime(userProjectTime.getNewpausetime());
            userProjectTimeRepository.save(userProjectTimeFromDB);
        }
    }

    @GetMapping(value = "/timer/all")
    public List<UserProjectTime> getAllTimers() {
        return (List<UserProjectTime>) userProjectTimeRepository.findAll();
    }

    private String toDate(long timestamp) {
        LocalDate date = Instant.ofEpochMilli(timestamp * 1000).atZone(ZoneId.systemDefault()).toLocalDate();
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}