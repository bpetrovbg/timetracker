package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.User;
import com.bobi.timetracker.services.LoginService;
import com.bobi.timetracker.utilities.SHA256Helper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    ModelAndView getLoginPage() {
        return new ModelAndView("login");
    }

    @GetMapping("/createaccount")
    ModelAndView getCreateAccountPage() {
        return new ModelAndView("createaccount");
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView loginAndReturnUser(@RequestParam Map<String, String> body, HttpSession session) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SHA256Helper helper = new SHA256Helper();
        User userToLogin = loginService.getUserByUsername(body.get("username"));
        if (userToLogin != null) {
            if (helper.inputPassHash(body.get("password")).equals(userToLogin.getPassword())) {
                if (userToLogin.getUserrole() != null && userToLogin.getUserrole().getName().equals("admin")) {
                    session.setAttribute("currentuser", userToLogin);
                    return new ModelAndView(new RedirectView("/admin", true));
                } else {
                    session.setAttribute("currentuser", userToLogin);
                    return new ModelAndView(new RedirectView("/main", true));
                }
            }
        } else {
            return null;
        }
        return null;
    }
}
