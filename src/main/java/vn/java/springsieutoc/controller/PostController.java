package vn.java.springsieutoc.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.java.springsieutoc.helper.ApiResponse;
import vn.java.springsieutoc.model.Post;
import vn.java.springsieutoc.model.dto.PostRequestDTO;
import vn.java.springsieutoc.model.dto.PostResponseDTO;
import vn.java.springsieutoc.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponseDTO>>> getAllPosts() {
        return ApiResponse.success(this.postService.getAllPosts(), "Get all posts successful");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> getPostById(@PathVariable Long id){
        return ApiResponse.success(this.postService.getPost(id), "Get post successful");
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDTO>> createPost(@Valid @RequestBody PostRequestDTO post) {
        return ApiResponse.created(this.postService.createPost(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> updatePost(@PathVariable Long id, @RequestBody PostRequestDTO post) {
        return ApiResponse.success(this.postService.updatePost(post, id), "Update post successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable Long id) {
        this.postService.deletePost(id);
        return ApiResponse.success("Delete post successful");
    }
}
