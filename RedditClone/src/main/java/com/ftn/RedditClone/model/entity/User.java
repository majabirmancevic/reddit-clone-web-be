package com.ftn.RedditClone.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String avatar;

    @Column(nullable = false)
    private LocalDate registrationDate;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String displayName;
}
