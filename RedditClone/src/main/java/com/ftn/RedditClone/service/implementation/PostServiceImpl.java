package com.ftn.RedditClone.service.implementation;

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
import com.ftn.RedditClone.service.PostService;
import com.ftn.RedditClone.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final UserService userService;

    @Override
    public Post save(PostRequest postRequest) {
        Community community = communityRepository.findByName(postRequest.getCommunityName())
                .orElseThrow(() -> new CommunityNotFoundException(postRequest.getCommunityName()));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        Post post = postMapper.map(postRequest, community, user);
        community.addPost(post);
        return postRepository.save(post);
    }

    @Override
    public Post save(Post post){
        return postRepository.save(post);
    }

    @Override
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Override
    public Post findPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return post;
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Override
    public List<PostResponse> getPostsByCommunityId(Long communityId) {
    //    Community community = communityRepository.findById(communityId)
    //            .orElseThrow(() -> new CommunityNotFoundException(communityId.toString()));
        List<Post> posts = postRepository.findAllByCommunityId(communityId);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Override
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Override
    public void removePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        postRepository.delete(post);
        post.setCommunity(null);
    }


}
