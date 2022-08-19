package com.ftn.RedditClone.controller;


import com.ftn.RedditClone.mapper.PostMapper;
import com.ftn.RedditClone.model.entity.Flair;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.dto.PostRequest;
import com.ftn.RedditClone.model.entity.dto.PostResponse;
import com.ftn.RedditClone.repository.FlairRepository;
import com.ftn.RedditClone.repository.PostRepository;
import com.ftn.RedditClone.service.PostService;
import com.ftn.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts/")
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserService userService;
    @Autowired
    FlairRepository flairRepository;
    @Autowired
    PostRepository postRepository;

    @PostMapping
    public ResponseEntity<Void> createPost(@Valid @RequestBody PostRequest postRequest) {
        postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @GetMapping("{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }

    @GetMapping("byCommunity/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id) {
        return status(HttpStatus.OK).body(postService.getPostsByCommunity(id));
    }

    @GetMapping("byUser/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }

    @GetMapping(value = "notSuspended")
    public ResponseEntity<List<PostResponse>> notSuspended(){
        return status(HttpStatus.OK)
                .body(postRepository.findNotDeleted()
                        .stream()
                        .map(postMapper::mapToDto)
                        .collect(toList()));
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> removePost(@PathVariable Long id){

        PostResponse post = postService.getPost(id);

        if(post != null){
            postService.removePost(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping(value = "edit/{id}")
    public ResponseEntity<PostResponse> updatePost( @RequestBody PostRequest postRequest, @PathVariable Long id){

        Post post = postService.findPost(id);

        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            if(postRequest.getPostName() != "" || postRequest.getText() != "") {
                post.setText(postRequest.getText());
                post.setTitle(postRequest.getPostName());
            }

            Flair flair = flairRepository.findByName(postRequest.getFlair());
            if(flair != null ){
                post.setFlair(flair);
            }

            if(postRequest.getImagePath() != null){
                post.setImagePath(postRequest.getImagePath());
            }

            post = postService.save(post);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(postMapper.mapToDto(post));
        }
    }




}
