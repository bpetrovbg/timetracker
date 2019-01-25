package com.bobi.timetracker.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class UserProjectTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    //@Column(name = "userid")
    private User userid;

    @ManyToOne
    //@Column(name = "projectid")
    private Project projectid;

    @Column(name = "starttime")
    private Timestamp starttime;

    @Column(name = "endtime")
    private Timestamp endtime;

    @Column(name = "overtime")
    private Timestamp overtime;

    @Column(name = "pause")
    private Timestamp pausetime;

    public Project getProjectid() {
        return projectid;
    }

    public void setProjectid(Project projectid) {
        this.projectid = projectid;
    }

    public Timestamp getOvertime() {
        return overtime;
    }

    public void setOvertime(Timestamp overtime) {
        this.overtime = overtime;
    }

    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    public Timestamp getPausetime() {
        return pausetime;
    }

    public void setPausetime(Timestamp pausetime) {
        this.pausetime = pausetime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserid() {
        return userid;
    }

    public void setUserid(User userid) {
        this.userid = userid;
    }

    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }
}
