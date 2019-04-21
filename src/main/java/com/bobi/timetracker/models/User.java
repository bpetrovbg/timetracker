package com.bobi.timetracker.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(min = 1)
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Size(min = 6)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @OneToOne
    private Role role;

    public User() {
    }

    public User(Integer id) {
        this.setId(id);
    }

    public Role getUserrole() {
        return role;
    }

    public void setUserrole(Role role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
