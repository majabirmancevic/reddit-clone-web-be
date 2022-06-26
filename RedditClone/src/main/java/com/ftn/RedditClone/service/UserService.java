package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.RegisterRequest;

public interface UserService {

    User createUser(RegisterRequest userDTO);
    User findByUsername(String username);
//    AuthenticationResponse login(LoginRequest loginRequest);
    User save (User user);

}
