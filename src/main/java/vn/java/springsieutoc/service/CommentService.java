package vn.java.springsieutoc.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.java.springsieutoc.helper.exception.ResourceNotFoundException;
import vn.java.springsieutoc.model.Comment;
import vn.java.springsieutoc.model.dto.CommentRequestDTO;
import vn.java.springsieutoc.model.dto.CommentResponseDTO;
import vn.java.springsieutoc.repository.CommentRepository;
import vn.java.springsieutoc.repository.PostRepository;
import vn.java.springsieutoc.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
                .user(this.userRepository.findById(commentRequestDTO.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("User not found!")))
                .post(this.postRepository.findById(commentRequestDTO.getPost().getId()).orElseThrow(() -> new ResourceNotFoundException("post not found")))
                .isApproved(commentRequestDTO.isApproved())
                .createdAt(commentRequestDTO.getCreatedAt())
                .updatedAt(commentRequestDTO.getUpdatedAt())
                .build();
    }

    public List<CommentResponseDTO> getAllComment(Long postId, Integer userId) {
        if (postId == null && userId == null) {
            return this.commentRepository.findAll().stream().map(comment -> convertToCommentResponseDTO(comment)).collect(Collectors.toList());
        }
        if (postId != null && userId == null) {
            return this.commentRepository.findByPost_Id(postId).stream().map(comment -> convertToCommentResponseDTO(comment)).collect(Collectors.toList());
        }
        if (postId == null && userId != null) {
            return this.commentRepository.findByUser_Id(userId).stream().map(comment -> convertToCommentResponseDTO(comment)).collect(Collectors.toList());
        }else
            return this.commentRepository.findByPost_IdAndUser_Id(postId, userId).stream().map(comment -> convertToCommentResponseDTO(comment)).collect(Collectors.toList());
    }

    public CommentResponseDTO getComment(Long id) {
        return this.commentRepository.findById(id).map(comment -> convertToCommentResponseDTO(comment)).orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));
    }

    public CommentResponseDTO createComment(CommentRequestDTO inputComment) {
        return convertToCommentResponseDTO(this.commentRepository.save(convertToComment(inputComment)));
    }

    public CommentResponseDTO updateComment(Long id, CommentRequestDTO inputComment) {

        Comment outputComment = this.commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment not found!"));
        outputComment.setContent(inputComment.getContent());

//        outputComment.setUser(outputComment.getUser());
//        outputComment.setPost(outputComment.getPost());

        outputComment.setApproved(inputComment.isApproved());
        outputComment.setCreatedAt(inputComment.getCreatedAt());
        outputComment.setUpdatedAt(inputComment.getUpdatedAt());
        return convertToCommentResponseDTO(this.commentRepository.save(outputComment));
    }

    public void deleteComment(Long id) {
        getComment(id);
        this.commentRepository.deleteById(id);
    }
}
