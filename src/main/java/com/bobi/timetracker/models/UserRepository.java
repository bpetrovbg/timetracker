package com.bobi.timetracker.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByUsername(String username);
    User findUserById(Integer id);
}
