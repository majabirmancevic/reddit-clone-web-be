package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.model.entity.Roles;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.RegisterRequest;
import com.ftn.RedditClone.repository.UserRepository;
import com.ftn.RedditClone.security.TokenUtils;
import com.ftn.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenUtils tokenUtils;


    @Override
    public User createUser(RegisterRequest request) {


        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if(user.isPresent()){
            return null;
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setEmail(request.getEmail());
        newUser.setRole(Roles.USER);
        newUser.setRegistrationDate(LocalDate.now());

        userRepository.save(newUser);

        return newUser;
    }

    @Override
    public User save (User user){
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new SpringRedditException("No user found with ID - " + id));
    }

    @Override
    public User update(Long id, RegisterRequest request) {

        User user = userRepository.findById(id).orElseThrow(() -> new SpringRedditException("No user found with ID - " + id));

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setDescription(request.getDescription());
        user.setDisplayName(request.getDisplayName());

        userRepository.save(user);

        return user;
    }


    @Override
    public User findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isEmpty()) {
            return user.get();
        }
        return null;
    }
/*
    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String token = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .expiresAt(new Date(System.currentTimeMillis() + expiresIn))
                .username(loginRequest.getUsername())
                .build();
    }
*/

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll();
    }

}
