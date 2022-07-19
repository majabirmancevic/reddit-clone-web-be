package com.ftn.RedditClone.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "post")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String text;

    private LocalDate creationDate;

    private String imagePath;

    private Integer reactionCount = 1;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flairId", referencedColumnName = "id")
    private Flair flair;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "communityId")
    private Community community;

}
