package com.ftn.RedditClone.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String creationDate;
    
    private boolean isSuspended;

    private String suspendedReason;

    @OneToMany(fetch = LAZY)
    private List<Post> posts;

    @ManyToMany(fetch = LAZY)
    private List<Flair> flairs;

}
