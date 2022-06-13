package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.repository.CommunityRepository;
import com.ftn.RedditClone.service.CommunityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;

    @Transactional
    public CommunityDto save(CommunityDto communityDto){
        Community save =  communityRepository.save(mapCommunityDto(communityDto));
        communityDto.setId(save.getId());
        return communityDto;
    }

    @Transactional(readOnly = true)
    public List<CommunityDto> getAll() {
        return communityRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(toList());
    }

    public CommunityDto mapToDto(Community community) {
        return CommunityDto.builder().name(community.getName())
                .id(community.getId())
                .numberOfPosts(community.getPosts().size())
                .build();
    }


    public  Community mapCommunityDto(CommunityDto communityDto){
        return Community.builder().name(communityDto.getName())
                .description(communityDto.getDescription())
                .build();
    }

}
