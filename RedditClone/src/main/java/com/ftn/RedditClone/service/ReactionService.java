package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.dto.ReactionDto;

public interface ReactionService {
    void votePost(ReactionDto reactionDto);
    void voteComment(ReactionDto reactionDto);
    int getKarma(Long userId);
}
