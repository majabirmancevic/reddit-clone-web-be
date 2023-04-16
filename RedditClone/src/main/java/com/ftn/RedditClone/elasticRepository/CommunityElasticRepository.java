package com.ftn.RedditClone.elasticRepository;

import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunityElasticRepository extends ElasticsearchRepository<CommunityElastic, Long>{
    List<CommunityElastic> findAllByName(String name);
    List<CommunityElastic> findAllByDescription(String description);
    List<CommunityElastic> findAllByDescriptionFromFile(String descriptionFromFile);
}
