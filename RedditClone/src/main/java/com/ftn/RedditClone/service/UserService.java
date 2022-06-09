package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.RegisterRequest;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User createUser(RegisterRequest userDTO);

    List<User> findAll();

    boolean isLoggedIn();

    User getCurrentUser();

}
