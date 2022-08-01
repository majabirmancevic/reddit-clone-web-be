package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.Rule;
import com.ftn.RedditClone.model.entity.dto.RuleDto;
import com.ftn.RedditClone.repository.RuleRepository;
import com.ftn.RedditClone.service.CommunityService;
import com.ftn.RedditClone.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    RuleRepository ruleRepository;
    @Autowired
    CommunityService communityService;

    @Override
    public RuleDto findOne(Long id) {
        Rule rule = ruleRepository.findById(id).orElseThrow(() -> new SpringRedditException("No rule found with ID - " + id));
        RuleDto dto = new RuleDto();
        dto.setDescription(rule.getDescription());
        dto.setCommunityId(rule.getCommunity().getId());
        return dto;
    }

    @Override
    public Rule findRule(Long id) {
        Rule rule = ruleRepository.findById(id).orElseThrow(() -> new SpringRedditException("No rule found with ID - " + id));
        return rule;
    }

    @Override
    public List<Rule> findAllByCommunity(Long communityId) {
        return ruleRepository.findAllByCommunityId(communityId);
    }

    @Override
    public Rule save(RuleDto ruleDto) {
        Rule rule = new Rule();
        Community community = communityService.findCommunity(ruleDto.getCommunityId());
        rule.setCommunity(community);
        rule.setDescription(ruleDto.getDescription());
        return ruleRepository.save(rule);
    }

    @Override
    public Rule update(RuleDto dto, Long id) {

        Rule r = ruleRepository.findById(id).orElseThrow(() -> new SpringRedditException("No rule found with ID - " + id));

        if(r != null){
            r.setDescription(dto.getDescription());
            ruleRepository.save(r);
        }
        return null;
    }

    @Override
    public Rule delete(Long id) {
        Rule r = ruleRepository.findById(id).orElseThrow(() -> new SpringRedditException("No rule found with ID - " + id));

        if(r != null){
            ruleRepository.delete(r);
        }
        return null;
    }
}
