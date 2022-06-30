package com.ftn.RedditClone.controller;

import com.ftn.RedditClone.model.entity.dto.CommentDTO;
import com.ftn.RedditClone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentDTO commentDto) {
        commentService.save(commentDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/byPost/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsFromPost(postId));
    }

    @GetMapping("/byUser/{userName}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForUser(@PathVariable String userName){
        return ResponseEntity.status(OK)
                .body(commentService.getAllCommentsFromUser(userName));
    }

}
