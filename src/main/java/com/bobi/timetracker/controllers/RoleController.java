package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Role;
import com.bobi.timetracker.services.RoleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles/all")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/roles/add", consumes = "application/json", produces = "application/json")
    public Role addRole(@RequestBody Role newRole) {
        return roleService.addRole(newRole);
    }
}
