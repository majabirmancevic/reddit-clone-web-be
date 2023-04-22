package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.elasticRepository.CommunityElasticRepository;
import com.ftn.RedditClone.elasticRepository.PostElasticRepository;
import com.ftn.RedditClone.exceptions.PostNotFoundException;
import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.model.entity.Comment;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.Reaction;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.ReactionDto;
import com.ftn.RedditClone.model.entity.elastic.CommunityElastic;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import com.ftn.RedditClone.repository.CommentRepository;
import com.ftn.RedditClone.repository.PostRepository;
import com.ftn.RedditClone.repository.ReactionRepository;
import com.ftn.RedditClone.service.ReactionService;
import com.ftn.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.DocumentOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
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

    @Autowired
    PostElasticRepository postElasticRepository;

    @Autowired
    CommunityElasticRepository communityElasticRepository;

    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    ElasticsearchOperations operations;

    @Autowired
    DocumentOperations documentOperations;

    @Override
    public void votePost(ReactionDto reactionDto) {
        Post post = postRepository.findById(reactionDto.getId())
                .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + reactionDto.getId()));

        //new post for elastic db
        PostElastic postElastic = postElasticRepository.findByName(post.getTitle());
      //  int newReactionElastic = postElastic.getReactionCount() ;

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
            postElastic.setReactionCount(postElastic.getReactionCount() + 1);
            //newReactionElastic = newReactionElastic + 1;
        } else {
            post.setReactionCount(post.getReactionCount() - 1);
            postElastic.setReactionCount(postElastic.getReactionCount() - 1);
           // newReactionElastic = newReactionElastic - 1;
        }
        reactionRepository.save(mapToReaction(reactionDto, post));
        postRepository.save(post);

        postElasticRepository.save(postElastic);
        CommunityElastic communityElastic = communityElasticRepository.findByName(postElastic.getCommunityName());
//        if(communityElastic.getPosts() == null){
//         communityElastic.setPosts(postElasticRepository.findByCommunityName(postElastic.getCommunityName()));
//        }
//        if(!communityElastic.getPosts().contains(postElastic) ){
//            communityElastic.getPosts().add(postElastic);
//        }
        //communityElastic.getPosts().add(postElastic);
        //communityElasticRepository.save(communityElastic);

        updateReactionElastic(postElastic,postElastic.getReactionCount());


    }

    //for update in elastic
    public void updateReactionElastic(PostElastic post, int reaction){

        org.springframework.data.elasticsearch.core.document.Document document = org.springframework.data.elasticsearch.core.document.Document
                .create();
        document.put("reactionCount", reaction);
        UpdateQuery updateQuery = UpdateQuery.builder(post.getId())
                .withDocument(document)
                .build();

        operations.update(updateQuery, IndexCoordinates.of("posts"));
        postElasticRepository.save(post);
    }


    @Override
    public void voteComment(ReactionDto reactionCommentDto) {
       Comment comment = commentRepository.findById(reactionCommentDto.getId())
               .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + reactionCommentDto.getId()));
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

    @Override
    public int getKarma(Long userId) {

        User user = userService.findById(userId);

        return reactionRepository.findAllByUser(user).size();
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

    private Reaction mapToReactionComment(ReactionDto reactionDto, Comment comment) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        return Reaction.builder()
                .type(reactionDto.getReactionType())
                .timestamp(LocalDate.now())
                .comment(comment)
                .user(user)
                .build();
    }
}
