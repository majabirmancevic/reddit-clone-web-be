package com.ftn.RedditClone.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String postName;
    private String text;
    private String userName;
    private Long userId;
    private String displayName;
    private String communityName;
    private Integer reactionCount;
    private Integer commentCount;
    private String duration;
    private String imagePath;
    private String flair;
    private boolean upVote;
    private boolean downVote;
}
