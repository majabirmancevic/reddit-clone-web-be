package com.ftn.RedditClone.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Column(unique = true)
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private String creationDate;
    
    private boolean isSuspended;

    private String suspendedReason;

    @OneToMany(mappedBy = "community",fetch = EAGER,cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Post> posts;

    @ManyToMany(fetch = LAZY)
    @JoinTable(
            name = "community_flair",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "flair_id"))
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


    @OneToMany(mappedBy = "community",fetch = EAGER,cascade = CascadeType.ALL)
    @JsonManagedReference
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
