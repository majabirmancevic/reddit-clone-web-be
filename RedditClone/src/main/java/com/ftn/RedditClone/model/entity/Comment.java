package com.ftn.RedditClone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    @NotEmpty
    private String text;

    private LocalDate timestamp;

    private boolean isDeleted;

    private Integer reactionCount = 1;

    @ManyToOne(fetch = EAGER)
    @JsonBackReference
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;



    @OneToMany(fetch = EAGER)
    private List<Comment> comments;


    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setComments(comments);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    //    moderator.setCommunity(null);
    }
}
