package com.ftn.RedditClone.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    private Integer reactionCount = 0;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "flairId", referencedColumnName = "id")
    private Flair flair;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "communityId")
    private Community community;

    @OneToMany(fetch = EAGER, mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

}
