package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserRepository;
import com.bobi.timetracker.utilities.SHA256Helper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class LoginController {
    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        User userToLogin = userRepository.findUserByUsername(body.get("username"));
        if(userToLogin != null) {
            if(helper.inputPassHash(body.get("password")).equals(userToLogin.getPassword())) {
                session.setAttribute("currentuser", userToLogin);
                return new ModelAndView(new RedirectView("/main", true));
            }
        } else {
            return null;
        }
        return null;
    }
}
