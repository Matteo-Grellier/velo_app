package com.formation.velo.service;

import com.formation.velo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    Optional<User> findById(Integer id);
    User save(User user);

    void deleteById(Integer id);

    void delete(User user);
}
