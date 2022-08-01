package com.ftn.RedditClone.controller;


import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.AuthenticationResponse;
import com.ftn.RedditClone.model.entity.dto.LoginRequest;
import com.ftn.RedditClone.model.entity.dto.PasswordDto;
import com.ftn.RedditClone.model.entity.dto.RegisterRequest;
import com.ftn.RedditClone.security.TokenUtils;
import com.ftn.RedditClone.service.UserService;
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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

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
    public ResponseEntity<RegisterRequest> signup(@RequestBody @Validated RegisterRequest registerRequest)
    {

        User createdUser = userService.createUser(registerRequest);
/*
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
*/
        if(createdUser == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        RegisterRequest userDTO = new RegisterRequest(createdUser);

        return new ResponseEntity<>(userDTO, OK);
    }

/*
    @GetMapping(path="/image/{id}")
    public byte[] getPhoto(@PathVariable("id") Long id) throws IOException {
        User User   =userService.findById(id)
                .orElseThrow(() -> new SpringRedditException("No photo found with ID - " + id));
        return Files.readAllBytes(Paths.get(context.getRealPath("/Photos/")+User.getAvatar()));
    }
*/
    @PutMapping("/users/{id}")
    public ResponseEntity<RegisterRequest> update(@Validated @PathVariable Long id, @RequestBody RegisterRequest registerRequest) {

        User user = userService.update(id, registerRequest);

        if(user == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        RegisterRequest userDTO = new RegisterRequest(user);

        return new ResponseEntity<>(userDTO, OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Validated @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

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

    @GetMapping("/loggedUser/{username}")
    public ResponseEntity<User> user(@PathVariable String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.findByUsername(username));
    }

    @GetMapping("/user/{id}")
    public User user(@PathVariable Long id) {
        return this.userService.findById(id);
    }

    @GetMapping("/allUsers")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @PostMapping("/changePassword/{id}")
    public ResponseEntity changePassword(@Validated @PathVariable Long id, @RequestBody PasswordDto passwordDto) {

        boolean result = userService.changePassword(id, passwordDto.getOldPassword(), passwordDto.getNewPassword());

        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
