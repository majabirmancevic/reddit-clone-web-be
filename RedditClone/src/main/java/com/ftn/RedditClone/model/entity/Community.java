package com.ftn.RedditClone.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "community")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Community name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private String creationDate;
    
    private boolean isSuspended;

    private String suspendedReason;

    @OneToMany(mappedBy = "community",fetch = EAGER,cascade = CascadeType.ALL)
    private List<Post> posts;

    @ManyToMany(fetch = LAZY)
    private List<Flair> flairs;

    @ManyToOne(fetch = LAZY)
    private User user;

    public void addPost(Post post) {
        posts.add(post);
        post.setCommunity(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setCommunity(null);
    }


    @OneToMany(mappedBy = "community",fetch = LAZY)
    private Set<Moderator> moderators = new HashSet<Moderator>();;

    public void addModerator(Moderator moderator) {
        moderators.add(moderator);
        moderator.setCommunity(this);
    }

    public void removeModerator(Moderator moderator) {
        moderators.remove(moderator);
        moderator.setCommunity(null);
    }




}
