package vn.java.springsieutoc.service.specification;

import org.springframework.data.jpa.domain.Specification;

import vn.java.springsieutoc.model.Comment;
import vn.java.springsieutoc.model.dto.CommentRequestFilterDTO;

public class CommentSpecification {

    public static Specification<Comment> hasUserId(CommentRequestFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter.getUserId() == null || filter.getUserId().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), filter.getUserId());
        };
    }

    public static Specification<Comment> hasPostId(CommentRequestFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter.getPostId() == null || filter.getPostId().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("post").get("id"), filter.getPostId());
        };
    }

    public static Specification<Comment> hasIsApproved(CommentRequestFilterDTO filter) {
        return (root, query, criteriaBuilder) -> {
            if (filter.getIsApproved() == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isApproved"), filter.getIsApproved());
        };
    }

    // public static Specification<Comment> hasCreatedAt(CommentRequestFilterDTO
    // filter) {
    // return (root, query, criteriaBuilder) -> {
    // if (filter.getCreatedAt() == null) {
    // return criteriaBuilder.conjunction();
    // }
    // return criteriaBuilder.equal(root.get("createdAt"), filter.getCreatedAt());
    // };
    // }

    // public static Specification<Comment> hasUpdatedAt(CommentRequestFilterDTO
    // filter) {
    // return (root, query, criteriaBuilder) -> {
    // if (filter.getUpdatedAt() == null) {
    // return criteriaBuilder.conjunction();
    // }
    // return criteriaBuilder.equal(root.get("updatedAt"), filter.getUpdatedAt());
    // };
    // }
}
