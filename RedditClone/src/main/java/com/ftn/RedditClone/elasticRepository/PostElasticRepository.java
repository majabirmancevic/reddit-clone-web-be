package com.ftn.RedditClone.elasticRepository;

import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PostElasticRepository extends ElasticsearchRepository<PostElastic, String> {
    List<PostElastic> findAllByName(String name);
    PostElastic findByName(String name);

    List<PostElastic> findAllByCommunityName(String communityName);

    List<PostElastic> findAllByDescription(String description);
    List<PostElastic> findAllByDescriptionFromFile(String descriptionFromFile);


}
