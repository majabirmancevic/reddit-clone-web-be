package com.ftn.RedditClone.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String text;

    private LocalDate timestamp;

    private boolean isDeleted;

    private Integer reactionCount = 1;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

    @OneToMany(fetch = EAGER)
    private List<Comment> comments;


    public void addComment(Comment comment) {
        comments.add(comment);
    //    comment.setComment(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    //    moderator.setCommunity(null);
    }
}
