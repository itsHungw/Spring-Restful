package vn.java.springsieutoc.service.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import vn.java.springsieutoc.model.Post;
import vn.java.springsieutoc.model.Tag;
import vn.java.springsieutoc.model.dto.PostRequestFilterDTO;

public class PostSpecification {

    public static Specification<Post> hasTitle(PostRequestFilterDTO filter) {
        return (root, query, cb) -> {
            if (filter.getTitle() == null) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%");
        };
    }

    public static Specification<Post> hasContent(PostRequestFilterDTO filter) {
        return (root, query, cb) -> {
            if (filter.getContent() == null) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("content")), "%" + filter.getContent().toLowerCase() + "%");
        };
    }

    public static Specification<Post> hasTag(PostRequestFilterDTO filter) {
        return (root, query, cb) -> {
            if (filter.getTags() == null || filter.getTags().isEmpty()) {
                return cb.conjunction();
            }
            Join<Post, Tag> tagJoin = root.join("tags", JoinType.INNER);
            return cb.in(tagJoin.get("name")).value(filter.getTags());
        };
    }

}
