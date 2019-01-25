package com.bobi.timetracker.controllers;

import com.bobi.timetracker.models.Role;
import com.bobi.timetracker.models.RoleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping("/roles/all")
    List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/roles/add", consumes = "application/json", produces = "application/json")
    Role addRole(@RequestBody Role newRole) {
        roleRepository.save(newRole);
        return newRole;
    }
}
