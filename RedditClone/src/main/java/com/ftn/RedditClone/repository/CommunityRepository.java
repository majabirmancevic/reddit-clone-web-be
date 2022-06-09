package com.ftn.RedditClone.repository;

import com.ftn.RedditClone.model.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    Optional<Community> findByName(String communityName);
}
