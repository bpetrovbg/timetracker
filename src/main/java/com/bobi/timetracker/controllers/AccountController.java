package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.User;
import com.bobi.timetracker.services.AccountService;
import com.bobi.timetracker.services.CheckIsAdminService;
import com.bobi.timetracker.utilities.SHA256Helper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@RestController
public class AccountController {
    private final CheckIsAdminService isAdminService;
    private final AccountService accountService;

    public AccountController(CheckIsAdminService isAdminService, AccountService accountService) {
        this.isAdminService = isAdminService;
        this.accountService = accountService;
    }

    @GetMapping("/users/all")
    List<User> getAllUsers() {
        return accountService.getAllUsers();
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
        return accountService.addUser(newUser);
    }

    @PostMapping(value = "/users/createaccount", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    ModelAndView addUserFromWebsite(@RequestParam Map<String, String> body) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        SHA256Helper passHelper = new SHA256Helper();
        User newUser = new User();
        newUser.setUsername(body.get("username"));
        newUser.setPassword(passHelper.inputPassHash(body.get("password")));
        accountService.addUser(newUser);
        return new ModelAndView("redirect:/login");
    }

    @PutMapping(value = "/users/{user}/roles/{role}")
    public ModelAndView updateUserRole(@PathVariable("user") int userid, @PathVariable("role") int roleid, HttpSession session) throws IOException {
        if (isAdminService.isAdmin(session)) {
            accountService.updateUserRole(userid, roleid);
            return new ModelAndView("users");
        } return null;
    }

    @PostMapping(value = "/myaccount/mail", consumes = "application/json")
    public ModelAndView updateUserMail(@RequestBody String newMail, HttpSession session) throws JSONException {
        if (session.getAttribute("currentuser") != null) {
            JSONObject mail = new JSONObject(newMail);
            session.setAttribute("currentuser", accountService.updateUserMail(((User) session.getAttribute("currentuser")).getId(), mail.getString("mail")));
            return new ModelAndView("myaccount");
        } return null;
    }

    @PostMapping(value = "/myaccount/changepassword", consumes = "application/json")
    public ModelAndView changeUserPassword(@RequestBody String newPassword, HttpSession session) throws JSONException, IOException, NoSuchAlgorithmException {
        if (session.getAttribute("currentuser") != null) {
            JSONObject password = new JSONObject(newPassword);
            SHA256Helper helper = new SHA256Helper();
            String hashedPassword = helper.inputPassHash(password.getString("newpassword"));
            session.setAttribute("currentuser", accountService.changeUserPassword(((User) session.getAttribute("currentuser")).getId(), hashedPassword));
            return new ModelAndView("myaccount");
        }
        return null;
    }
}