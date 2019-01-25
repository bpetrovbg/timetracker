package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserRepository;
import com.bobi.timetracker.utilities.SHA256Helper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController {
    private final UserRepository userRepository;

    public AccountController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/all")
    List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/add", consumes = "application/json", produces = "application/json")
    User addUser(@RequestBody User newUser) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SHA256Helper passHelper = new SHA256Helper();
        newUser.setPassword(passHelper.inputPassHash(newUser.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    @PostMapping(value = "/users/createaccount", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    ModelAndView addUserFromWebsite(@RequestParam Map<String, String> body) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SHA256Helper passHelper = new SHA256Helper();
        User newUser = new User();
        newUser.setUsername(body.get("username"));
        newUser.setPassword(passHelper.inputPassHash(body.get("password")));
        userRepository.save(newUser);
        return new ModelAndView("login");
    }
}
