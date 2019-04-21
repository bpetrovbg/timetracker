package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Record;
import com.bobi.timetracker.models.RecordRepository;
import com.bobi.timetracker.services.GetRecordService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class TimerController {
    private final RecordRepository recordRepository;
    private final GetRecordService getRecordService;

    public TimerController(RecordRepository recordRepository, GetRecordService getRecordService) {
        this.recordRepository = recordRepository;
        this.getRecordService = getRecordService;
    }

    @PostMapping(value = "/timer/start", consumes = "application/json")
    public void saveStartTime(@RequestBody Record record) {
        List<Record> recordFromDBList = recordRepository.findByUserAndProject(record.getUser(), record.getProject());
        boolean queryExists = false;

        if (recordFromDBList.size() > 0) {
            LocalDateTime currentDate = new Timestamp(System.currentTimeMillis()).toLocalDateTime();
            for (Record recordFromDB : recordFromDBList) {
                if (recordFromDB.getStarttime().toLocalDateTime().getDayOfMonth() == currentDate.getDayOfMonth()
                        && recordFromDB.getStarttime().toLocalDateTime().getMonthValue() == currentDate.getMonthValue()
                        && recordFromDB.getStarttime().toLocalDateTime().getYear() == currentDate.getYear()) {
                    queryExists = true;
                }
            }
        }
        if (!queryExists) {
            recordRepository.save(record);
        }
    }

    @PostMapping(value = "/timer/newstart", consumes = "application/json")
    public Record getUserProjectTime(@RequestBody Record record) {
        return getRecordService.getUserByuserAndprojectAndStarttime(record.getUser(),
                record.getProject(), record.getStarttime());
    }

        /*for (Record userProjectTimeFromDB:userProjectTimeFromDBList) {
            if(userProjectTimeFromDB == null) {
                recordRepository.save(userProjectTime);
            } else if (!toDate(userProjectTimeFromDB.getStarttime().getTime()).equals(toDate(userProjectTime.getStarttime().getTime()))) {
                recordRepository.save(userProjectTime);
            } else {
                //do nothing
            }
        }*/

    @PutMapping(value = "/timer/stop", consumes = "application/json")
    public Record saveStopTime(@RequestBody Record record) {
        //Record recordFromDB = recordRepository.findByuserAndproject(record.getuser(), record.getproject());
        Record recordFromDB = recordRepository.findByUserAndProjectAndStarttime(
                record.getUser(), record.getProject(), record.getStarttime()
        );
        if (recordFromDB == null) {
            //not started => do nothing
            //recordRepository.save(record);
        } else if (recordFromDB.getEndtime() == null) {
            long totalWorkingtime, overtime;
            recordFromDB.setEndtime(record.getEndtime());
            totalWorkingtime = compareTwoTimeStamps(recordFromDB.getEndtime(), recordFromDB.getStarttime());

            if (recordFromDB.getPausetime() != null) {
                totalWorkingtime = totalWorkingtime - recordFromDB.getPausetime();
            }
            if (totalWorkingtime > 480) {
                overtime = totalWorkingtime - 480;
                recordFromDB.setOvertime(overtime);
            } else {
                recordFromDB.setOvertime((long) 0);
            }
            recordFromDB.setTotaltime(totalWorkingtime);
            recordRepository.save(recordFromDB);
            return recordFromDB;

        } else {
            //do nothing => veche e prikluchil za denq...
        }
        return null;
    }

    @PutMapping(value = "/timer/oldpause", consumes = "application/json")
    public void savePauseTime(@RequestBody Record record) {
        Record recordFromDB = recordRepository.findByUserAndProjectAndStarttime(
                record.getUser(), record.getProject(), record.getStarttime()
        );
        if (recordFromDB == null) {
            //not started => do nothing
        } else {
            recordFromDB.setOldpausetime(record.getOldpausetime());
            recordRepository.save(recordFromDB);
        }
    }

    @PutMapping(value = "/timer/newpause", consumes = "application/json")
    public Record saveOldPauseTime(@RequestBody Record record) {
        Record recordFromDB = getRecordService.getUserByuserAndprojectAndStarttime(
                record.getUser(), record.getProject(), record.getStarttime()
        );
        if (recordFromDB == null) {
            //doesnt exist => do nothing
        } else if (recordFromDB.getOldpausetime() != null) {
            recordFromDB.setNewpausetime(record.getNewpausetime());
            if (recordFromDB.getPausetime() == null) {
                long tempPauseTimeInMinutes;
                tempPauseTimeInMinutes = compareTwoTimeStamps(recordFromDB.getNewpausetime(), recordFromDB.getOldpausetime());
                recordFromDB.setPausetime(tempPauseTimeInMinutes);
                recordRepository.save(recordFromDB);
            } else {
                long tempPauseTimeInMinutes = recordFromDB.getPausetime();
                tempPauseTimeInMinutes += compareTwoTimeStamps(recordFromDB.getNewpausetime(), recordFromDB.getOldpausetime());
                recordFromDB.setPausetime(tempPauseTimeInMinutes);
                recordRepository.save(recordFromDB);
            }
        }
        return recordFromDB;
    }

    @GetMapping(value = "/timer/all")
    public List<Record> getAllTimers() {
        return (List<Record>) recordRepository.findAll();
    }

    private String toDate(long timestamp) {
        LocalDate date = Instant.ofEpochMilli(timestamp * 1000).atZone(ZoneId.systemDefault()).toLocalDate();
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static long compareTwoTimeStamps(java.sql.Timestamp currentTime, java.sql.Timestamp oldTime) {
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