package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    void save(CommentDTO commentDto);
    List<CommentDTO> getAllCommentsFromPost(Long postId);
    List<CommentDTO> getAllCommentsFromUser(String userName);
    void removeComment(Long id);
}
