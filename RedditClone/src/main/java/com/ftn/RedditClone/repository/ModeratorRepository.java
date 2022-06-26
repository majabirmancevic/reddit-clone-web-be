package com.ftn.RedditClone.repository;

import com.ftn.RedditClone.model.entity.Moderator;
import com.ftn.RedditClone.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeratorRepository extends JpaRepository<Moderator,Long> {
    Moderator findModeratorByUser(User user);
}
