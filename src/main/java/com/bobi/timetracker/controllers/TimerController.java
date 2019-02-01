package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.UserProjectTime;
import com.bobi.timetracker.models.UserProjectTimeRepository;
import com.bobi.timetracker.services.GetUserProjectTimeService;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class TimerController {
    private final UserProjectTimeRepository userProjectTimeRepository;
    private final GetUserProjectTimeService getUserProjectTimeService;

    public TimerController(UserProjectTimeRepository userProjectTimeRepository, GetUserProjectTimeService getUserProjectTimeService) {
        this.userProjectTimeRepository = userProjectTimeRepository;
        this.getUserProjectTimeService = getUserProjectTimeService;
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

    @PostMapping(value = "/timer/newstart", consumes = "application/json")
    public UserProjectTime getUserProjectTime(@RequestBody UserProjectTime userProjectTime) {
        return getUserProjectTimeService.getUserByUseridAndProjectidAndStarttime(userProjectTime.getUserid(),
                userProjectTime.getProjectid(), userProjectTime.getStarttime());
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
    public UserProjectTime saveStopTime(@RequestBody UserProjectTime userProjectTime) {
        //UserProjectTime userProjectTimeFromDB = userProjectTimeRepository.findByUseridAndProjectid(userProjectTime.getUserid(), userProjectTime.getProjectid());
        UserProjectTime userProjectTimeFromDB = userProjectTimeRepository.findByUseridAndProjectidAndStarttime(
                userProjectTime.getUserid(), userProjectTime.getProjectid(), userProjectTime.getStarttime()
        );
        if(userProjectTimeFromDB == null) {
            //not started => do nothing
            //userProjectTimeRepository.save(userProjectTime);
        } else if (userProjectTimeFromDB.getEndtime() == null) {
            long totalWorkingtime, overtime;
            userProjectTimeFromDB.setEndtime(userProjectTime.getEndtime());
            totalWorkingtime = compareTwoTimeStamps(userProjectTimeFromDB.getEndtime(), userProjectTimeFromDB.getStarttime());

            if(userProjectTimeFromDB.getPausetime() != null) {
                totalWorkingtime = totalWorkingtime - userProjectTimeFromDB.getPausetime();
            }
            if(totalWorkingtime > 480) {
                overtime = totalWorkingtime - 480;
                userProjectTimeFromDB.setOvertime(overtime);
            } else {
                userProjectTimeFromDB.setOvertime((long) 0);
            }
            userProjectTimeFromDB.setTotaltime(totalWorkingtime);
            userProjectTimeRepository.save(userProjectTimeFromDB);
            return userProjectTimeFromDB;

        } else {
            //do nothing => veche e prikluchil za denq...
        }
        return null;
    }

    @PutMapping(value = "/timer/oldpause", consumes = "application/json")
    public void savePauseTime(@RequestBody UserProjectTime userProjectTime) {
        UserProjectTime userProjectTimeFromDB = userProjectTimeRepository.findByUseridAndProjectidAndStarttime(
                userProjectTime.getUserid(), userProjectTime.getProjectid(), userProjectTime.getStarttime()
        );
        if(userProjectTimeFromDB == null) {
            //not started => do nothing
        } else  {
            userProjectTimeFromDB.setOldpausetime(userProjectTime.getOldpausetime());
            userProjectTimeRepository.save(userProjectTimeFromDB);
        }
    }

    @PutMapping(value = "/timer/newpause", consumes = "application/json")
    public UserProjectTime saveOldPauseTime(@RequestBody UserProjectTime userProjectTime) {
        UserProjectTime userProjectTimeFromDB = getUserProjectTimeService.getUserByUseridAndProjectidAndStarttime(
                userProjectTime.getUserid(), userProjectTime.getProjectid(), userProjectTime.getStarttime()
        );
        if(userProjectTimeFromDB == null) {
            //doesnt exist => do nothing
        } else if (userProjectTimeFromDB.getOldpausetime() != null) {
            userProjectTimeFromDB.setNewpausetime(userProjectTime.getNewpausetime());
            if (userProjectTimeFromDB.getPausetime() == null) {
                long tempPauseTimeInMinutes;
                tempPauseTimeInMinutes = compareTwoTimeStamps(userProjectTimeFromDB.getNewpausetime(), userProjectTimeFromDB.getOldpausetime());
                userProjectTimeFromDB.setPausetime(tempPauseTimeInMinutes);
                userProjectTimeRepository.save(userProjectTimeFromDB);
            } else {
                long tempPauseTimeInMinutes = userProjectTimeFromDB.getPausetime();
                tempPauseTimeInMinutes += compareTwoTimeStamps(userProjectTimeFromDB.getNewpausetime(), userProjectTimeFromDB.getOldpausetime());
                userProjectTimeFromDB.setPausetime(tempPauseTimeInMinutes);
                userProjectTimeRepository.save(userProjectTimeFromDB);
            }
        }
        return userProjectTimeFromDB;
    }

    @GetMapping(value = "/timer/all")
    public List<UserProjectTime> getAllTimers() {
        return (List<UserProjectTime>) userProjectTimeRepository.findAll();
    }

    private String toDate(long timestamp) {
        LocalDate date = Instant.ofEpochMilli(timestamp * 1000).atZone(ZoneId.systemDefault()).toLocalDate();
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime)
    {
        long milliseconds1 = oldTime.getTime();
        long milliseconds2 = currentTime.getTime();

        long diff = milliseconds2 - milliseconds1;
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

        return diffMinutes;
    }
}