package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.dto.PostRequest;
import com.ftn.RedditClone.model.entity.dto.PostResponse;
import com.ftn.RedditClone.model.entity.dto.PostResponseElastic;

import java.io.IOException;
import java.util.List;

public interface PostService {

    Post save(PostRequest postRequest) throws IOException;
    Post save(Post save);
    PostResponse getPost(Long id);
    Post findPost(Long id);
    List<PostResponse> getAllPosts();
    List<PostResponse> getPostsByCommunity(Long communityId);
    List<PostResponse> getPostsByUsername(String username);
    boolean removePost(Long id);
    void reindex();
    List<PostResponseElastic>  findAllByName(String name);
    List<PostResponseElastic> findAllByDescription(String description);
    List<PostResponseElastic> findAllByDescriptionFromFile(String descriptionFromFile);
    List<PostResponseElastic> findByCommunityName(String name);
}
