package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.exceptions.PostNotFoundException;
import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.model.entity.Comment;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.Reaction;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.ReactionCommentDto;
import com.ftn.RedditClone.model.entity.dto.ReactionDto;
import com.ftn.RedditClone.repository.CommentRepository;
import com.ftn.RedditClone.repository.PostRepository;
import com.ftn.RedditClone.repository.ReactionRepository;
import com.ftn.RedditClone.service.ReactionService;
import com.ftn.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.ftn.RedditClone.model.entity.ReactionType.UPVOTE;

@Service
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserService userService;

    @Override
    public void votePost(ReactionDto reactionDto) {
        Post post = postRepository.findById(reactionDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + reactionDto.getPostId()));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);
        Optional<Reaction> voteByPostAndUser = reactionRepository.findTopByPostAndUserOrderByIdDesc(post, user);

        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getType()
                        .equals(reactionDto.getReactionType())) {
            throw new SpringRedditException("You have already "
                    + reactionDto.getReactionType() + "'d for this post");
        }
        if (UPVOTE.equals(reactionDto.getReactionType())) {
            post.setReactionCount(post.getReactionCount() + 1);
        } else {
            post.setReactionCount(post.getReactionCount() - 1);
        }
        reactionRepository.save(mapToReaction(reactionDto, post));
        postRepository.save(post);
    }

    @Override
    public void voteComment(ReactionCommentDto reactionCommentDto) {
       Comment comment = commentRepository.findById(reactionCommentDto.getCommentId())
               .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + reactionCommentDto.getCommentId()));
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        Optional<Reaction> voteByCommentAndUser = reactionRepository.findTopByCommentAndUserOrderByIdDesc(comment, user);

        if (voteByCommentAndUser.isPresent() &&
                voteByCommentAndUser.get().getType()
                        .equals(reactionCommentDto.getReactionType())) {
            throw new SpringRedditException("You have already "
                    + reactionCommentDto.getReactionType() + "'d for this post");
        }
        if (UPVOTE.equals(reactionCommentDto.getReactionType())) {
            comment.setReactionCount(comment.getReactionCount() + 1);
        } else {
            comment.setReactionCount(comment.getReactionCount() - 1);
        }

        reactionRepository.save(mapToReactionComment(reactionCommentDto,comment));
        commentRepository.save(comment);
    }

    private Reaction mapToReaction(ReactionDto reactionDto, Post post) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        return Reaction.builder()
                .type(reactionDto.getReactionType())
                .timestamp(LocalDate.now())
                .post(post)
                .user(user)
                .build();
    }

    private Reaction mapToReactionComment(ReactionCommentDto reactionCommentDto, Comment comment) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        return Reaction.builder()
                .type(reactionCommentDto.getReactionType())
                .timestamp(LocalDate.now())
                .comment(comment)
                .user(user)
                .build();
    }
}
