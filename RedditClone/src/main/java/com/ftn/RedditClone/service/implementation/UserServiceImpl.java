package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.RegisterRequest;
import com.ftn.RedditClone.repository.UserRepository;
import com.ftn.RedditClone.repository.VerificationTokenRepository;
import com.ftn.RedditClone.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public User createUser(RegisterRequest request) {


        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if(user.isPresent()){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setAvatar(request.getAvatar());
        newUser.setRegistrationDate(LocalDate.now());
        newUser.setDescription(request.getDescription());
        newUser.setDisplayName(request.getDisplayName());

        userRepository.save(newUser);

        //String token =
        //generateVerificationToken(newUser);

        return newUser;
    }


    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }

}
