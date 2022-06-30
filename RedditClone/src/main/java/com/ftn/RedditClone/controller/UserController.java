package com.ftn.RedditClone.controller;


import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.AuthenticationResponse;
import com.ftn.RedditClone.model.entity.dto.LoginRequest;
import com.ftn.RedditClone.model.entity.dto.RegisterRequest;
import com.ftn.RedditClone.security.TokenUtils;
import com.ftn.RedditClone.service.UserService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")

public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    ServletContext context;

    @PostMapping("/signup")
    public ResponseEntity<RegisterRequest> signup(@RequestBody @Validated RegisterRequest registerRequest,
                                                  @RequestParam("file") MultipartFile file)
    {

        User createdUser = userService.createUser(registerRequest);

        boolean isExit = new File(context.getRealPath("/Photos")).exists();
        if (!isExit)
        {
            new File (context.getRealPath("/Photos/")).mkdir();
            //           System.out.println("mk dir Photos.");
        }

        String filename = file.getOriginalFilename();
        String newFileName = FilenameUtils.getBaseName(filename)+"."+FilenameUtils.getExtension(filename);
        File serverFile = new File (context.getRealPath("/Photos/"+ File.separator+newFileName));
        try
        {
            FileUtils.writeByteArrayToFile(serverFile,file.getBytes());

        }catch(Exception e) {
            e.printStackTrace();
        }
        createdUser.setAvatar(newFileName);

        userService.save(createdUser);

        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        RegisterRequest userDTO = new RegisterRequest(createdUser);

        return new ResponseEntity<>(userDTO, OK);
    }

    @GetMapping(path="/image/{id}")
    public byte[] getPhoto(@PathVariable("id") Long id) throws IOException {
        User User   =userService.findById(id)
                .orElseThrow(() -> new SpringRedditException("No photo found with ID - " + id));;
        return Files.readAllBytes(Paths.get(context.getRealPath("/Photos/")+User.getAvatar()));
    }

    @PutMapping("/users/{id}")
    public void update(@PathVariable long id, @RequestBody User User) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.update(id, User);
        } else {
            userService.save(User);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

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

        return ResponseEntity.ok(new AuthenticationResponse(token, user.getUsername(),new Date(System.currentTimeMillis() + expiresIn)));


    }


}
