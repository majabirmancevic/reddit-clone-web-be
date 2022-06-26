package com.ftn.RedditClone.mapper;

import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface  CommunityMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(community.getPosts()))")
    CommunityDto mapSubredditToDto(Community community);

    default Integer mapPosts(List<Post> numberOfPosts) {
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    Community mapDtoToSubreddit(CommunityDto subredditDto);

}
