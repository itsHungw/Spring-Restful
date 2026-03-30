package vn.java.springsieutoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.java.springsieutoc.helper.ApiResponse;
import vn.java.springsieutoc.helper.PageResponse;
import vn.java.springsieutoc.model.dto.CommentRequestDTO;
import vn.java.springsieutoc.model.dto.CommentRequestFilterDTO;
import vn.java.springsieutoc.model.dto.CommentResponseDTO;
import vn.java.springsieutoc.service.CommentService;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<?> getAllComment(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, // sort theo newest
                                                                                                     // comment first
            CommentRequestFilterDTO filter) {
        return ApiResponse.success(PageResponse.from(this.commentService.getAllComment(filter, pageable)),
                "Get all comment successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> getComment(@PathVariable Long id) {
        return ApiResponse.success(this.commentService.getComment(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(
            @Valid @RequestBody CommentRequestDTO inputComment) {
        return ApiResponse.created(this.commentService.createComment(inputComment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> updateComment(@PathVariable Long id,
            @Valid @RequestBody CommentRequestDTO inputComment) {
        return ApiResponse.success(this.commentService.updateComment(id, inputComment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long id) {
        this.commentService.deleteComment(id);
        return ApiResponse.success("Delete comment successfully");
    }
}
