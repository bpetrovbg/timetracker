package com.bobi.timetracker.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Project project;

    @Column(name = "starttime")
    private Timestamp starttime;

    @Column(name = "endtime")
    private Timestamp endtime;

    //overtime in minutes => Long
    @Column(name = "overtime")
    private Long overtime;

    @Column(name = "oldpause")
    private Timestamp oldpausetime;

    @Column(name = "newpause")
    private Timestamp newpausetime;

    //pausetime in minutes => Long
    @Column(name = "pause")
    private Long pausetime;

    @Column(name = "total")
    private Long totaltime;

    @Column(name = "comment")
    private String comment;

    @Column(name = "location")
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getTotaltime() {
        return totaltime;
    }

    public void setTotaltime(Long totaltime) {
        this.totaltime = totaltime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Long getOvertime() {
        return overtime;
    }

    public void setOvertime(Long overtime) {
        this.overtime = overtime;
    }

    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    public Long getPausetime() {
        return pausetime;
    }

    public void setPausetime(Long pausetime) {
        this.pausetime = pausetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    public Timestamp getOldpausetime() {
        return oldpausetime;
    }

    public void setOldpausetime(Timestamp oldpausetime) {
        this.oldpausetime = oldpausetime;
    }

    public Timestamp getNewpausetime() {
        return newpausetime;
    }

    public void setNewpausetime(Timestamp newpausetime) {
        this.newpausetime = newpausetime;
    }
}
