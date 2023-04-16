package com.ftn.RedditClone.model.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunityResponseElastic {
    private String id;
    private String name;
    private int numOfPosts;

    private double averageKarma;
}
