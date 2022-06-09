package com.ftn.RedditClone.mapper;

import com.ftn.RedditClone.model.entity.*;
import com.ftn.RedditClone.model.entity.dto.PostRequest;
import com.ftn.RedditClone.model.entity.dto.PostResponse;
import com.ftn.RedditClone.repository.CommentRepository;
import com.ftn.RedditClone.repository.ReactionRepository;
import com.ftn.RedditClone.service.UserService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.ftn.RedditClone.model.entity.ReactionType.DOWNVOTE;
import static com.ftn.RedditClone.model.entity.ReactionType.UPVOTE;


@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private UserService userService;



    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "user", source = "user")
    public abstract Post map(PostRequest postRequest, Community community, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration( Post post) {
        return TimeAgo.using(post.getCreationDate().toEpochDay());
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, ReactionType voteType) {
        if (userService.isLoggedIn()) {
            Optional<Reaction> voteForPostByUser =
                    reactionRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            userService.getCurrentUser());
            return voteForPostByUser.filter(reaction -> reaction.getType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

}