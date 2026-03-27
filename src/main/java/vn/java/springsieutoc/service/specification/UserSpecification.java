package vn.java.springsieutoc.service.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import vn.java.springsieutoc.model.Role;
import vn.java.springsieutoc.model.User;
import vn.java.springsieutoc.model.dto.UserRequestFilterDTO;

public class UserSpecification {

    public static Specification<User> hasName(UserRequestFilterDTO filter) {
        return (root, query, cb) -> {
            if (filter.getName() == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("name"), filter.getName());
        };
    }

    public static Specification<User> hasEmail(UserRequestFilterDTO filter) {
        return (root, query, cb) -> {
            if (filter.getEmail() == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("email"), filter.getEmail());
        };
    }

    public static Specification<User> hasRole(UserRequestFilterDTO filter) {
        return (root, query, cb) -> {
            if (filter.getRoleName() == null) {
                return cb.conjunction();
            }

            Join<User, Role> roleJoin = root.join("role", JoinType.INNER);
            return cb.equal(cb.lower(roleJoin.get("name")), filter.getRoleName().toLowerCase());

        };
    }

    public static Specification<User> hasAddress(UserRequestFilterDTO filter) {
        return (root, query, cb) -> {
            if (filter.getAddress() == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("address"), filter.getAddress());
        };
    }

}

