package com.bobi.timetracker.models;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HolidayRepository extends CrudRepository<Holiday, Integer> {
    List<Holiday> findHolidaysByUserid(User userid);
    Holiday findHolidayById(Integer holidayid);
}
