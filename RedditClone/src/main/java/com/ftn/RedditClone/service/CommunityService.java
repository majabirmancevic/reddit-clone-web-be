package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.dto.CommunityDto;

import java.util.List;

public interface CommunityService {

    CommunityDto save(CommunityDto communityDto);
    List<CommunityDto> getAll();

}
