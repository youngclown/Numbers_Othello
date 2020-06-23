package com.chat.number.controller;

import com.chat.number.domain.entity.PostEntity;
import com.chat.number.repository.PostRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PostController {

  final PostRepository postRepository;

  public PostController(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @GetMapping("/post")
  public List<PostEntity> getAllPost(){
    return postRepository.findAll();
  }


  @GetMapping("/post/{id}")
  public PostEntity getPost(@PathVariable String id){
    Long postID = Long.parseLong(id);

    Optional<PostEntity> postEntity = postRepository.findById(postID);

    return postEntity.get();
  }


  @PostMapping("/post/{id}")
  public PostEntity updatePost(@PathVariable String id, @RequestBody PostEntity newPost){
    Long postID = Long.parseLong(id);

    Optional<PostEntity> post = postRepository.findById(postID);

    post.get().setTitle(newPost.getTitle());
    post.get().setContent(newPost.getContent());

    postRepository.save(post.get());

    return post.get();
  }


  @PutMapping("/post")
  public PostEntity createPost(@RequestBody PostEntity post){
    return postRepository.save(post);
  }


  @DeleteMapping("/post/{id}")
  public String deletePost(@PathVariable String id){
    Long postID = Long.parseLong(id);
    postRepository.deleteById(postID);
    return "Delete Success!";
  }

}