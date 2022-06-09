package com.ftn.RedditClone.model.entity.dto;

import com.ftn.RedditClone.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public RegisterRequest(User createdUser) {
        this.id = createdUser.getId();
        this.username = createdUser.getUsername();
    }

}
