package com.ftn.RedditClone.service;

import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.dto.PostRequest;
import com.ftn.RedditClone.model.entity.dto.PostResponse;

import java.util.List;

public interface PostService {

    Post save(PostRequest postRequest);
    Post save(Post save);
    PostResponse getPost(Long id);
    Post findPost(Long id);
    List<PostResponse> getAllPosts();
    List<PostResponse> getPostsByCommunity(Long communityId);
    List<PostResponse> getPostsByUsername(String username);
    void removePost(Long id);

}
