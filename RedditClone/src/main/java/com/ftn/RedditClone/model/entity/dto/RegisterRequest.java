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
    private String avatar;
    private String description;
    private String displayName;

    public RegisterRequest(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
    }
}
