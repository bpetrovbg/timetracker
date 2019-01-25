package com.bobi.timetracker.models;

import java.util.Date;

public class Timer {
    private Date starttime;

    Timer(Date starttime) {

    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }
}
