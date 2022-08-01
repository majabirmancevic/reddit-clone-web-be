package com.ftn.RedditClone.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long postId;
    private String communityName;
    private String postName;
    private String text;
    private String imagePath;
    private String flair;
}
