package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.exceptions.PostNotFoundException;
import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.Reaction;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.ReactionDto;
import com.ftn.RedditClone.repository.PostRepository;
import com.ftn.RedditClone.repository.ReactionRepository;
import com.ftn.RedditClone.service.ReactionService;
import com.ftn.RedditClone.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static com.ftn.RedditClone.model.entity.ReactionType.UPVOTE;

@Service
@AllArgsConstructor
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Transactional
    public void vote(ReactionDto reactionDto) {
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
}
