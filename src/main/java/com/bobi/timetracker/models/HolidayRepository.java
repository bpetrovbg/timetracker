package com.bobi.timetracker.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepository extends CrudRepository<Holiday, Long> {
    List<Holiday> findHolidaysByUser(User user);
    Holiday findHolidayById(Long holidayid);
}
