package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.model.entity.dto.CommunityResponseElastic;
import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;

import java.io.IOException;
import java.util.List;

public interface CommunityService {

    CommunityDto save(CommunityDto communityDto) throws IOException;
    Community save(Community community);
    List<CommunityDto> getAll();
    CommunityDto getCommunity(Long id);
    CommunityDto getCommunityByName(String name);
    Community removeCommunity(Long id, String suspendedReason);
    Community findCommunity(Long id);
    List<CommunityDto> getAllNotDeleted();
    Community editCommunity(CommunityDto dto, Long id);

    List<CommunityResponseElastic>  findAllByName(String name);
    List<CommunityResponseElastic> findAllByDescription(String description);
    List<CommunityResponseElastic> findAllByDescriptionFromFile(String descriptionFromFile);
}
