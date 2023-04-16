package com.ftn.RedditClone.elasticRepository;

import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PostElasticRepository extends ElasticsearchRepository<PostElastic, Long> {
    PostElastic findByName(String name);
    List<PostElastic> findAllByCommunity(CommunityElastic community);

}
