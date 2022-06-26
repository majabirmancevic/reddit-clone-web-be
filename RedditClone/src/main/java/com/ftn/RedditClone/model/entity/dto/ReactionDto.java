package com.ftn.RedditClone.model.entity.dto;

import com.ftn.RedditClone.model.entity.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactionDto {
    private ReactionType reactionType;
    private Long postId;
}
