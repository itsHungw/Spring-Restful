package vn.java.springsieutoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.java.springsieutoc.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost_IdAndUser_Id(Long postID, int userId);
    List<Comment> findByPost_Id(Long postID);
    List<Comment> findByUser_Id(Integer userId);
}
