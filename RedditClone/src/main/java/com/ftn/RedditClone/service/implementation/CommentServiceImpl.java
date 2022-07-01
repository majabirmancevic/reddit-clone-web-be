package com.ftn.RedditClone.service.implementation;

import com.ftn.RedditClone.exceptions.PostNotFoundException;
import com.ftn.RedditClone.exceptions.SpringRedditException;
import com.ftn.RedditClone.mapper.CommentMapper;
import com.ftn.RedditClone.model.entity.Comment;
import com.ftn.RedditClone.model.entity.Post;
import com.ftn.RedditClone.model.entity.User;
import com.ftn.RedditClone.model.entity.dto.CommentDTO;
import com.ftn.RedditClone.repository.CommentRepository;
import com.ftn.RedditClone.repository.PostRepository;
import com.ftn.RedditClone.repository.UserRepository;
import com.ftn.RedditClone.service.CommentService;
import com.ftn.RedditClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentRepository commentRepository;

    @Override
    public CommentDTO save(CommentDTO commentDto) {
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentDto.getPostId().toString()));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userService.findByUsername(username);

        Comment comment = commentMapper.map(commentDto, post, user);
        comment.setDeleted(false);

        comment.addComment(comment);
        commentRepository.save(comment);

        return commentMapper.mapToDto(comment);
    }

    @Override
    public List<CommentDTO> getAllCommentsFromPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    @Override
    public List<CommentDTO> getAllCommentsFromUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    @Override
    public void removeComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
        //        commentRepository.delete(comment);
        comment.removeComment(comment);
        comment.setDeleted(true);
        comment.setPost(null);
        comment.setUser(null);
    }

    @Override
    public CommentDTO getComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No community found with ID - " + id));
        return commentMapper.mapToDto(comment);
    }

    @Override
    public Comment findComment(Long id) {
        return commentRepository.findById(id).orElseGet(null);
    }
}
