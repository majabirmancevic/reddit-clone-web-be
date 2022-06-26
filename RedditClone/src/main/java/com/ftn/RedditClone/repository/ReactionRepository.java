package com.ftn.RedditClone.repository;

import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.Reaction;
import com.ftn.RedditClone.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);
}
