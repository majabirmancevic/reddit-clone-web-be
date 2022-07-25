package com.ftn.RedditClone.mapper;

import com.ftn.RedditClone.model.entity.*;
import com.ftn.RedditClone.model.entity.dto.CommentDTO;
import com.ftn.RedditClone.repository.CommentRepository;
import com.ftn.RedditClone.repository.ReactionRepository;
import com.ftn.RedditClone.security.TokenUtils;
import com.ftn.RedditClone.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static com.ftn.RedditClone.model.entity.ReactionType.DOWNVOTE;
import static com.ftn.RedditClone.model.entity.ReactionType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract  class CommentMapper {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TokenUtils tokenUtils;
    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    UserService userService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentDto.text")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "reactionCount", constant = "1")
    @Mapping(target = "parentId", source = "commentDto.parentId")
    public abstract Comment map(CommentDTO commentDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "upVote", expression = "java(isCommentUpVoted(comment))")
    @Mapping(target = "downVote", expression = "java(isCommentDownVoted(comment))")
    @Mapping(target = "userId", expression = "java(comment.getUser().getId())")
    @Mapping(target = "reactionCount", source = "reactionCount")
    @Mapping(target = "parentId", source = "parentId")
    public abstract CommentDTO mapToDto(Comment comment);



    private boolean checkVoteType(Comment comment, ReactionType reactionType) {
        if (tokenUtils.isLoggedIn()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = ((UserDetails) principal).getUsername();
            User user = userService.findByUsername(username);

            Optional<Reaction> reactionForCommentByUser =
                    reactionRepository.findTopByCommentAndUserOrderByIdDesc(comment, user);
            return reactionForCommentByUser.filter(reaction -> reaction.getType().equals(reactionType))
                    .isPresent();
        }
        return false;
    }

    boolean isCommentUpVoted(Comment comment) {
        return checkVoteType(comment, UPVOTE);
    }

    boolean isCommentDownVoted(Comment comment) {
        return checkVoteType(comment, DOWNVOTE);
    }
}