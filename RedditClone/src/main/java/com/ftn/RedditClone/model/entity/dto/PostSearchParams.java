package com.ftn.RedditClone.model.entity.dto;

import lombok.Data;

@Data
public class PostSearchParams {

    private String name;
    private String description;
    private Integer minKarma;
    private Integer maxKarma;
    private String operator;
}
