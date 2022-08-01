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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CommunityServiceImpl implements CommunityService {

    CommunityRepository communityRepository;

    CommunityMapper communityMapper;

    UserService userService;

    ModeratorService moderatorService;

    public CommunityServiceImpl (CommunityRepository communityRepository, CommunityMapper communityMapper, UserService userService,ModeratorService moderatorService){
        this.communityMapper = communityMapper;
        this.communityRepository = communityRepository;
        this.userService = userService;
        this.moderatorService = moderatorService;
    }

    public CommunityDto save(CommunityDto communityDto){
        Community community =  communityMapper.mapDtoToSubreddit(communityDto);
        communityDto.setId(community.getId());
        community.setCreationDate(String.valueOf(LocalDate.now()));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        community.setUser(user);

        Moderator moderator = new Moderator();

        moderator.setUser(user);
        moderator.setCommunity(community);

//        user.addModerator(moderator);
//        community.addModerator(moderator);


        communityRepository.save(community);
        //        moderatorService.save(moderator);
        return communityDto;
    }

    @Override
    public Community save(Community community) {
        Community comm =  communityRepository.save(community);
        return comm;
    }

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
        CommunityDto dto = communityMapper.mapSubredditToDto(community);
        dto.setUserId(community.getUser().getId());

        return dto;
    }

    @Override
    public CommunityDto getCommunityByName(String name) {
        Community community = communityRepository.findByName(name);
        return communityMapper.mapSubredditToDto(community);
    }

    @Override
    public Community removeCommunity(Long id, String suspendedReason) {
        Community community = communityRepository.findById(id).orElseThrow(() -> new SpringRedditException("No community found with ID - " + id));
        community.setSuspended(true);
        community.setSuspendedReason(suspendedReason);
        community.setUser(null);
        return communityRepository.save(community);
    }

    @Override
    public Community findCommunity(Long id) {
        return communityRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<CommunityDto> getAllNotDeleted() {
        return communityRepository.findAllBySuspended()
                .stream()
                .map(communityMapper::mapSubredditToDto)
                .collect(toList());
    }

}
