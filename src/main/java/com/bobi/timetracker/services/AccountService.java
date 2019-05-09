package com.bobi.timetracker.services;

import com.bobi.timetracker.models.Role;
import com.bobi.timetracker.models.RoleRepository;
import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AccountService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public void updateUserRole(int userid, int roleid) {
        User user = userRepository.findUserById(userid);
        Role role = roleRepository.findRoleById(roleid);
        user.setUserrole(role);
        userRepository.save(user);
    }

    public User updateUserMail(int userid, String mail) {
        User user = userRepository.findUserById(userid);
        user.setEmail(mail);
        return userRepository.save(user);
    }

    public User changeUserPassword(int userid, String newPassword) {
        User user = userRepository.findUserById(userid);
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
}
