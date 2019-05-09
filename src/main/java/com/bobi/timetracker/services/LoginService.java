package com.bobi.timetracker.services;

import com.bobi.timetracker.models.User;
import com.bobi.timetracker.models.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
