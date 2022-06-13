package com.ftn.RedditClone.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
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

    private Integer reactionCount = 0;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flairId", referencedColumnName = "id")
    private Flair flair;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "communityId", referencedColumnName = "id")
    private Community community;

}
