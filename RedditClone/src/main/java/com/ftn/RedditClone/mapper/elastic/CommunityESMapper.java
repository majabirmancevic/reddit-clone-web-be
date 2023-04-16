package com.ftn.RedditClone.mapper.elastic;

import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.model.entity.dto.CommunityResponseElastic;
import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;

import java.util.List;

public class CommunityESMapper {

    public static CommunityElastic mapDtoToCommunity(CommunityDto dto){
        return CommunityElastic.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    public static CommunityResponseElastic mapCommunityToDto(CommunityElastic community){

        return CommunityResponseElastic.builder()
                .id(community.getId())
                .name(community.getName())
                .numOfPosts(mapPosts(community.getPosts()))
                .averageKarma(averageKarma(community.getPosts()))
                .build();
    }

    public static Integer mapPosts(List<PostElastic> numberOfPosts) {
        if (numberOfPosts == null ){
            return 0;
        } else {
        return numberOfPosts.size();
        }
    }

    public static Double averageKarma(List<PostElastic> posts){
        double average = 0.0;
        if(posts == null){
            return average;
        } else {
        int numOfPosts = mapPosts(posts);
        int sumOfPosts = 0;


        for(PostElastic post : posts){
           sumOfPosts = sumOfPosts + post.getReactionCount();
        }

        if(sumOfPosts != 0) {
             average = sumOfPosts / numOfPosts;
        }
        return average;}
    }
}
