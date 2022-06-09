package com.ftn.RedditClone.service;


import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.mapper.SubredditMapper;
import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.repository.CommunityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class CommunityService {

    private final CommunityRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public CommunityDto save(CommunityDto communityDto) {
        Community save = subredditRepository.save(subredditMapper.mapDtoToCommunity(communityDto));
        communityDto.setId(save.getId());
        return communityDto;
    }

    @Transactional(readOnly = true)
    public List<CommunityDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapCommunityToDto)
                .collect(toList());
    }

    public CommunityDto getSubreddit(Long id) {
        Community subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
        return subredditMapper.mapCommunityToDto(subreddit);
    }
}
