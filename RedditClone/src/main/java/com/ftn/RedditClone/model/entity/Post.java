package com.ftn.RedditClone.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private String imagePath;

    private Integer reactionCount = 0;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flairId", referencedColumnName = "id")
    private Flair flair;


}
