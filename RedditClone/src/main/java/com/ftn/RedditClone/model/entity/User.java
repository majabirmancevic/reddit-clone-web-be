package com.ftn.RedditClone.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotEmpty(message = "Email is required")
    private String email;

    private String avatar;

    private LocalDate registrationDate;

    private String description;

    private String displayName;

    @Enumerated(EnumType.STRING)
    private Roles role;



    @OneToMany(mappedBy = "user",fetch = EAGER,cascade = CascadeType.ALL)
    private Set<Moderator> moderators = new HashSet<>();


    public void addModerator(Moderator moderator) {
        moderators.add(moderator);
        moderator.setUser(this);
    }

    public void removeModerator(Moderator moderator) {
        moderators.remove(moderator);
        moderator.setUser(null);
    }




}
