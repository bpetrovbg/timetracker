package com.bobi.timetracker.models;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "startdate", nullable = false)
    private String startdate;

    @Column(name = "enddate", nullable = false)
    private String enddate;

    @Column(name = "approved")
    private Boolean isApproved;

    @ManyToOne
    private User user;

    public Holiday() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }
}
