package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Comment;
import com.ftn.RedditClone.model.entity.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO save(CommentDTO commentDto);
    List<CommentDTO> getAllCommentsFromPost(Long postId);
    List<CommentDTO> getAllCommentsFromUser(String userName);
    void removeComment(Long id);
    CommentDTO getComment(Long id);
    Comment findComment(Long id);
}
