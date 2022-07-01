package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.mapper.CommunityMapper;
import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.Moderator;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.repository.CommunityRepository;
import com.ftn.RedditClone.service.CommunityService;
import com.ftn.RedditClone.service.ModeratorService;
import com.ftn.RedditClone.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    CommunityMapper communityMapper;
    @Autowired
    UserService userService;
    @Autowired
    ModeratorService moderatorService;

    @Transactional
    public CommunityDto save(CommunityDto communityDto){
        Community community =  communityMapper.mapDtoToSubreddit(communityDto);
        communityDto.setId(community.getId());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        communityRepository.save(community);
        Moderator moderator = new Moderator();
        user.addModerator(moderator);
        moderator.setCommunity(community);
        community.addModerator(moderator);

        return communityDto;
    }

    @Override
    public Community save(Community community) {
        Community comm =  communityRepository.save(community);
        return comm;
    }

    @Transactional(readOnly = true)
    public List<CommunityDto> getAll() {
        return communityRepository.findAll()
                .stream()
                .map(communityMapper::mapSubredditToDto)
                .collect(toList());
    }

    @Override
    public CommunityDto getCommunity(Long id) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No community found with ID - " + id));
        return communityMapper.mapSubredditToDto(community);
    }

    @Override
    public void removeCommunity(Long id, String suspendedReason) {
        Community community = communityRepository.findById(id).orElseThrow(() -> new SpringRedditException("No community found with ID - " + id));
        community.setSuspended(true);
        community.setSuspendedReason(suspendedReason);
    }

    @Override
    public Community findCommunity(Long id) {
        return communityRepository.findById(id).orElseGet(null);
    }

}
