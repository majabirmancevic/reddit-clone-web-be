package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;

import java.util.List;

public interface CommunityService {

    CommunityDto save(CommunityDto communityDto);
    Community save(Community community);
    List<CommunityDto> getAll();
    CommunityDto getCommunity(Long id);
    CommunityDto getCommunityByName(String name);
    Community removeCommunity(Long id, String suspendedReason);
    Community findCommunity(Long id);
    List<CommunityDto> getAllNotDeleted();
    Community editCommunity(CommunityDto dto, Long id);
}
