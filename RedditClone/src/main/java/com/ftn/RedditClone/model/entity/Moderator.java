package com.ftn.RedditClone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "moderators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Moderator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "communityId")
    @JsonBackReference
    private Community community ;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId")
    @JsonBackReference
    private  User user;
}
