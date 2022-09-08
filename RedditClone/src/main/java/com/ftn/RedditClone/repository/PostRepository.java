package com.ftn.RedditClone.repository;

import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByCommunity(Community community);
    List<Post> findByUser(User user);

    @Query(value = "SELECT p.id,p.creation_date,p.image_path,p.reaction_count,p.text,p.title,p.community_id,p.flair_id,p.user_id FROM post p left join community c on p.community_id = c.id where c.is_suspended = false",nativeQuery = true)
    List<Post> findNotDeleted();

    @Modifying
    @Transactional
    @Query(value = "delete from post where id = ?1",nativeQuery = true)
    void deleteCurrent(Long id);




}
