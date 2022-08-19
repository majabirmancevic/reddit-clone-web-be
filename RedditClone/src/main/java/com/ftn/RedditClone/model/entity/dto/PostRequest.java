package com.ftn.RedditClone.model.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private Long postId;

 //   @NotBlank(message = "Community name is required")
    private String communityName;

    @NotBlank(message = "Post name is required")
    private String postName;

    @NotBlank(message = "Description is required")
    private String text;

    private String imagePath;
    private String flair;
}
