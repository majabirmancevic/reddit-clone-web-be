package com.ftn.RedditClone.repository;

import com.ftn.RedditClone.model.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    @Query(value = "SELECT * FROM reddit.community c WHERE c.is_suspended = 0 AND c.name LIKE %?1%", nativeQuery = true)
    public Community findByName(String name);

    @Query(value = "select c from community c join fetch c.posts where c.id = ?1", nativeQuery = true)
    public Community findOneWithPosts(Long id);

    @Query(value = "SELECT * FROM reddit.community c WHERE c.is_suspended = 0 ", nativeQuery = true)
    public List<Community> findAllBySuspended();
}
