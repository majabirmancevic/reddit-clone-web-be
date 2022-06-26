package com.ftn.RedditClone.repository;

import com.ftn.RedditClone.model.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    Optional<Community> findByName(String subredditName);

    @Query("select c from Community c join fetch c.posts where c.id =?1")
    public Community findOneWithPosts(Long communityId);
}
