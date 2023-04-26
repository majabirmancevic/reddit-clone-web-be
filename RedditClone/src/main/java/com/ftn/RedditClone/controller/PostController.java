package com.ftn.RedditClone.controller;


import com.ftn.RedditClone.elasticRepository.PostElasticRepository;
import com.ftn.RedditClone.mapper.PostMapper;
import com.ftn.RedditClone.model.entity.Flair;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.dto.*;
import com.ftn.RedditClone.model.entity.elastic.PostElastic;
import com.ftn.RedditClone.repository.FlairRepository;
import com.ftn.RedditClone.repository.PostRepository;
import com.ftn.RedditClone.service.PostService;
import com.ftn.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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

    @Autowired
    PostElasticRepository postElasticRepository;


    @PostMapping
    public ResponseEntity<Void> createPost(@Valid @ModelAttribute PostRequest postRequest) throws IOException {
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

        //PostResponse post = postService.getPost(id);
       // Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
        if (postService.removePost(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        //        if(post != null){
//            postService.removePost(id);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else{
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
    }

// TODO : prebaci logiku u servis ; dodati pdf?

    @PutMapping(value = "edit/{id}")
    public ResponseEntity<PostResponse> updatePost( @RequestBody PostRequest postRequest, @PathVariable Long id){

        Post post = postService.findPost(id);
        PostElastic postElastic = postService.findByName(post.getTitle());

        if (post == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            if(postRequest.getPostName() != "" && postRequest.getText() != "") {
                post.setText(postRequest.getText());
                post.setTitle(postRequest.getPostName());
                postElastic.setDescription(postRequest.getText());
                postElastic.setName(postRequest.getPostName());
            }

            Flair flair = flairRepository.findByName(postRequest.getFlair());
            if(flair != null ){
                post.setFlair(flair);
            }

            if(postRequest.getImagePath() != null){
                post.setImagePath(postRequest.getImagePath());
            }

            post = postRepository.save(post);
            postElasticRepository.save(postElastic);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(postMapper.mapToDto(post));
        }
    }

    @GetMapping("name/{name}")
    public List<PostResponseElastic> findPostByName(@PathVariable String name){
        return postService.findAllByName(name);
    }
    @GetMapping("description")
    public List<PostResponseElastic> findPostByDescription(@RequestBody DescriptionDto dto){
        return postService.findAllByDescription(dto.getText());
    }
    @GetMapping("description/file")
    public List<PostResponseElastic> findPostByDescriptionFromFile(@RequestBody DescriptionDto dto){
        return postService.findAllByDescriptionFromFile(dto.getText());
    }

    @GetMapping("reindex")
    public void reindex(){
        postService.reindex();
    }

    @GetMapping("community/{name}")
    public List<PostResponseElastic> getAllByCommunityName(@PathVariable String name){
       return postService.findByCommunityName(name);
    }

    @GetMapping("karma")
    public List<PostResponseElastic> getByKarmaRange(@RequestParam(name = "from") Integer from, @RequestParam(name = "to") Integer to) {
        return postService.findByKarma(from, to);
    }
    @GetMapping("find")
    public List<PostResponseElastic> getPosts(@RequestBody PostSearchParams params){
        return postService.find(params);
    }


}
