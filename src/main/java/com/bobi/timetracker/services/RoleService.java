package com.bobi.timetracker.services;

import com.bobi.timetracker.models.Role;
import com.bobi.timetracker.models.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }

    public Role addRole(Role role) {
        return roleRepository.save(role);
    }
}
