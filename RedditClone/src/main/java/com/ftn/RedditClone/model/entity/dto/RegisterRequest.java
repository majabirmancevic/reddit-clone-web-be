package com.ftn.RedditClone.model.entity.dto;

import com.ftn.RedditClone.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Long id;
    private String email;
    private String username;
    private String password;
    private String description;
    private String displayName;

    public RegisterRequest(User createdUser) {
        this.id = createdUser.getId();
        this.email = createdUser.getEmail();
        this.username = createdUser.getUsername();
        this.password = createdUser.getPassword();
        this.description = createdUser.getDescription();
        this.displayName = createdUser.getDisplayName();
    }
}
