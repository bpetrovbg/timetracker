package com.bobi.timetracker.models;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByUsername(String username);
    User findUserById(Integer id);
}
