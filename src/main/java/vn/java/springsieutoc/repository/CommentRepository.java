package vn.java.springsieutoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import vn.java.springsieutoc.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findByPost_IdAndUser_Id(Long postID, int userId);

    List<Comment> findByPost_Id(Long postID);

    List<Comment> findByUser_Id(Integer userId);
}
