package com.ftn.RedditClone.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

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

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Text is required")
    @Lob
    private String text;

    private LocalDate creationDate;

    @Lob
    private String imagePath;

    private Integer reactionCount = 1;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "userId")
    private User user;


    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "flairId", referencedColumnName = "id")
    private Flair flair;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "communityId")
    @JsonBackReference
    private Community community;

    @OneToMany(fetch = EAGER, mappedBy = "post", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;


}
