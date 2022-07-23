package com.ftn.RedditClone.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private Long postId;
    private String timestamp;
    private String text;
    private String userName;
    private Long userId;
    private Long parentId;
    //new

    private boolean upVote;
    private boolean downVote;
}
