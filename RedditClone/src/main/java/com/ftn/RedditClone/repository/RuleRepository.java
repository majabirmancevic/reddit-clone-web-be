package com.ftn.RedditClone.repository;

import com.ftn.RedditClone.model.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule,Long> {
    List<Rule> findAllByCommunityId(Long communityId);
}
