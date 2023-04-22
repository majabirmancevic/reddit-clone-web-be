package com.ftn.RedditClone.mapper.elastic;

import com.ftn.RedditClone.elasticRepository.PostElasticRepository;
import com.ftn.RedditClone.model.entity.dto.CommunityDto;
import com.ftn.RedditClone.model.entity.dto.CommunityResponseElastic;
import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public class CommunityESMapper {

    @Autowired
    static
    PostElasticRepository postElasticRepository;

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
                .numOfPosts(community.getPosts().size())
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
        //List<PostElastic> posts = postElasticRepository.findAllByCommunityName(communityName);
        double average = 0.0;
        if(posts == null){
            return average;
        } else {
        int numOfPosts = posts.size();
        int sumOfPosts = 0;


        for(PostElastic post : posts){
           sumOfPosts = sumOfPosts + post.getReactionCount();
        }

        if(sumOfPosts != 0) {
             average = (double) sumOfPosts / numOfPosts;
             Math.round(average);
        }
        return round(average,1);
        }
    }
    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
    public static List<CommunityResponseElastic> mapDtos(SearchHits<CommunityElastic> searchHits) {
//        List<CommunityResponseElastic> response = new ArrayList<>();
        return searchHits
                .map(community -> mapCommunityToDto(community.getContent()))
                .toList();
    }
}
