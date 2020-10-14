package com.chat.number.controller;

import com.chat.number.domain.entity.PostEntity;
import com.chat.number.repository.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
public class PostController {

  final PostRepository postRepository;

  public PostController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @GetMapping("/post")
  public List<PostEntity> getAllPost() {
    return postRepository.findAll();
  }


  @GetMapping("/post/{id}")
  public PostEntity getPost(@PathVariable String id) {
    Long postID = Long.parseLong(id);
    PostEntity postEntity = postRepository.findById(postID).orElse(null);
    return postEntity;
  }


  @PostMapping("/post/{id}")
  public PostEntity updatePost(@PathVariable String id, @RequestBody PostEntity newPost) throws Exception {
    Long postID = Long.parseLong(id);
    PostEntity post = postRepository.findById(postID).orElse(null);
    if (post != null) {
      post.setTitle(newPost.getTitle());
      post.setContent(newPost.getContent());
      postRepository.save(post);
      return post;
    } else {
      log.error("id : {}, newPost : {}", id, newPost);
      throw new Exception();
    }
  }


  @PutMapping("/post")
  public PostEntity createPost(@RequestBody PostEntity post) {
    return postRepository.save(post);
  }


  @DeleteMapping("/post/{id}")
  public String deletePost(@PathVariable String id) {
    Long postID = Long.parseLong(id);
    postRepository.deleteById(postID);
    return "Delete Success!";
  }

}