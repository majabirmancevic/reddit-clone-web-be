package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;

import java.util.List;

public interface CommunityService {

    CommunityDto save(CommunityDto communityDto);
    Community save(Community community);
    List<CommunityDto> getAll();
    CommunityDto getCommunity(Long id);
    void removeCommunity(Long id);
    Community findCommunity(Long id);

}
