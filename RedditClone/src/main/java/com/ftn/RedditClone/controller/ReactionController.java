package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.model.entity.dto.ReactionCommentDto;
import com.ftn.RedditClone.model.entity.dto.ReactionDto;
import com.ftn.RedditClone.service.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class ReactionController {

    @Autowired
    ReactionService reactionService;

    @PostMapping("post")
    public ResponseEntity<Void> votePost(@RequestBody ReactionDto reactionDto) {
        reactionService.votePost(reactionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("comment")
    public ResponseEntity<Void> voteComment(@RequestBody ReactionCommentDto reactionDto) {
        reactionService.voteComment(reactionDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("voter/{id}")
    public int getKarma(@PathVariable Long id){
        return reactionService.getKarma(id);
    }
}