package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.model.entity.dto.ReactionDto;
import com.ftn.RedditClone.service.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<Void> votePost(@RequestBody ReactionDto reactionDto) {
        reactionService.votePost(reactionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}