package com.ftn.RedditClone.mapper;

import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.PostRequest;
import com.ftn.RedditClone.model.entity.dto.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract  class PostMapper {

    @Mapping(target = "id", source = "postRequest.postId")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "text", source = "postRequest.text")
    @Mapping(target = "title", source = "postRequest.postName")
    @Mapping(target = "community", source = "community")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "reactionCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Community community, User user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "postName", source = "title")
    @Mapping(target = "communityName", source = "community.name")
    @Mapping(target = "userName", source = "user.username")
    //    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", source = "creationDate")
    //    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    //    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    /*  Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreationDate().toEpochDay());
    }

     */
}
