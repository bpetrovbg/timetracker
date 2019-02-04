package com.bobi.timetracker.services;

import com.bobi.timetracker.models.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class CheckIsAdminService {

    public CheckIsAdminService() {
    }

    public boolean isAdmin(HttpSession session) {
        if(session.getAttribute("currentuser") != null) {
            User currentUser = (User) session.getAttribute("currentuser");
            return currentUser.getUserrole().getName().equals("admin");
        } return false;
    }
}
