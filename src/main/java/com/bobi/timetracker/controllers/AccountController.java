package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Role;
import com.bobi.timetracker.models.RoleRepository;
import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserRepository;
import com.bobi.timetracker.services.CheckIsAdminService;
import com.bobi.timetracker.utilities.SHA256Helper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController {
    private final UserRepository userRepository;
    private final CheckIsAdminService isAdminService;
    private final RoleRepository roleRepository;

    public AccountController(UserRepository userRepository, CheckIsAdminService isAdminService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.isAdminService = isAdminService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users/all")
    List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("/myaccount")
    public ModelAndView getMyAccountPage(HttpSession session) {
        if (session.getAttribute("currentuser") != null) {
            if (isAdminService.isAdmin(session)) {
                return new ModelAndView("myaccountadmin");
            } else {
                return new ModelAndView("myaccount");
            }
        }
        return null;
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

    @PutMapping(value = "/users/{userid}/roles/{roleid}")
    public ModelAndView updateUserRole(@PathVariable("userid") int userid, @PathVariable("roleid") int roleid, HttpSession session) {
        if (isAdminService.isAdmin(session)) {
            User user = userRepository.findUserById(userid);
            Role role = roleRepository.findRoleById(roleid);
            user.setUserrole(role);
            userRepository.save(user);
            return new ModelAndView("users");
        } else {
            return null;
        }
    }

    @PostMapping(value = "/myaccount/mail", consumes = "application/json")
    public ModelAndView updateUserMail(@RequestBody String newMail, HttpSession session) throws JSONException {
        if (session.getAttribute("currentuser") != null) {
            JSONObject jsonObject = new JSONObject(newMail);
            User user = userRepository.findUserById(((User) session.getAttribute("currentuser")).getId());
            user.setEmail(jsonObject.getString("mail"));
            userRepository.save(user);
            session.setAttribute("currentuser", user);
            return new ModelAndView("myaccount");
        }
        return null;
    }

    @PostMapping(value = "/myaccount/changepassword", consumes = "application/json")
    public ModelAndView changeUserPassword(@RequestBody String newPassword, HttpSession session) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (session.getAttribute("currentuser") != null) {
            JSONObject jsonObject = new JSONObject(newPassword);
            User user = userRepository.findUserById(((User) session.getAttribute("currentuser")).getId());
            SHA256Helper helper = new SHA256Helper();
            user.setPassword(helper.inputPassHash(jsonObject.getString("newpassword")));
            userRepository.save(user);
            session.setAttribute("currentuser", user);
            return new ModelAndView("myaccount");
        }
        return null;
    }
}