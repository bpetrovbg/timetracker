package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Record;
import com.bobi.timetracker.services.RecordService;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class TimerController {
    private final RecordService recordService;

    public TimerController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping(value = "/timer/start", consumes = "application/json")
    public void saveStartTime(@RequestBody Record record) {
        List<Record> recordFromDBList = recordService.getRecordsByUserAndProject(record.getUser(), record.getProject());
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
            recordService.saveRecord(record);
        }
    }

    /*@PostMapping(value = "/timer/newstart", consumes = "application/json")
    public Record getUserProjectTime(@RequestBody Record record) {
        return recordService.getRecordByUserAndprojectAndStarttime(record.getUser(),
                record.getProject(), record.getStarttime());
    }*/

    @PutMapping(value = "/timer/stop", consumes = "application/json")
    public Record saveStopTime(@RequestBody Record record) {
        Record recordFromDB = recordService.getRecordByUserAndprojectAndStarttime(
                record.getUser(), record.getProject(), record.getStarttime()
        );
        if (recordFromDB == null) {
            //not started => do nothing
        } else if (recordFromDB.getEndtime() == null) {
            long totalWorkingtime, overtime;
            recordFromDB.setEndtime(record.getEndtime());
            totalWorkingtime = recordService.compareTwoTimeStamps(recordFromDB.getEndtime(), recordFromDB.getStarttime());

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
            recordService.saveRecord(recordFromDB);
            return recordFromDB;

        } else {
            //do nothing => user workday has ended already...
        }
        return null;
    }

    @PutMapping(value = "/timer/oldpause", consumes = "application/json")
    public void savePauseTime(@RequestBody Record record) {
        Record recordFromDB = recordService.getRecordByUserAndprojectAndStarttime(
                record.getUser(), record.getProject(), record.getStarttime()
        );
        if (recordFromDB == null) {
            //not started => do nothing
        } else {
            recordFromDB.setOldpausetime(record.getOldpausetime());
            recordService.saveRecord(recordFromDB);
        }
    }

    @PutMapping(value = "/timer/newpause", consumes = "application/json")
    public Record saveOldPauseTime(@RequestBody Record record) {
        Record recordFromDB = recordService.getRecordByUserAndprojectAndStarttime(
                record.getUser(), record.getProject(), record.getStarttime()
        );
        if (recordFromDB == null) {
            //doesnt exist => do nothing
        } else if (recordFromDB.getOldpausetime() != null) {
            recordFromDB.setNewpausetime(record.getNewpausetime());
            if (recordFromDB.getPausetime() == null) {
                long tempPauseTimeInMinutes;
                tempPauseTimeInMinutes = recordService.compareTwoTimeStamps(recordFromDB.getNewpausetime(), recordFromDB.getOldpausetime());
                recordFromDB.setPausetime(tempPauseTimeInMinutes);
                recordService.saveRecord(recordFromDB);
            } else {
                long tempPauseTimeInMinutes = recordFromDB.getPausetime();
                tempPauseTimeInMinutes += recordService.compareTwoTimeStamps(recordFromDB.getNewpausetime(), recordFromDB.getOldpausetime());
                recordFromDB.setPausetime(tempPauseTimeInMinutes);
                recordService.saveRecord(recordFromDB);
            }
        }
        return recordFromDB;
    }

    @GetMapping(value = "/timer/all")
    public List<Record> getAllRecords() {
        return recordService.getAllRecords();
    }
}