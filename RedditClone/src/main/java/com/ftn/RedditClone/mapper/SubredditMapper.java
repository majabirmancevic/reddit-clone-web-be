package com.ftn.RedditClone.mapper;


import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    CommunityDto mapCommunityToDto(Community community);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Community mapDtoToCommunity(CommunityDto CommunityDto);
}
