package com.ftn.RedditClone.model.entity.dto;

import lombok.Data;

@Data
public class CommunitySearchParams {

    private String name;
    private String description;
    private Integer minNumOfPosts;
    private Integer maxNumOfPosts;
    private String operator;
}
