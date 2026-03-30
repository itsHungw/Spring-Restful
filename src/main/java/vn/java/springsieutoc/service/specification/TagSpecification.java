package vn.java.springsieutoc.service.specification;

import org.springframework.data.jpa.domain.Specification;

import vn.java.springsieutoc.model.Tag;
import vn.java.springsieutoc.model.dto.TagRequestFilterDTO;

public class TagSpecification {

    public static Specification<Tag> hasName(TagRequestFilterDTO filter) {
        return (root, query, cb) -> {
            if (filter.getName() == null) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%");
        };
    }

}
