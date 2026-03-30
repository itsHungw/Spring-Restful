package vn.java.springsieutoc.service;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.java.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.java.springsieutoc.model.Comment;
import vn.java.springsieutoc.model.dto.CommentRequestDTO;
import vn.java.springsieutoc.model.dto.CommentRequestFilterDTO;
import vn.java.springsieutoc.model.dto.CommentResponseDTO;
import vn.java.springsieutoc.repository.CommentRepository;
import vn.java.springsieutoc.repository.PostRepository;
import vn.java.springsieutoc.repository.UserRepository;
import vn.java.springsieutoc.service.specification.CommentSpecification;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponseDTO convertToCommentResponseDTO(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(new CommentResponseDTO.OutputUser(comment.getUser().getId(), comment.getUser().getName()))
                .post(new CommentResponseDTO.OutputPost(comment.getPost().getId(), comment.getPost().getTitle()))
                .isApproved(comment.isApproved())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public Comment convertToComment(CommentRequestDTO commentRequestDTO) {
        return Comment.builder()
                .content(commentRequestDTO.getContent())
                .user(this.userRepository.findById(commentRequestDTO.getUser().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found!")))
                .post(this.postRepository.findById(commentRequestDTO.getPost().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("post not found")))
                .isApproved(commentRequestDTO.isApproved())
                .createdAt(commentRequestDTO.getCreatedAt())
                .updatedAt(commentRequestDTO.getUpdatedAt())
                .build();
    }

    public Page<CommentResponseDTO> getAllComment(CommentRequestFilterDTO filter, Pageable pageable) {
        Specification<Comment> specification = Specification.allOf(
                CommentSpecification.hasUserId(filter),
                CommentSpecification.hasPostId(filter),
                CommentSpecification.hasIsApproved(filter));
        // CommentSpecification.hasCreatedAt(filter),
        // CommentSpecification.hasUpdatedAt(filter));
        Page<Comment> comments = this.commentRepository.findAll(specification, pageable);

        return comments.map(comment -> convertToCommentResponseDTO(comment));
    }

    public CommentResponseDTO getComment(Long id) {
        return this.commentRepository.findById(id).map(comment -> convertToCommentResponseDTO(comment))
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));
    }

    public CommentResponseDTO createComment(CommentRequestDTO inputComment) {
        return convertToCommentResponseDTO(this.commentRepository.save(convertToComment(inputComment)));
    }

    public CommentResponseDTO updateComment(Long id, CommentRequestDTO inputComment) {

        Comment outputComment = this.commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));

        if (inputComment.getContent() != null) {
            outputComment.setContent(inputComment.getContent());
        }

        // outputComment.setUser(outputComment.getUser());
        // outputComment.setPost(outputComment.getPost());

        outputComment.setApproved(inputComment.isApproved());
        // outputComment.setCreatedAt(inputComment.getCreatedAt());
        // outputComment.setUpdatedAt(inputComment.getUpdatedAt());
        return convertToCommentResponseDTO(this.commentRepository.save(outputComment));
    }

    public void deleteComment(Long id) {
        getComment(id);
        this.commentRepository.deleteById(id);
    }

}
