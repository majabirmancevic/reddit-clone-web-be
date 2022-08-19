package com.ftn.RedditClone.model.entity.dto;

import com.ftn.RedditClone.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Long id;

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;

    private String description;
    private String displayName;
    private String avatar;

    public RegisterRequest(User createdUser) {
        this.id = createdUser.getId();
        this.email = createdUser.getEmail();
        this.username = createdUser.getUsername();
        this.password = createdUser.getPassword();
        this.description = createdUser.getDescription();
        this.displayName = createdUser.getDisplayName();
        this.avatar = createdUser.getAvatar();
    }
}
