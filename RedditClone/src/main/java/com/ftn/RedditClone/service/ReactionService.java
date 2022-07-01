package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.ReactionCommentDto;
import com.ftn.RedditClone.model.entity.dto.ReactionDto;

public interface ReactionService {
    void votePost(ReactionDto reactionDto);
    void voteComment(ReactionCommentDto reactionCommentDto);
    int getKarma(Long userId);
}
