package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Rule;
import com.ftn.RedditClone.model.entity.dto.RuleDto;

import java.util.List;

public interface RuleService {
    RuleDto findOne(Long id);
    Rule findRule(Long id);
    List<Rule> findAllByCommunity(Long communityId);
    Rule save(RuleDto rule);
    Rule update(RuleDto rule, Long id);
    Rule delete(Long id);
}
