package com.ftn.RedditClone.mapper.elastic;

import com.ftn.RedditClone.elasticRepository.CommunityElasticRepository;
import com.ftn.RedditClone.model.entity.dto.PostRequest;
import com.ftn.RedditClone.model.entity.dto.PostResponseElastic;
import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public class PostESMapper {

    @Autowired
    private static CommunityElasticRepository communityElasticRepository;

    public static PostElastic mapDtoToPost (PostRequest request){
        return PostElastic.builder()
                .name(request.getPostName())
                .description(request.getText())
                .communityName(request.getCommunityName())
                .reactionCount(1)
                .build();
    }


    public static PostResponseElastic postToResponse(PostElastic post){
        return PostResponseElastic.builder()
                .id(post.getId())
                .name(post.getName())
                .description(post.getDescription())
                .build();
    }

    public static CommunityElastic getCommunity(String req){
        List<CommunityElastic> communities = communityElasticRepository.findAllByName(req);
        return communities.get(0);
    }

    public static List<PostResponseElastic> mapDtos(SearchHits<PostElastic> searchHits) {
        return searchHits
                .map(post -> postToResponse(post.getContent()))
                .toList();
    }
}
