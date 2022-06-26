package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.dto.ReactionDto;
import org.springframework.web.bind.annotation.RequestBody;

public interface ReactionService {

    void vote(@RequestBody ReactionDto reactionDto);
}
