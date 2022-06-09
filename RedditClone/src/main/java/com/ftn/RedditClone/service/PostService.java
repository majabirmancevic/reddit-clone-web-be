package com.ftn.RedditClone.service;


import com.ftn.RedditClone.exceptions.CommunityNotFoundException;
import com.ftn.RedditClone.exceptions.PostNotFoundException;
import com.ftn.RedditClone.mapper.PostMapper;
import com.ftn.RedditClone.model.entity.Community;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.PostRequest;
import com.ftn.RedditClone.model.entity.dto.PostResponse;
import com.ftn.RedditClone.repository.CommunityRepository;
import com.ftn.RedditClone.repository.PostRepository;
import com.ftn.RedditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CommunityRepository subredditRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Community community = subredditRepository.findByName(postRequest.getCommunitytName())
                .orElseThrow(() -> new CommunityNotFoundException(postRequest.getCommunitytName()));
        postRepository.save(postMapper.map(postRequest, community, userService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Community subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new CommunityNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllByCommunity(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
