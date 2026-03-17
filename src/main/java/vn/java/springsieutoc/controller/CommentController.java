package vn.java.springsieutoc.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.java.springsieutoc.helper.ApiResponse;
import vn.java.springsieutoc.model.dto.CommentRequestDTO;
import vn.java.springsieutoc.model.dto.CommentResponseDTO;
import vn.java.springsieutoc.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponseDTO>>> getAllComment(@RequestParam(required = false) Long postId, @RequestParam(required = false) Integer userId) {
        return ApiResponse.success(this.commentService.getAllComment(postId, userId), "Get all comment successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> getComment(@PathVariable Long id) {
        return ApiResponse.success(this.commentService.getComment(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(@Valid @RequestBody CommentRequestDTO inputComment){
        return ApiResponse.created(this.commentService.createComment(inputComment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> updateComment(@Valid @PathVariable Long id, @RequestBody CommentRequestDTO inputComment){
        return ApiResponse.success(this.commentService.updateComment(id, inputComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@Valid @PathVariable Long id){
        this.commentService.deleteComment(id);
        return ApiResponse.success("Delete comment successfully");
    }
}
