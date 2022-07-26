package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.RegisterRequest;

import java.util.List;

public interface UserService {

    User createUser(RegisterRequest userDTO);
    User findByUsername(String username);
//    AuthenticationResponse login(LoginRequest loginRequest);
    User save (User user);
    User findById(Long id);
    User update(Long id, RegisterRequest registerRequest);
    List<User> findAll();
    boolean changePassword(Long id, String oldPassword, String newPassword);

}
