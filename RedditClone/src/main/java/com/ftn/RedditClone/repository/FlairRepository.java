package com.ftn.RedditClone.repository;

import com.ftn.RedditClone.model.entity.Flair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlairRepository extends JpaRepository<Flair, Long> {
    Flair findByName(String name);
}
